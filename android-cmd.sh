mkdir ~/.android_sdk
unzip nodejs/node_modules/@dankieu/appium-base-setup/commandlinetools-mac-13114758_latest.zip -d ~/.android_sdk/cmdline-tools

export ANDROID_SDK_ROOT="$HOME/.android_sdk"
export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$PATH"
export PATH="$ANDROID_SDK_ROOT/emulator:$PATH"

source ~/.zshrc   # or source ~/.bashrc

$ANDROID_SDK_ROOT/cmdline-tools/bin/sdkmanager \
--sdk_root=$ANDROID_SDK_ROOT "cmdline-tools;latest"

sdkmanager "platforms;android-33" "build-tools;33.0.2"
sdkmanager "platform-tools" "tools"
sdkmanager "emulator"
sdkmanager --licenses

sdkmanager "system-images;android-33;google_apis_playstore;x86_64"

avdmanager create avd -n iphone \
--device pixel -k "system-images;android-33;google_apis_playstore;x86_64"

emulator @iphone
