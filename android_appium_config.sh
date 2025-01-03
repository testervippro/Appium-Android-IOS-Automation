#!/bin/bash

# Define the APK path
APK_PATH="/Users/mac/Documents/Appium-Android-IOS-Automation/appium-automation/src/test/resources/androidapp/app-android.apk"

# Define the full path to aapt
AAPT_PATH="/Users/mac/Library/Android/sdk/build-tools/35.0.0/aapt"

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

# Verify appPackage and appActivity using aapt
if [ -f "$AAPT_PATH" ]; then
    APP_PACKAGE=$("$AAPT_PATH" dump badging "$APK_PATH" | grep "package: name=" | awk -F"'" '{print $2}')
    APP_ACTIVITY=$("$AAPT_PATH" dump badging "$APK_PATH" | grep "launchable-activity: name=" | awk -F"'" '{print $2}')
    JSON_OUTPUT+="\"appPackage\": \"$APP_PACKAGE\", "
    JSON_OUTPUT+="\"appActivity\": \"$APP_ACTIVITY\", "
else
    JSON_OUTPUT+="\"appPackage\": \"aapt not found\", "
    JSON_OUTPUT+="\"appActivity\": \"aapt not found\", "
fi

# Verify automationName (always UiAutomator2 for Android)
JSON_OUTPUT+="\"automationName\": \"UiAutomator2\""

# Close JSON output
JSON_OUTPUT+="}"

# Print JSON output
echo "$JSON_OUTPUT"