package com.dan.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AndroidDeviceUtils {

    public static class DeviceInfo {
        private String deviceId;
        private String deviceName;
        private String platformVersion;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getPlatformVersion() {
            return platformVersion;
        }

        public void setPlatformVersion(String platformVersion) {
            this.platformVersion = platformVersion;
        }
    }

    // Get all connected Android devices or emulators
    public static List<DeviceInfo> getConnectedDevices() {
        List<DeviceInfo> devices = new ArrayList<>();
        try {
            Process process = new ProcessBuilder("adb", "devices", "-l").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.contains("device") && !line.startsWith("List")) {
                    String[] parts = line.split("\\s+");
                    String deviceId = parts[0];
                    String model = "";
                    for (String part : parts) {
                        if (part.startsWith("model:")) {
                            model = part.replace("model:", "");
                            break;
                        }
                    }

                    // Get Android version
                    Process versionProc = new ProcessBuilder("adb", "-s", deviceId, "shell", "getprop", "ro.build.version.release").start();
                    BufferedReader versionReader = new BufferedReader(new InputStreamReader(versionProc.getInputStream()));
                    String version = versionReader.readLine();

                    DeviceInfo info = new DeviceInfo();
                    info.setDeviceId(deviceId);
                    info.setDeviceName(model);
                    info.setPlatformVersion(version);
                    devices.add(info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return devices;
    }

    // Get device info by name
    public static DeviceInfo getDeviceInfoByName(String deviceName) {
        List<DeviceInfo> devices = getConnectedDevices();
        for (DeviceInfo device : devices) {
            if (device.getDeviceName().equalsIgnoreCase(deviceName)) {
                return device;
            }
        }
        return null;
    }

    // List all available device names
    public static String[] getAvailableDeviceNames() {
        List<DeviceInfo> devices = getConnectedDevices();
        return devices.stream().map(DeviceInfo::getDeviceName).toArray(String[]::new);
    }
}

