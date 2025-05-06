#!/bin/bash

# Define the Android SDK and AVD directories
ANDROID_SDK_ROOT="$HOME/.android_sdk"
AVD_DIR="$HOME/.android/avd"
CMDLINE_TOOLS_DIR="$ANDROID_SDK_ROOT/cmdline-tools/latest"

# Step 1: Clean up the existing Android SDK directory and AVD files
echo "Cleaning up existing Android SDK and AVD files..."

# Remove the Android SDK directory if it exists
if [ -d "$ANDROID_SDK_ROOT" ]; then
    echo "Removing Android SDK directory: $ANDROID_SDK_ROOT"
    rm -rf $ANDROID_SDK_ROOT
    echo "Successfully removed Android SDK directory."
else
    echo "Android SDK directory does not exist: $ANDROID_SDK_ROOT"
fi

# Remove all files in the AVD directory
if [ -d "$AVD_DIR" ]; then
    echo "Removing all AVD files in $AVD_DIR..."
    rm -rf $AVD_DIR/*
    echo "Successfully removed all files from $AVD_DIR."
else
    echo "AVD directory does not exist: $AVD_DIR"
fi

# Remove ANDROID_SDK_ROOT and related PATH settings from .zshrc
echo "Removing Android SDK related settings from .zshrc..."
sed -i '' '/ANDROID_SDK_ROOT/d' ~/.zshrc
sed -i '' '/ANDROID_HOME/d' ~/.zshrc
sed -i '' '/cmdline-tools/d' ~/.zshrc
sed -i '' '/emulator/d' ~/.zshrc

# Apply changes to .zshrc
source ~/.zshrc

# Step 2: Set up the Android SDK again if it doesn't exist
echo "Setting up Android SDK..."

# Create the Android SDK directory if it doesn't exist
if [ ! -d "$ANDROID_SDK_ROOT" ]; then
    echo "Creating Android SDK directory: $ANDROID_SDK_ROOT"
    mkdir -p "$ANDROID_SDK_ROOT"
fi

# Set up cmdline-tools/latest if not already installed
if [ ! -d "$CMDLINE_TOOLS_DIR/bin" ]; then
    echo "Installing command line tools in the correct location..."
    mkdir -p "$CMDLINE_TOOLS_DIR"
    unzip -q appium-base-setup/commandlinetools-mac-13114758_latest.zip -d "$CMDLINE_TOOLS_DIR/tmp"
    mv "$CMDLINE_TOOLS_DIR/tmp/cmdline-tools/"* "$CMDLINE_TOOLS_DIR"
    rm -rf "$CMDLINE_TOOLS_DIR/tmp"
else
    echo "Command line tools already installed. Skipping unzip."
fi

# Export environment variables
export ANDROID_SDK_ROOT
export PATH="$CMDLINE_TOOLS_DIR/bin:$ANDROID_SDK_ROOT/emulator:$PATH"
export ANDROID_HOME="$ANDROID_SDK_ROOT"

# Install SDK packages
echo "Installing necessary SDK packages..."
yes | sdkmanager --sdk_root="$ANDROID_SDK_ROOT" --licenses
sdkmanager --sdk_root="$ANDROID_SDK_ROOT" \
  "cmdline-tools;latest" \
  "platform-tools" \
  "build-tools;33.0.2" \
  "platforms;android-33" \
  "emulator" \
  "system-images;android-33;google_apis_playstore;x86_64"

# Step 3: Create AVD if it doesn't exist
if ! avdmanager list avd | grep -q "Name: iphone"; then
    echo "Creating AVD named 'iphone'..."
    echo "no" | avdmanager create avd -n iphone \
      --device pixel \
      -k "system-images;android-33;google_apis_playstore;x86_64"
else
    echo "AVD 'iphone' already exists. Skipping creation."
fi

# Step 4: Optionally launch the emulator for the 'iphone' AVD
echo "Launching emulator for 'iphone' AVD..."
emulator @iphone
