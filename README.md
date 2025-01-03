
# Appium Configuration and Simulator Commands

##  Appium Configuration

Get capaability Android ,IOS run scipt copy , paste to appium inpector


```bash
chmod +x android_appium_config.sh
./android_appium_config.sh
```


```bash
chmod +x ios_appium_config.sh
./ios_appium_config.sh
```

## Simulator Management

### List Devices

To list all available devices:

```bash
xcrun simctl list devices
```

### Start a Simulator

To boot a specific simulator (e.g., "iPhone 14"):

```bash
xcrun simctl boot "iPhone 14"
```

### Open Simulator App (Optional)

To open the Simulator application:

```bash
open -a Simulator
```

### Stop the Simulator

To shut down a specific simulator (e.g., "iPhone 14"):

```bash
xcrun simctl shutdown "iPhone 14"
```
```
