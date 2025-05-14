#!/bin/bash

# ------------------------------------------
# Step 0: Define constants and download zip
# ------------------------------------------

TOOLS_ZIP="commandlinetools-mac-9477386_latest.zip"
TOOLS_URL="https://dl.google.com/android/repository/${TOOLS_ZIP}"
ANDROID_SDK_ROOT="$HOME/.android_sdk"
CMDLINE_TOOLS_DIR="$ANDROID_SDK_ROOT/cmdline-tools/latest"
AVD_DIR="$HOME/.android/avd"

# Download to current directory
if [ ! -f "$TOOLS_ZIP" ]; then
    echo "Downloading Android command line tools to current directory..."
    curl -o "$TOOLS_ZIP" "$TOOLS_URL"
    echo "Download complete: $TOOLS_ZIP"
else
    echo "File already exists in current directory: $TOOLS_ZIP"
fi

# ------------------------------------------
# Step 1: Clean up existing SDK & AVDs
# ------------------------------------------

echo "Cleaning up old SDK and AVDs..."

if [ -d "$ANDROID_SDK_ROOT" ]; then
    echo "Removing Android SDK directory: $ANDROID_SDK_ROOT"
    rm -rf "$ANDROID_SDK_ROOT"
fi

if [ -d "$AVD_DIR" ]; then
    echo "Removing all AVD files in $AVD_DIR..."
    rm -rf "$AVD_DIR"/*
fi

# Remove SDK-related lines in .zshrc
echo "Cleaning up environment variables in .zshrc..."
sed -i '' '/ANDROID_SDK_ROOT/d' ~/.zshrc
sed -i '' '/ANDROID_HOME/d' ~/.zshrc
sed -i '' '/cmdline-tools/d' ~/.zshrc
sed -i '' '/emulator/d' ~/.zshrc

# Reload shell config
source ~/.zshrc

# ------------------------------------------
# Step 2: Extract tools into SDK directory
# ------------------------------------------

echo "Setting up Android SDK..."

mkdir -p "$CMDLINE_TOOLS_DIR"
TEMP_UNZIP_DIR="./tools-unzipped"

unzip -q "$TOOLS_ZIP" -d "$TEMP_UNZIP_DIR"
mv "$TEMP_UNZIP_DIR/cmdline-tools"/* "$CMDLINE_TOOLS_DIR"
rm -rf "$TEMP_UNZIP_DIR"

# ------------------------------------------
# Step 3: Export env vars
# ------------------------------------------

export ANDROID_SDK_ROOT
export ANDROID_HOME="$ANDROID_SDK_ROOT"
export PATH="$CMDLINE_TOOLS_DIR/bin:$ANDROID_SDK_ROOT/emulator:$PATH"

# Optionally add them to .zshrc permanently
echo "export ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT" >> ~/.zshrc
echo "export ANDROID_HOME=$ANDROID_SDK_ROOT" >> ~/.zshrc
echo "export PATH=$CMDLINE_TOOLS_DIR/bin:$ANDROID_SDK_ROOT/emulator:\$PATH" >> ~/.zshrc

# ------------------------------------------
# Step 4: Install SDK packages
# ------------------------------------------

echo "Installing SDK packages..."

yes | sdkmanager --sdk_root="$ANDROID_SDK_ROOT" --licenses

sdkmanager --sdk_root="$ANDROID_SDK_ROOT" \
  "cmdline-tools;latest" \
  "platform-tools" \
  "build-tools;33.0.2" \
  "platforms;android-33" \
  "emulator" \
  "system-images;android-33;google_apis_playstore;x86_64"

# ------------------------------------------
# Step 5: Create AVD
# ------------------------------------------

if ! avdmanager list avd | grep -q "Name: iphone"; then
    echo "Creating AVD named 'iphone'..."
    echo "no" | avdmanager create avd -n iphone \
      --device "pixel" \
      -k "system-images;android-33;google_apis_playstore;x86_64"
else
    echo "AVD 'iphone' already exists."
fi

# ------------------------------------------
# Step 6: Launch Emulator
# ------------------------------------------

echo "Launching emulator for 'iphone' AVD..."
emulator @iphone
