#!/bin/bash

# Define directories
ANDROID_SDK_ROOT="$HOME/.android_sdk"
AVD_DIR="$HOME/.android/avd"
CMDLINE_TOOLS_DIR="$ANDROID_SDK_ROOT/cmdline-tools/latest"
TOOLS_ZIP="commandlinetools-mac-9477386_latest.zip"
TOOLS_URL="https://dl.google.com/android/repository/${TOOLS_ZIP}"
TOOLS_TMP="$ANDROID_SDK_ROOT/tools-temp"

# Step 0: Download the command line tools
mkdir -p "$TOOLS_TMP"
if [ ! -f "$TOOLS_TMP/$TOOLS_ZIP" ]; then
    echo "Downloading Android command line tools..."
    curl -o "$TOOLS_TMP/$TOOLS_ZIP" "$TOOLS_URL"
else
    echo "Command line tools already downloaded."
fi

# Step 1: Clean up old SDK and AVDs
echo "Cleaning up existing Android SDK and AVD files..."

if [ -d "$ANDROID_SDK_ROOT" ]; then
    echo "Removing Android SDK directory: $ANDROID_SDK_ROOT"
    rm -rf "$ANDROID_SDK_ROOT"
    echo "Successfully removed Android SDK directory."
else
    echo "Android SDK directory does not exist: $ANDROID_SDK_ROOT"
fi

if [ -d "$AVD_DIR" ]; then
    echo "Removing all AVD files in $AVD_DIR..."
    rm -rf "$AVD_DIR"/*
    echo "Successfully removed all files from $AVD_DIR."
else
    echo "AVD directory does not exist: $AVD_DIR"
fi

echo "Removing Android SDK related settings from .zshrc..."
sed -i '' '/ANDROID_SDK_ROOT/d' ~/.zshrc
sed -i '' '/ANDROID_HOME/d' ~/.zshrc
sed -i '' '/cmdline-tools/d' ~/.zshrc
sed -i '' '/emulator/d' ~/.zshrc

source ~/.zshrc

# Step 2: Set up SDK and extract tools
echo "Setting up Android SDK..."
mkdir -p "$CMDLINE_TOOLS_DIR"
unzip -q "$TOOLS_TMP/$TOOLS_ZIP" -d "$TOOLS_TMP/unzipped"
mv "$TOOLS_TMP/unzipped/cmdline-tools"/* "$CMDLINE_TOOLS_DIR"
rm -rf "$TOOLS_TMP"

# Export environment variables
export ANDROID_SDK_ROOT
export PATH="$CMDLINE_TOOLS_DIR/bin:$ANDROID_SDK_ROOT/emulator:$PATH"
export ANDROID_HOME="$ANDROID_SDK_ROOT"

# Step 3: Install SDK packages
echo "Installing necessary SDK packages..."
yes | sdkmanager --sdk_root="$ANDROID_SDK_ROOT" --licenses
sdkmanager --sdk_root="$ANDROID_SDK_ROOT" \
  "cmdline-tools;latest" \
  "platform-tools" \
  "build-tools;33.0.2" \
  "platforms;android-33" \
  "emulator" \
  "system-images;android-33;google_apis_playstore;x86_64"

# Step 4: Create AVD
if ! avdmanager list avd | grep -q "Name: iphone"; then
    echo "Creating AVD named 'iphone'..."
    echo "no" | avdmanager create avd -n iphone \
      --device "pixel" \
      -k "system-images;android-33;google_apis_playstore;x86_64"
else
    echo "AVD 'iphone' already exists. Skipping creation."
fi

# Step 5: Launch emulator
echo "Launching emulator for 'iphone' AVD..."
emulator @iphone
