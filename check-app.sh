PACKAGE_NAME="com.saucelabs.mydemoapp.android"
ANR_PATTERN="Application Not Responding: $PACKAGE_NAME"

while true; do
    output=$(adb shell dumpsys window | grep -E 'mCurrentFocus|mFocusedApp')

    if echo "$output" | grep -q "$ANR_PATTERN"; then
        echo "ANR detected for $PACKAGE_NAME. Force stopping..."
        adb shell am force-stop "$PACKAGE_NAME"

    else
        echo "No ANR detected. Checking again in 5 seconds..."
        sleep 5
    fi
done
