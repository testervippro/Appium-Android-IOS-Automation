

## 📦 Install Android SDK for Appium

### For **macOS** and **Windows**:

Both OS need to accept SDK licenses during installation.

### 1. Install the GitHub Package via NPM

```bash
npm install github:testervippro/appium-base-setup#main
```

---

### For **Windows**: Run the following to initialize the setup.

```bash
node nodejs/src/app.js
```

---

### 2. Extract Android Command Line Tools

For **macOS**, run the following commands:

```bash
# Create SDK directory
mkdir -p ~/.android_sdk

# Unzip command-line tools from the installed package
unzip nodejs/node_modules/@dankieu/appium-base-setup/commandlinetools-mac-13114758_latest.zip -d ~/.android_sdk/cmdline-tools

# Move to proper structure
mkdir -p ~/.android_sdk/cmdline-tools/latest
mv ~/.android_sdk/cmdline-tools/cmdline-tools/* ~/.android_sdk/cmdline-tools/latest/
```

---

### 3. Set Environment Variables

For **macOS**, add these lines to your shell profile (`~/.zshrc` or `~/.bashrc` or `~/.bash_profile`):

```bash
export ANDROID_SDK_ROOT="$HOME/.android_sdk"
export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$ANDROID_SDK_ROOT/emulator:$PATH"
```

Then, apply them:

```bash
source ~/.zshrc  # or source ~/.bashrc
```

---

### 4. Install Required SDK Components

```bash
sdkmanager "platforms;android-33" \
           "build-tools;33.0.2" \
           "platform-tools" \
           "system-images;android-33;google_apis;x86_64"
```

---

### 5. Accept All SDK Licenses

```bash
yes | sdkmanager --licenses
```

---

### 6. Create an AVD (Android Virtual Device)

```bash
avdmanager create avd -n iphone --device "pixel" \
  -k "system-images;android-33;google_apis;x86_64" --force
```

---

### 7. Launch the Emulator

```bash
emulator @iphone
```


