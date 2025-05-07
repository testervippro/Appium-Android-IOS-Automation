# Appium Java Setup Guide (macOS & Windows)

For Mobile Automation (Android & iOS)

---

## Pre-Requisites

### macOS Setup

```bash
# 1. Install JDK 17
brew install openjdk@17

# 2. Set JAVA_HOME (temporary session)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# 3. Make permanent (add to ~/.zshrc or ~/.bashrc)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# 4. Verify installation
java -version
echo $JAVA_HOME
```

---

Here's your formatted Markdown (`.md`) version of the instructions and PowerShell script:

---

# ✅ Windows Setup Before Running Java Applications

> ⚠️ **Make sure to remove any old Java versions first (Run as Administrator).**

## 🔍 Check Existing Java Installation

You can check the location of your current Java installation by running the following command in **Command Prompt**:

```cmd
where java
```

## ⬇️ Download Java JDK 17

Download the installer from the official Oracle archive:

👉 [Download JDK 17.0.12 for Windows x64 (.msi)](https://download.oracle.com/java/17/archive/jdk-17.0.12_windows-x64_bin.msi)

## ⚙️ PowerShell Script to Set `JAVA_HOME` and Update `PATH`

> Run the following PowerShell script **as a regular user** (not administrator) to update environment variables for the current user.

```powershell
# Set JAVA_HOME for current user
[Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Program Files\Java\jdk-17", "User")

# Get current PATH and append JDK bin if not already there
$currentPath = [Environment]::GetEnvironmentVariable("Path", "User")
$javaBin = "C:\Program Files\Java\jdk-17\bin"

if ($currentPath -notlike "*$javaBin*") {
    $newPath = "$currentPath;$javaBin"
    [Environment]::SetEnvironmentVariable("Path", $newPath, "User")
    Write-Host "JAVA_HOME and PATH updated."
} else {
    Write-Host "JAVA_HOME already set and bin already in PATH."
}
```


## Recommended IDE

* IntelliJ IDEA
  Download: [https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download)

---

## Node.js & Appium Setup

### macOS

```bash
brew install node@20
echo 'export PATH="/opt/homebrew/opt/node@20/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
node -v
npm -v
```

### Windows

```powershell
Invoke-WebRequest -Uri "https://nodejs.org/dist/v20.11.1/node-v20.11.1-x64.msi" -OutFile "nodejs.msi"
Start-Process msiexec.exe -ArgumentList "/i nodejs.msi /quiet" -Wait
node -v
npm -v
```

### Install Appium

```bash
npm install -g appium
npm install -g appium-doctor
appium -v
```

---

## Android Setup

### Install Android Studio

Download: [https://developer.android.com/studio](https://developer.android.com/studio)

---

### Set Environment Variables

#### Android SDK Paths:

* macOS: `/Users/<your-username>/Library/Android/sdk`
* Windows: `%LOCALAPPDATA%\Android\Sdk`

#### Auto-Setup via Node Script:

```bash
node nodejs/src/setup.js
```

This script:

* Detects OS
* Sets `ANDROID_HOME`
* Adds `platform-tools` to `PATH`

#### Manual Setup (macOS/Linux):

```bash
export ANDROID_HOME=~/Library/Android/sdk
export PATH=$ANDROID_HOME/platform-tools:$PATH
```

#### Manual Setup (Windows):

```powershell
setx ANDROID_HOME "%LOCALAPPDATA%\Android\Sdk"
setx PATH "%PATH%;%ANDROID_HOME%\platform-tools"
```

---

### Create Android Virtual Device (AVD)

1. Open **Android Studio > Device Manager**
2. Create a new device (e.g., Pixel 6)
3. Download system image (e.g., Android 14)
4. Click **Finish**
5. Start emulator using play icon

#### Verify:

```bash
adb devices
```

---

### UI Inspector Tools

* Appium Inspector: [https://github.com/appium/appium-inspector/releases](https://github.com/appium/appium-inspector/releases)
* UIAutomatorViewer: `$ANDROID_HOME/tools/bin/uiautomatorviewer`

---

## iOS Setup (macOS Only)

### Install Xcode

Download: [https://xcodereleases.com/](https://xcodereleases.com/)

### Command Line Tools

```bash
xcode-select --install
```

### Install Appium iOS Driver

```bash
npm install -g appium-xcuitest-driver
```

### Launch Simulator

1. Xcode → Open Developer Tool → Simulator
2. Choose/Create iOS device (e.g., iPhone 14)

#### Check Booted Devices:

```bash
xcrun simctl list | egrep '(Booted)'
```

---

## Selenium Grid 3 + Appium

### Update Local IP in JSON (Auto)

```bash
node nodejs/src/setup.js
```

This updates `hubHost` in `android.json` / `ios.json`.

#### Or manually edit:

```json
{
  "hubHost": "192.168.x.x"
}
```

---

### Start Grid via Java

```bash
java -jar grid3/selenium.jar -role hub -hubConfig grid3/grid.json
```

---

### Register Android Node

```bash
appium --nodeconfig grid3/android.json --base-path=/wd/hub --port 4723
```

### Register iOS Node

```bash
appium --nodeconfig grid3/ios.json --base-path=/wd/hub --port 4727
```

---

### Auto Start Grid from Java (TestNG)

Ensure npm install is run inside the nodejs folder:
Move to folder

```bash
cd nodejs
```

Then run cmd

```bash
npm install
```



## Add `BaseTest` to Start Grid and Update Configuration

This update to `BaseTest` will perform the following:

* Update `configuration.hubHost` in the following JSON files inside the `grid3` folder:

  * `android.json`
  * `ios.json`
* Start Grid with the following ports:

  * **Grid Hub**: `4444`
  * **Appium Android Node**: `4723`
  * **Appium iOS Node**: `4727`
  * **Express Server**: `9999` (for real-time logs)

###  Example: BaseTest.java Snippet

```java
@BeforeSuite
public void startGrid() throws IOException {
    // Logic to update configuration.hubHost in android.json and ios.json
    // Kill All port 4444,9999,4723,4727
    // Start Grid on port 4444
    // Start Appium Android node on port 4723
    // Start Appium iOS node on port 4727
    // Start Express server on port 9999 for real-time logs
}
```

### 📷 Screenshot

<img width="1280" alt="Screen Shot 2025-05-03 at 13 22 07" src="https://github.com/user-attachments/assets/9ea099a0-6063-4d7a-8de9-9ad26ecf8227" />




## Run Your Tests

### Windows

```bash
./mvnw.cmd clean test -Dsuites=android
````

### macOS / Linux

```bash
./mvnw clean test -Dsuites=android-ios
```

```bash
./mvnw clean test -Dsuites=android
```



---

## Demo Video

Watch the full guide:
[https://www.youtube.com/watch?v=HCDSs9ilyXA](https://www.youtube.com/watch?v=HCDSs9ilyXA)
