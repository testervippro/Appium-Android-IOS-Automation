
## Configuration and Setup
Run the following command to clone the repository:

```bash
git clone https://github.com/testervippro/appium-base-setup.git
```

After the clone finishes, follow these instructions:

### 📦 Way 1: Download the Android SDK

Download the Android SDK zip file from one of the following links:

- 👉 [Download Android SDK for macOS (~2GB)](https://drive.google.com/file/d/1AESybZmmfmnwEooQDFpw5vs5QEEPeao7/view?usp=sharing)  
- 👉 [Download Android SDK for Windows (~2GB)](https://drive.google.com/file/d/1aNsnMuwSMW1GUR7kjza8gWgW_OJpgqAs/view?usp=sharing)

After downloading, **move the `android_sdk.zip` file to your project directory**, then run the following commands:

#### MacOS

```bash
chmod +x install_android_cmd_tool_mac_v1.sh && ./install_android_cmd_tool_mac_v1.sh
````

#### Windows

```bash
node appium-base-setup/install_android_cmd_tool_win_v1.js
```

Then, launch the emulator:

### 🖥️ Create Device and Launch Emulator
MAc
```bash
# Create a new AVD (Android Virtual Device) named 'iphone'
avdmanager create avd -n iphone \
  --device pixel \
  -k "system-images;android-33;google_apis_playstore;x86_64"
```

```bash
# Launch the emulator
emulator -avd iphone
```
Win
```bash
# Launch the emulator
avdmanager.bat create avd -n iphone --device "pixel" -k "system-images;android-33;google_apis_playstore;x86_64"
```

```bash
# Launch the emulator
emulator -avd iphone
```

### 🧹 Cleanup

After the setup completes, you can safely remove the `android_sdk.zip` file if it's no longer needed:

```bash
rm android_sdk.zip
```

---

### Way 2: Clone the Repository


#### MacOS

```bash
chmod +x install_android_cmd_tool_mac_v2.sh && ./install_android_cmd_tool_mac_v2.sh
```

#### Windows

```bash
node appium-base-setup/install_android_cmd_tool_win_v2.js
```

It will automatically launch a device named "iphone."

```
