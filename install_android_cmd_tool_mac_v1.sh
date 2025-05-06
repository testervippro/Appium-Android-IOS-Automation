#!/bin/bash

# Define environment variables
ANDROID_SDK_ROOT="$HOME/.android_sdk"
ANDROID_AVD="$HOME/.android"
ZIP_PATH="android_sdk.zip"  # Assumes the zip file is in the current directory

echo "Setting up Android SDK directory..."

# Remove existing SDK and AVD directories if they exist
if [ -d "$ANDROID_SDK_ROOT" ]; then
    echo "Removing existing Android SDK directory..."
    rm -rf "$ANDROID_SDK_ROOT"
fi

if [ -d "$ANDROID_AVD" ]; then
    echo "Removing existing Android AVD directory..."
    rm -rf "$ANDROID_AVD"
fi

# Create new SDK directory
mkdir -p "$ANDROID_SDK_ROOT"

# Unzip SDK
if [ -f "$ZIP_PATH" ]; then
    echo "Unzipping Android SDK..."
    unzip -q "$ZIP_PATH" -d "$ANDROID_SDK_ROOT"
else
    echo "Error: '$ZIP_PATH' not found!"
    exit 1
fi

# Set environment variables in current shell
echo "Setting environment variables..."
export ANDROID_SDK_ROOT
export ANDROID_HOME="$ANDROID_SDK_ROOT"

# Remove any previous Android paths from PATH (for this session only)
CLEANED_PATH=$(echo "$PATH" | awk -v RS=: -v ORS=: '!/android_sdk/ && !/cmdline-tools/ && !/platform-tools/ && !/emulator/' | sed 's/:$//')

# Add new Android tools paths
export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$CLEANED_PATH"

# Optionally, add to .zshrc or .bashrc for persistence
echo "Updating ~/.zshrc for persistent environment variables..."
{
    echo "# Android SDK Environment"
    echo "export ANDROID_SDK_ROOT=\"$ANDROID_SDK_ROOT\""
    echo "export ANDROID_HOME=\"$ANDROID_SDK_ROOT\""
    echo "export PATH=\"\$ANDROID_HOME/cmdline-tools/latest/bin:\$ANDROID_HOME/platform-tools:\$ANDROID_HOME/emulator:\$PATH\""
} >> ~/.zshrc

echo "Reloading shell configuration..."
source ~/.zshrc

echo "Android SDK setup complete."
