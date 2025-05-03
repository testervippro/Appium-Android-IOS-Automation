
## Running Appium Servers

### For Android
```bash
appium --config appium-android.yml
````

### For iOS

```bash
appium --config appium-ios.yml
```

---

## Running Selenium Grid Nodes with Appium

### Android Node

```bash
java -jar selenium-server-4.12.0.jar node --config appium-node-android.toml
```

### iOS Node

```bash
java -jar selenium-server-4.12.0.jar node --config appium-node-ios.toml
```

---

## Running Selenium Grid Hub

```bash
java -jar selenium-server-4.12.0.jar hub
```

