#!/bin/bash

# Automatically retrieve values for desired capabilities

# Get the device name (for simulators)
deviceName=$(xcrun simctl list devices | grep Booted | awk -F'[()]' '{print $1}' | xargs)

# Get the UDID of the booted simulator
udid=$(xcrun simctl list devices | grep Booted | awk -F'[()]' '{print $2}' | xargs)

# Set platform name (always iOS for iOS devices)
platformName="iOS"

# Get the platform version of the booted simulator
platformVersion=$(xcrun simctl list devices | grep Booted | awk -F'[()]' '{print $2}' | xargs | cut -d'.' -f1-2)

# Set the app path dynamically
app="/Users/mac/Documents/Appium-Android-IOS-Automation/appium-automation/src/test/resources/iosapp/My Demo App.app"

# Set the automation name (always XCUITest for iOS)
automationName="XCUITest"

# Set noReset to true (modify as needed)
noReset=true

# Set the updated WebDriverAgent bundle ID (optional)
updatedWDABundleId="com.yourcompany.WebDriverAgentRunner"

# Set useNewWDA to true (modify as needed)
useNewWDA=true

# Generate the JSON
cat <<EOF
{
  "deviceName": "$deviceName",
  "udid": "$udid",
  "platformName": "$platformName",
  "platformVersion": "$platformVersion",
  "app": "$app",
  "automationName": "$automationName",
  "noReset": $noReset,
  "updatedWDABundleId": "$updatedWDABundleId",
  "useNewWDA": $useNewWDA
}
EOF