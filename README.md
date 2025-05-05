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

### Windows Setup Before Running: Make Sure to Remove Old Java Version ( Run by admin)
You can check the location of your existing Java version by running the `where` command in Command Prompt.

```powershell
# 1. Download & Install Temurin JDK 17
Invoke-WebRequest -Uri "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.8%2B7/OpenJDK17U-jdk_x64_windows_hotspot_17.0.8_7.msi" -OutFile jdk17.msi
Start-Process msiexec.exe -ArgumentList '/i jdk17.msi /quiet' -Wait

# 2. Set JAVA_HOME for Current Session (TEMPORARY)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.8.7-hotspot"
$env:Path += ";$env:JAVA_HOME\bin"

# 3. Make Environment Variables Permanent (PERSISTENT)
[System.Environment]::SetEnvironmentVariable('JAVA_HOME', $env:JAVA_HOME, 'Machine')
[System.Environment]::SetEnvironmentVariable('Path', "$([System.Environment]::GetEnvironmentVariable('Path','Machine'));$env:JAVA_HOME\bin", 'Machine')

# 4. Verify That the Correct JAVA_HOME is Set
$javaHome = [System.Environment]::GetEnvironmentVariable('JAVA_HOME', 'Machine')
$path = [System.Environment]::GetEnvironmentVariable('Path', 'Machine')
Write-Host "JAVA_HOME is set to: $javaHome"
Write-Host "Path contains: $path"

# 5. Verify Java Version
java -version
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

### Start Grid via Java (Manual)
Refer in folder
grid3/run-grid3.md


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


## Run Your Tests

```bash
mvn clean test -Dsuites=android-ios
```

---

## Demo Video

Watch the full guide:
[https://www.youtube.com/watch?v=HCDSs9ilyXA](https://www.youtube.com/watch?v=HCDSs9ilyXA)
