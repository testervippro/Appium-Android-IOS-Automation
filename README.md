

# Java and Maven Nodejs,Appium ,Appim Inpector,Android Cmd Tools (Run as Admin)

```powershell
Invoke-WebRequest https://raw.githubusercontent.com/testervippro/base_setup/main/setup.ps1 | Invoke-Expression
```

### Uninstall 
```powershell
Invoke-WebRequest https://raw.githubusercontent.com/testervippro/base_setup/main/un-setup.ps1 | Invoke-Expression
```




# Appium Configuration and Simulator Commands

##  Appium Configuration

Get capaability Android ,IOS run scipt copy , paste to appium inpector


```bash
chmod +x android_appium_config.sh && ./android_appium_config.sh
```


```bash
chmod +x ios_appium_config.sh && ./ios_appium_config.sh
```
Set Bash ENV
```bash
nano ~/.zshrc
```
Paste content in Bash.txt then Ctrl X -> Y 
```bash
# Maven Setup
export MAVEN_HOME=$(brew --prefix maven)/libexec  # Set Maven home directory
export PATH=$MAVEN_HOME/bin:$PATH  # Add Maven binaries to PATH

# Java Setup (JDK 17)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)  # Set JAVA_HOME to JDK 17
export PATH=$JAVA_HOME/bin:$PATH  # Add Java binaries to PATH

# Android SDK Setup
export ANDROID_HOME=$HOME/Library/Android/sdk  # Set Android SDK home directory
# Add Android SDK command-line tools to PATH
export PATH=$ANDROID_HOME/cmdline-tools/tools/bin:$PATH
# Add Android platform tools (e.g., adb, fastboot) to PATH
export PATH=$ANDROID_HOME/platform-tools:$PATH
# Add Android emulator tools to PATH
export PATH=$ANDROID_HOME/emulator:$PATH
# Add Android SDK tools to PATH
export PATH=$ANDROID_HOME/tools:$PATH
# Add Android SDK tools binaries to PATH
export PATH=$ANDROID_HOME/tools/bin:$PATH
# Add Android build tools (e.g., aapt) to PATH (latest version)
export PATH=$ANDROID_HOME/build-tools/$(ls -1 $ANDROID_HOME/build-tools | sort -V | tail -n 1):$PATH
```
```bash
source ~/.zshrc
```

Steps to Run WebDriverAgent and Fix Appium Inspector Errors WebDriverAgent

1. Navigate to the WebDriverAgent Directory
First, navigate to the directory where `WebDriverAgent` is located:

```bash
cd /Users/mac/.appium/node_modules/appium-xcuitest-driver/node_modules/appium-webdriveragent
```
WedDriverAgenRuner : Run app with this option
<img width="1280" alt="Screen Shot 2025-04-20 at 14 27 11" src="https://github.com/user-attachments/assets/d501d828-6f5e-4ca2-9624-987bccd92eec" />


<img width="355" alt="Screen Shot 2025-04-20 at 14 25 00" src="https://github.com/user-attachments/assets/99fff044-5671-46b8-a12c-c44c53b182e7" />



```bash
xcodebuild -project WebDriverAgent.xcodeproj -scheme WebDriverAgentRunner -destination "id=38AEB073-1BAF-4158-A7DE-43F5F1132595" -derivedDataPath /tmp/WebDriverAgentBuild clean test -allowProvisioningUpdates

```

## Simulator  IOS

### List Devices

kill port 
lsof -i :8888 | awk 'NR>1 {print $2}' | xargs kill -9
lsof -i :9999 | awk 'NR>1 {print $2}' | xargs kill -9


To list all available devices:

```bash
xcrun simctl list devices
```

### Start a Simulator

To boot a specific simulator (e.g., "iPhone 14"):

```bash
xcrun simctl boot "iPhone 13 mini"
```

### Open Simulator App (Optional)

To open the Simulator application:

```bash
open -a Simulator
```

### Stop the Simulator

To shut down a specific simulator (e.g., "iPhone 14"):

```bash
xcrun simctl shutdown "iPhone 13 mini"
```



## Simulator  Android

### 1. List Available Emulators

To list all available Android Virtual Devices (AVDs), run:

```bash
emulator -list-avds
```



### 2. Start an Emulator

To start a specific emulator, use the `emulator` command with the AVD name:

```bash
emulator -avd Pixel_4_API_30
```

Replace `Pixel_4_API_30` with the name of your AVD.

---

### 3. Interact with the Emulator

- Use the emulator window to test your app or perform actions.

---

### 4. Stop the Emulator

To stop a running emulator, use the `adb` command with the emulator ID (e.g., `emulator-5554`):

```bash
adb -s emulator-5554 emu kill
```

