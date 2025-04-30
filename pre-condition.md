
# Appium Java Setup Guide (macOS & Windows)
For Mobile Automation (Android & iOS)

## Pre-Requisites

### macOS Setup
```bash
# 1. Install JDK 17 (Homebrew)
brew install openjdk@17

# 2. Set JAVA_HOME (temporary session)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# 3. Make permanent (add to ~/.zshrc or ~/.bashrc)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc

source ~/.zshrc

# 4. Verify
java -version 
echo $JAVA_HOME  
```

### Windows Setup
```powershell
# 1. Download & install Temurin JDK 17 (silent install)
Invoke-WebRequest -Uri "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.8%2B7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.8_7.msi" -OutFile jdk17.msi
Start-Process msiexec.exe -ArgumentList '/i jdk17.msi /quiet' -Wait

# 2. Set JAVA_HOME (for current session)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.8.7-hotspot"
$env:Path += ";$env:JAVA_HOME\bin"

# 3. Make permanent
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', $env:JAVA_HOME, 'User')
[System.Environment]::SetEnvironmentVariable('Path', "$([System.Environment]::GetEnvironmentVariable('Path','User'));$env:JAVA_HOME\bin", 'User')

# 4. Verify (in NEW terminal)
java -version
$env:JAVA_HOME
```

### 2. IDE with Maven
- IntelliJ IDEA (Recommended):  
  Download: https://www.jetbrains.com/idea/download

### 3. Node.js & Appium

macOS (Bash or Zsh)
```bash
brew install node@20
echo 'export PATH="/opt/homebrew/opt/node@20/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
node -v    
npm -v    
```

Windows (PowerShell Admin)
```powershell
Invoke-WebRequest -Uri "https://nodejs.org/dist/v20.11.1/node-v20.11.1-x64.msi" -OutFile "nodejs.msi"
Start-Process msiexec.exe -ArgumentList "/i nodejs.msi /quiet" -Wait
node -v
npm -v
```

Install Appium:
```bash
npm install -g appium
npm install -g appium-xcuitest-driver
appium -v
```

---

## Android Setup (macOS & Windows)
### 1. Android Studio
Download: https://developer.android.com/studio

### 2. Environment Variables
macOS/Linux (add to ~/.zshrc)
```bash
export ANDROID_HOME="$HOME/Library/Android/sdk"
export PATH="$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin:$PATH"
```

Windows (PowerShell)
```powershell
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
$env:Path += ";$env:ANDROID_HOME\platform-tools;$env:ANDROID_HOME\cmdline-tools\latest\bin"
```

Android Device Setup via Android Studio
1. Create Virtual Device
   Open Android Studio → Device Manager (from toolbar or Tools menu)

Click Create Device

Select hardware profile (e.g., Pixel 6)

Choose system image (recommend Android 14/Q API 34 with Google Play)

Click Next → Finish

2. Launch Emulator
   From Device Manager:
   Click ▶️ button next to your AVD


Verify:
```bash
adb devices
```

### 3. UI Inspector Tools
- Appium Inspector:  
  Download: https://github.com/appium/appium-inspector/releases
- UIAutomatorViewer:  
  Located in $ANDROID_HOME/tools/bin/uiautomatorviewer

---

## iOS Setup (macOS Only)
### 1. Xcode
Download: https://xcodereleases.com/

### 2. Command Line Tools
```bash
xcode-select --install
```

### 3. Simulator
1. Open Xcode > Xcode Menu > Open Developer Tool > Simulator
2. Create a device (e.g., iPhone 14, iOS 16+)

---

## Running Tests with Maven Wrapper
### 1. First Build
```bash
./mvnw clean install -DskipTests              # macOS/Linux
mvnw.cmd clean install -DskipTests                # Windows
```

### 2. Execute Test Suites
Android:
```bash
./mvnw test -Dsuites=android          # macOS/Linux
mvnw.cmd test -Dsuites=android        # Windows
```

iOS:
```bash
./mvnw test -Dsuites=ios              # macOS only
```