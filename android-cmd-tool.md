Macos
# 1. Create SDK directory
mkdir -p ~/.android_sdk

# 2. Unzip command-line tools into proper structure
unzip nodejs/src/commandlinetools-mac.zip -d ~/.android_sdk/cmdline-tools
mkdir -p ~/.android_sdk/cmdline-tools/latest
mv ~/.android_sdk/cmdline-tools/cmdline-tools/* ~/.android_sdk/cmdline-tools/latest/

# 3. Set environment variables
export ANDROID_SDK_ROOT="$HOME/.android_sdk"
export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/emulator:$PATH"

# 4. Install necessary SDK components for Appium
sdkmanager "platforms;android-33" "build-tools;33.0.2" "platform-tools" "system-images;android-33;google_apis;x86_64"

# 5. Accept licenses
yes | sdkmanager --licenses

# 6. Create and start AVD (Android Virtual Device)
avdmanager create avd -n iphone --device "pixel" \
  -k "system-images;android-33;google_apis;x86_64" --force

# 7. Launch the emulator
emulator @iphone
