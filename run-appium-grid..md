
# 🚀 Start Appium & Selenium Grid (Android + iOS)
---

## 🖥 macOS / Linux

```bash
# Terminal 1 - Start Android Appium Server
appium server --config grid/appium-android.yml
```

```bash
# Terminal 2 - Start iOS Appium Server
appium server --config grid/appium-ios.yml
```

```bash
# Terminal 3 - Start Selenium Grid Hub
java -jar grid/selenium-server-4.12.0.jar hub
```

```bash
# Terminal 4 - Start Android Node
java -jar grid/selenium-server-4.12.0.jar node --config grid/appium-node-android.toml
```

```bash
# Terminal 5 - Start iOS Node
java -jar grid/selenium-server-4.12.0.jar node --config grid/appium-node-ios.toml
```

---

## 🪟 Windows (CMD)

> ⚠ Use **separate Command Prompt windows** or tabs for each.

```cmd
:: CMD Window 1 - Start Android Appium Server
appium server --config grid\appium-android.yml
```

```cmd
:: CMD Window 2 - Start iOS Appium Server
appium server --config grid\appium-ios.yml
```

```cmd
:: CMD Window 3 - Start Selenium Grid Hub
java -jar grid\selenium-server-4.12.0.jar hub
```

```cmd
:: CMD Window 4 - Start Android Node
java -jar grid\selenium-server-4.12.0.jar node --config grid\appium-node-android.toml
```

```cmd
:: CMD Window 5 - Start iOS Node
java -jar grid\selenium-server-4.12.0.jar node --config grid\appium-node-ios.toml
```

---
