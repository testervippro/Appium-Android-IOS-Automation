#!/bin/bash

# ------------------------------------------
# Step 0: Define constants and download zip
# ------------------------------------------

TOOLS_ZIP="commandlinetools-mac-13114758_latest.zip"
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
