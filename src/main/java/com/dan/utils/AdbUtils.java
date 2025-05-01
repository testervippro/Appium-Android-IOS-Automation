package com.dan.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AndroidDeviceChecker {

    public static void main(String[] args) {
        String targetDevice = "emulator-5554";  // Replace with your target device

        // Step 1: Check connected devices
        List<String> devices = getConnectedDevices();

        if (devices == null || !devices.contains(targetDevice)) {
            System.out.println("Device " + targetDevice + " not found.");
            return;
        }

        // Step 2: Get Android version for that device
        String version = getAndroidVersion(targetDevice);
        if (version != null) {
            System.out.println("Device " + targetDevice + " is running Android version: " + version);
        } else {
            System.out.println("Error fetching Android version for device " + targetDevice);
        }
    }

    // Get a list of connected devices
    public static List<String> getConnectedDevices() {
        List<String> devices = new ArrayList<>();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("adb", "devices");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().endsWith("device")) {
                    devices.add(line.split("\t")[0].trim());
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error executing adb devices command.");
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
        return devices;
    }

    // Get Android version of the device
    public static String getAndroidVersion(String device) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("adb", "-s", device, "shell", "getprop", "ro.build.version.release");
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String version = reader.readLine().trim();

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Error executing adb shell getprop command.");
                return null;
            }

            return version;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}

