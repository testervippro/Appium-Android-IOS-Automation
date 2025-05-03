
### **1. Run Docker Compose**
Navigate to the directory where your `docker-compose-android-real-devices.yml` file is located and run the following command:

```bash
docker-compose -f docker-compose-android-real-devices.yml up --build
```

This will build and start the container. The `--build` flag ensures that Docker will rebuild the image if there are any changes.

---

### **2. Verify that adb detects your Android device**

Once the container is up and running, you can verify if your Android device is detected by `adb`.

#### **For Linux or macOS:**

Run the following command to verify if your Android device is detected:

```bash
docker exec -it appium-container adb devices
```

This command will execute the `adb devices` command inside the running Appium container, which should list any connected Android devices.

#### **For Windows:**

On Windows, Docker containers do not automatically have access to the host’s USB devices. You need to specify the host using `host.docker.internal`. Run the following command:

```bash
docker exec -it appium-container adb -H host.docker.internal devices
```

This will instruct the container to use `host.docker.internal` as the host for `adb` to communicate with your connected Android device.

---

This setup should allow you to verify if your Android device is detected inside the container for testing with Appium.