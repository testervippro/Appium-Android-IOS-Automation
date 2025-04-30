#!/bin/bash

# Default device name
DEFAULT_DEVICE="iPhone 13"

# Use the provided device name or default to "iPhone 13"
DEVICE_NAME="${1:-$DEFAULT_DEVICE}"

echo "Building WebDriverAgent for device: $DEVICE_NAME"

# Path to WebDriverAgent directory
WDA_PATH="$HOME/.appium/node_modules/appium-xcuitest-driver/node_modules/appium-webdriveragent"

# Navigate to WebDriverAgent directory
cd "$WDA_PATH" || { echo "WebDriverAgent directory not found at $WDA_PATH"; exit 1; }

# Install dependencies using Carthage
echo "Installing dependencies..."
./Scripts/bootstrap.sh || { echo "Failed to install dependencies"; exit 1; }

# Build WebDriverAgent on the specified device (default or provided)
echo "Building WebDriverAgent on $DEVICE_NAME..."

xcodebuild -project WebDriverAgent.xcodeproj -scheme WebDriverAgent -destination "platform=iOS Simulator,name=$DEVICE_NAME" test || { echo "Failed to build WebDriverAgent"; exit 1; }

# Output success message
echo "Successfully built WebDriverAgent on $DEVICE_NAME."
