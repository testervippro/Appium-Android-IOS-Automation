
# 📱 Steps to Run WebDriverAgent and Appium with Selenium Grid 3

---

## 1. Run WebDriverAgent with Appium

You can run WebDriverAgent using the Appium CLI:

```bash
appium driver run xcuitest open-wda
```

---

## 2. Set up Code Signing in Xcode

- Open the `WebDriverAgent` project in Xcode.
- Select `WebDriverAgentRunner` as the target.
- Go to **Signing & Capabilities** tab.
- Under **Team**, select your Apple developer team for code signing.

---

## 3. Build the WebDriverAgent Project

In Xcode:

- Press `Cmd + B` to build the project.

---

## 4. Start a Simulator

To boot and open a specific simulator (e.g., **iPhone 14**), run:

```bash
open -a Simulator
```

---

## 5. Find the Running Simulator (Optional)

To list running (booted) simulators:

```bash
xcrun simctl list | egrep '(Booted)'
```

---


# 🧪 Selenium Grid 3 + Appium Setup
> (Running on Grid 4 may encounter many issues, Grid 3 is more stable here.)

---
Look for your active network adapter (e.g., `Wi-Fi`, `en0`) and copy the **IPv4 address**.

Use this IP address in your node JSON config android.json, ios.json:

```json
"hubHost": "192.168.x.x"
```
> Replace `"192.168.x.x"` with your actual local IP address.

---

## 1. Start Selenium Hub

```bash
java -jar grid3/selenium.jar -role hub -hubConfig grid3/grid.json
```

---

## 2. Register Android Node

```bash
appium --nodeconfig grid3/android.json --base-path=/wd/hub --port 4723
```

---

## 3. Register iOS Node

```bash
appium --nodeconfig grid3/ios.json --base-path=/wd/hub --port 4727
```

---

# 🔍 How to Get `hubHost` (Local IP)

Run this command in your terminal to get your local IP address:

```bash
ipconfig    # Windows
ifconfig    # macOS/Linux
``

# 🚀 Run Your Tests

After setting everything up, run your tests using Maven:

```bash
mvn clean test -Dsuites=android-ios
```
## 🎥 Video Demo

Watch the video demo [here](https://www.youtube.com/watch?v=HCDSs9ilyXA).
