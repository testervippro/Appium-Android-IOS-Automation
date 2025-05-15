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
    rm -rf "$ANDROID_SDK_ROOT"
    echo "Successfully removed Android SDK directory."
else
    echo "Android SDK directory does not exist: $ANDROID_SDK_ROOT"
fi

# Remove all files in the AVD directory
if [ -d "$AVD_DIR" ]; then
    echo "Removing all AVD files in $AVD_DIR..."
    rm -rf "$AVD_DIR"/*
    echo "Successfully removed all files from $AVD_DIR."
else
    echo  echo "AVD directory does not exist: $AVD_DIR"
fi

# Remove ANDROID_SDK_ROOT and related PATH settings from .zshrc
echo "Removing Android SDK related settings from .zshrc..."
sed -i '' '/ANDROID_SDK_ROOT/d' ~/.zshrc
sed -i '' '/ANDROID_HOME/d' ~/.zshrc
sed -i '' '/cmdline-tools/d' ~/.zshrc
sed -i '' '/platform-tools/d' ~/.zshrc
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

# Export environment variables and persist them in .zshrc
export ANDROID_SDK_ROOT
export ANDROID_HOME="$ANDROID_SDK_ROOT"
export PATH="$CMDLINE_TOOLS_DIR/bin:$ANDROID_SDK_ROOT/platform-tools:$ANDROID_SDK_ROOT/emulator:$PATH"

source ~/.zshrc

# Persist environment variables in .zshrc
echo "export ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT" >> ~/.zshrc
echo "export ANDROID_HOME=$ANDROID_SDK_ROOT" >> ~/.zshrc
echo "export PATH=$CMDLINE_TOOLS_DIR/bin:$ANDROID_SDK_ROOT/platform-tools:$ANDROID_SDK_ROOT/emulator:$PATH" >> ~/.zshrc

source ~/.zshrc
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

# Verify platform-tools installation
if [ ! -f "$ANDROID_SDK_ROOT/platform-tools/adb" ]; then
    echo "Error: platform-tools installation failed. Reinstalling..."
    sdkmanager --sdk_root="$ANDROID_SDK_ROOT" "platform-tools"
fi

# Step 3: Create AVD if it doesn't exist
if ! avdmanager list avd | grep -q "Name: pixel_android"; then
    echo "Creating AVD named 'pixel_android'..."
    echo "no" | avdmanager create avd -n pixel_android \
      --device pixel \
      -k "system-images;android-33;google_apis_playstore;x86_64"
else
    echo "AVD 'iphone' already exists. Skipping creation."
fi

# Step 4: Launch the emulator for the 'pixel_android' AVD
echo "Launching emulator for 'iphone' AVD..."
emulator -avd iphone &

# Step 5: Verify adb is accessible
echo "Verifying adb installation..."
if command -v adb >/dev/null; then
    echo "adb is installed and accessible."
    adb devices
else
    echo "Error: adb not found. Please check the platform-tools installation."
    exit 1
fi
