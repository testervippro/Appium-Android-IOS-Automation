
## **1. Extract the Tools**
### **macOS (Terminal)**:
```bash
unzip commandlinetools-mac-*.zip -d ~/android-sdk
```

### **Windows (PowerShell)**:
```powershell
Expand-Archive -Path .\commandlinetools-win-*.zip -DestinationPath "$env:USERPROFILE\android-sdk"
```

---

## **2. Set Environment Variables**
### **macOS** (Add to `~/.zshrc` or `~/.bashrc`):
```bash
export ANDROID_SDK_ROOT="$HOME/android-sdk"
export PATH="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin:$PATH"
export PATH="$ANDROID_SDK_ROOT/platform-tools:$PATH"
export PATH="$ANDROID_SDK_ROOT/emulator:$PATH"
```
**Apply changes**:
```bash
source ~/.zshrc  # or ~/.bashrc
```

### **Windows** (Run in PowerShell):
```powershell
[System.Environment]::SetEnvironmentVariable("ANDROID_SDK_ROOT", "$env:USERPROFILE\android-sdk", "User")
$env:Path += ";$env:ANDROID_SDK_ROOT\cmdline-tools\latest\bin;$env:ANDROID_SDK_ROOT\platform-tools;$env:ANDROID_SDK_ROOT\emulator"
```
**Note**: Restart PowerShell after this.

---

## **3. Install Essential Packages**
*(Same commands for both OS)*

```bash
sdkmanager --install "cmdline-tools;latest"
sdkmanager --install "platform-tools" "emulator"
sdkmanager --install "platforms;android-34" "build-tools;34.0.0"
yes | sdkmanager --licenses  # Auto-accept licenses (macOS/Linux)
sdkmanager --licenses        # Windows: Press 'y' for each
```

---

## **4. Create Android Virtual Device**
```bash
sdkmanager --install "system-images;android-34;google_apis;x86_64"
avdmanager create avd -n Pixel_6 -k "system-images;android-34;google_apis;x86_64" -d pixel_6
```

---

## **5. Start Emulator**
```bash
emulator @Pixel_6 -no-audio -no-boot-anim
```

---

## **Verify Installation**
```bash
adb devices
sdkmanager --list
```


