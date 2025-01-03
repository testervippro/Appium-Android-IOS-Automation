#!/bin/bash

# Define the APK path
APK_PATH="/Users/mac/Documents/Appium-Android-IOS-Automation/appium-automation/src/test/resources/androidapp/app-android.apk"

# Initialize JSON output
JSON_OUTPUT="{"

# Verify platformVersion
PLATFORM_VERSION=$(adb shell getprop ro.build.version.release)
JSON_OUTPUT+="\"platformVersion\": \"$PLATFORM_VERSION\", "

# Verify deviceName
DEVICE_NAME=$(adb devices | grep -w "device" | awk '{print $1}')
JSON_OUTPUT+="\"deviceName\": \"$DEVICE_NAME\", "

# Verify app (APK file path)
if [ -f "$APK_PATH" ]; then
    JSON_OUTPUT+="\"app\": \"$APK_PATH\", "
else
    JSON_OUTPUT+="\"app\": \"File not found\", "
fi

# Verify appPackage and appActivity
APP_PACKAGE=$(aapt dump badging "$APK_PATH" | grep "package: name=" | awk -F"'" '{print $2}')
APP_ACTIVITY=$(aapt dump badging "$APK_PATH" | grep "launchable-activity: name=" | awk -F"'" '{print $2}')
JSON_OUTPUT+="\"appPackage\": \"$APP_PACKAGE\", "
JSON_OUTPUT+="\"appActivity\": \"$APP_ACTIVITY\", "

# Verify automationName (always UiAutomator2 for Android)
JSON_OUTPUT+="\"automationName\": \"UiAutomator2\""

# Close JSON output
JSON_OUTPUT+="}"

# Print JSON output
echo "$JSON_OUTPUT"
