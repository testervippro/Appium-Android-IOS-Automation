
### 🧪 Selenium Grid 3 + Appium Setup (Run Grid 4 meet many issue)

#### 1. Start Selenium Hub
```bash
java -jar grid3/selenium.jar -role hub -hubConfig grid3/grid.json
```

#### 2. Register Android Node
```bash
appium --nodeconfig grid3/android.json --base-path=/wd/hub --port 4723
```

#### 3. Register iOS Node
```bash
appium --nodeconfig grid3/ios.json --base-path=/wd/hub --port 4727
```

---

### 🔍 How to get `hubHost` (local IP)

Run this command in your terminal to get the local IP address:
```bash
ipconfig    # Windows
ifconfig    # macOS/Linux
```

Look for your local network adapter (like `Wi-Fi` or `en0`) and copy the `IPv4 address`.

Use this IP in your node JSON config:
```json
"hubHost": "192.168.x.x"
```

> Replace `"192.168.x.x"` with your actual IP.
