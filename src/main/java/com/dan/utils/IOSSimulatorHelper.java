package com.dan.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;

public class IOSSimulatorHelper {

    // Nested class to hold device information
    public static class DeviceInfo {
        private String udid;
        private String platformVersion;
        private String deviceName;

        // Getter and Setter methods for UDID, Platform Version, and Device Name
        public String getUdid() {
            return udid;
        }

        public void setUdid(String udid) {
            this.udid = udid;
        }

        public String getPlatformVersion() {
            return platformVersion;
        }

        public void setPlatformVersion(String platformVersion) {
            this.platformVersion = platformVersion;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }
    }

    // Method to find device by its name and return its information
    public static DeviceInfo getDeviceInfoByName(String deviceName) {
        DeviceInfo info = new DeviceInfo();
        try {
            // Running the xcrun command to list all devices
            Process process = new ProcessBuilder("xcrun", "simctl", "list", "-j", "devices").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(reader);
            JsonNode devicesNode = root.path("devices");

            // Iterating through iOS versions
            Iterator<String> versions = devicesNode.fieldNames();
            while (versions.hasNext()) {
                String version = versions.next(); // e.g., "iOS 15.0"
                JsonNode deviceList = devicesNode.path(version);
                for (JsonNode device : deviceList) {
                    // Check for the device by name
                    if (device.path("name").asText().equalsIgnoreCase(deviceName) &&
                            device.path("isAvailable").asBoolean()) {
                        info.setUdid(device.path("udid").asText());
                        info.setPlatformVersion(version.replace("iOS ", ""));
                        info.setDeviceName(device.path("name").asText());
                        return info;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Method to get a list of all available device names
    public static String[] getAvailableDeviceNames() {
        try {
            // Running the xcrun command to list all devices
            Process process = new ProcessBuilder("xcrun", "simctl", "list", "-j", "devices").start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(reader);
            JsonNode devicesNode = root.path("devices");

            // Create a list to hold available device names
            Iterator<String> versions = devicesNode.fieldNames();
            while (versions.hasNext()) {
                String version = versions.next();
                JsonNode deviceList = devicesNode.path(version);
                String[] deviceNames = new String[deviceList.size()];
                for (int i = 0; i < deviceList.size(); i++) {
                    deviceNames[i] = deviceList.get(i).path("name").asText();
                }
                return deviceNames;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new String[0];
    }
}

