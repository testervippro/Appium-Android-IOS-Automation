package com.thoaikx.utils;

import io.appium.java_client.AppiumDriver;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;

public class AppiumServerManager {

    private static AppiumDriverLocalService service;

    public static void main(String[] args) {
     startServer();   
     stopServer();
    }

    public static void startServer() {
        // Set the path to Node.js and Appium
        String nodePath = "/usr/local/bin/node"; // Replace with your Node.js path
        String appiumPath = "/usr/local/bin/appium"; // Replace with your Appium path

        // Configure the Appium server
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1") // IP address of the server
                .usingPort(8888) // Port to run the server
                .withArgument(GeneralServerFlag.BASEPATH, "/wd/hub") // Base path for the server
                .usingDriverExecutable(new File(nodePath)) // Path to Node.js executable
                .withAppiumJS(new File(appiumPath)) // Path to Appium executable
                .withLogFile(new File("appium.log")); // Log file for Appium server output

        // Start the Appium server
        service = AppiumDriverLocalService.buildService(builder);
        service.start();

        System.out.println("Appium server started on: " + service.getUrl());
    }

    public static void stopServer() {
        if (service != null && service.isRunning()) {
            service.stop();
            System.out.println("Appium server stopped.");
        }
    }
}