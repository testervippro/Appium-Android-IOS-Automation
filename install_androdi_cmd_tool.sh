# Set environment variables
ANDROID_SDK_ROOT="$HOME/.android_sdk"
ZIP_PATH="android_sdk.zip"  # Assuming the zip file is in the current directory

# Step 1–3: Prepare SDK Directory, Unzip SDK, and Set Environment Variables
echo "Setting up Android SDK directory..."
mkdir -p "$ANDROID_SDK_ROOT"

echo "Unzipping Android SDK..."
unzip -q "$ZIP_PATH" -d "$ANDROID_SDK_ROOT"

echo "Setting environment variables..."
export ANDROID_SDK_ROOT
export ANDROID_HOME="$ANDROID_SDK_ROOT"
export PATH="$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator:$PATH"

# Reload Shell Configuration
source ~/.zshrc
