
## Running Appium Servers

### For Android
```bash
appium --config appium1.yml --base-path=/wd/hub
````

### For iOS

```bash
appium --config appium2.yml --base-path=/wd/hub
```

---

## Running Selenium Grid Nodes with Appium

### Android Node

```bash
java -jar selenium-server-4.12.0.jar node --config node1.toml
```

### iOS Node

```bash
java -jar selenium-server-4.12.0.jar node --config node2.toml
```

---

## Running Selenium Grid Hub

```bash
java -jar selenium-server-4.12.0.jar hub
```

