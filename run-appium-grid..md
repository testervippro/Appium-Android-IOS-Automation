
#  Start Appium & Selenium Grid (Android + iOS)

##  macOS / Linux

```bash
#!/bin/bash

appium server --config grid/appium-android.yml &
appium server --config grid/appium-ios.yml &

java -jar grid/selenium-server-4.12.0.jar hub &

java -jar grid/selenium-server-4.12.0.jar node --config grid/appium-node-android.toml &
java -jar grid/selenium-server-4.12.0.jar node --config grid/appium-node-ios.toml &
```

## Windows (CMD)

```cmd
start /B appium server --config grid\appium-android.yml
start /B appium server --config grid\appium-ios.yml
start /B java -jar grid\selenium-server-4.12.0.jar hub
start /B java -jar grid\selenium-server-4.12.0.jar node --config grid\appium-node-android.toml
start /B java -jar grid\selenium-server-4.12.0.jar node --config grid\appium-node-ios.toml
```

---
