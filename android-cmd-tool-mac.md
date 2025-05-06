
## Configuration and Setup

### 📦 Download the Android SDK

Download the Android SDK zip file from one of the following links:

- 👉 [Download Android SDK for macOS (~2GB)](https://drive.google.com/file/d/1AESybZmmfmnwEooQDFpw5vs5QEEPeao7/view?usp=sharing)  
- 👉 [Download Android SDK for Windows (~2GB)](https://drive.google.com/file/d/1aNsnMuwSMW1GUR7kjza8gWgW_OJpgqAs/view?usp=sharing)

After downloading, **move the `android_sdk.zip` file to your project directory**, then run the following commands:

```bash
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
````

### 🖥️ Create Device and Launch Emulator

```bash
# Create a new AVD (Android Virtual Device) named 'iphone'
avdmanager create avd -n iphone \
  --device pixel \
  -k "system-images;android-33;google_apis_playstore;x86_64"

# Launch the emulator
emulator -avd iphone
```

### 🧹 Cleanup

After the setup completes, you can safely remove the `android_sdk.zip` file if it's no longer needed:

```bash
rm android_sdk.zip
```

```

