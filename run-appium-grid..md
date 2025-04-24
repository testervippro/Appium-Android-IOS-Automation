
# Start Appium & Selenium Grid (Android + iOS)

Ensure all required ports are free and each component runs in its **own terminal** for smooth operation and debugging.

---

## macOS / Linux

### Kill Ports Before Starting

```bash
#!/bin/bash
echo "Killing processes on ports 4723, 4733, 4444, 5555, 5556..."
for port in 4723 4733 4444 5555 5556
do
  lsof -ti:$port | xargs kill -9 2>/dev/null
done
echo "All specified ports are now free."
```

### Launch Each Component (Run each in a separate terminal)

```bash
appium server --config grid/appium-android.yml
```

```bash
appium server --config grid/appium-ios.yml
```

```bash
java -jar grid/selenium-server-4.12.0.jar hub
```

```bash
java -jar grid/selenium-server-4.12.0.jar node --config grid/appium-node-android.toml
```

```bash
java -jar grid/selenium-server-4.12.0.jar node --config grid/appium-node-ios.toml
```

---

## Windows (CMD)

### Kill Ports Before Starting

Create a file called `kill-ports.cmd` with this content and run as Administrator:

```cmd
@echo off
echo Killing processes on ports 4723, 4733, 4444, 5555, 5556...

for %%p in (4723 4733 4444 5555 5556) do (
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%%p') do (
        echo Killing PID %%a on port %%p
        taskkill /PID %%a /F >nul 2>&1
    )
)

echo All specified ports are now free.
```

### Launch Each Component (Use separate CMD windows)

```cmd
appium server --config grid\appium-android.yml
```

```cmd
appium server --config grid\appium-ios.yml
```

```cmd
java -jar grid\selenium-server-4.12.0.jar hub
```

```cmd
java -jar grid\selenium-server-4.12.0.jar node --config grid\appium-node-android.toml
```

```cmd
java -jar grid\selenium-server-4.12.0.jar node --config grid\appium-node-ios.toml
```
