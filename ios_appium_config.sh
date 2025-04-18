#!/bin/bash

# Automatically retrieve values for desired capabilities
# Set the app path dynamically
app="/Users/mac/Documents/Appium-Android-IOS-Automation/src/test/resources/iosapp/My Demo App.app"


# Get the device name (for simulators)
deviceName=$(xcrun simctl list devices | grep Booted | awk -F'[()]' '{print $1}' | xargs)

# Get the UDID of the booted simulator
udid=$(xcrun simctl list devices | grep Booted | awk -F'[()]' '{print $2}' | xargs)

# Set platform name (always iOS for iOS devices)
platformName="iOS"

# Get the platform version of the booted simulator
platformVersion=$(xcrun simctl list devices | grep Booted | awk -F'[()]' '{print $2}' | xargs | cut -d'.' -f1-2)


# Set the automation name (always XCUITest for iOS)
automationName="XCUITest"

# Generate the JSON
cat <<EOF
{
  "deviceName": "$deviceName",
  "udid": "$udid",
  "platformName": "$platformName",
  "platformVersion": "$platformVersion",
  "app": "$app",
  "automationName": "$automationName",
  "appium:showXcodeLog": true
}
EOF