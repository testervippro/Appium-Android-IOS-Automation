
# Appium Configuration and Simulator Commands

##  Appium Configuration

Get capaability Android ,IOS run scipt copy , paste to appium inpector


```bash
chmod +x android_appium_config.sh && ./android_appium_config.sh
```


```bash
chmod +x ios_appium_config.sh && ./ios_appium_config.sh
```
# Steps to Run WebDriverAgent and Fix Appium Inspector Errors WebDriverAgent

## 1. Navigate to the WebDriverAgent Directory
First, navigate to the directory where `WebDriverAgent` is located:

```bash
cd /path/to/mac@192/appium-webdriveragent

```bash
xcodebuild -project WebDriverAgent.xcodeproj -scheme WebDriverAgentRunner test -allowProvisioningUpdates
```

## Simulator  IOS

### List Devices

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

