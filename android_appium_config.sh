#!/bin/bash

# Define paths
APK_PATH="/Users/mac/Documents/Appium-Android-IOS-Automation/src/test/resources/androidapp/app-android.apk"
AAPT_PATH="/Users/mac/Library/Android/sdk/build-tools/35.0.0/aapt"
OUTPUT_FILE="android.json"

# Collect device info
PLATFORM_VERSION=$(adb shell getprop ro.build.version.release)
DEVICE_NAME=$(adb devices | grep -w "device" | awk '{print $1}')

# Get app package and activity
if [ -f "$AAPT_PATH" ] && [ -f "$APK_PATH" ]; then
    APP_PACKAGE=$("$AAPT_PATH" dump badging "$APK_PATH" | grep "package: name=" | awk -F"'" '{print $2}')
    APP_ACTIVITY=$("$AAPT_PATH" dump badging "$APK_PATH" | grep "launchable-activity: name=" | awk -F"'" '{print $2}')
else
    APP_PACKAGE="aapt_or_apk_not_found"
    APP_ACTIVITY="aapt_or_apk_not_found"
fi

# Create JSON file with proper formatting
{
  echo '{'
  echo '  "platformName": "Android",'
  echo '  "platformVersion": "'"$PLATFORM_VERSION"'",'
  echo '  "deviceName": "'"$DEVICE_NAME"'",'
  echo '  "app": "'"$APK_PATH"'",'
  echo '  "appPackage": "'"$APP_PACKAGE"'",'
  echo '  "appActivity": "'"$APP_ACTIVITY"'",'
  echo '  "automationName": "UiAutomator2"'
  echo '}'
} > "$OUTPUT_FILE"

echo "Android capabilities successfully written to $OUTPUT_FILE"