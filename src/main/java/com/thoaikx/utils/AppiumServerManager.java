package com.thoaikx.utils;

import io.appium.java_client.AppiumDriver;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

import static com.thoaikx.config.ConfigurationManager.configuration;

public class AppiumServerManager {

    private static AppiumDriverLocalService service;

    public static void main(String[] args) throws IOException, InterruptedException {
  //   startServer();
    // stopServer();
        //startAppiumServer(9999);
    }

    public static void startServer() {
        // Set the path to Node.js and Appium
        String nodePath = "/usr/local/bin/node"; // Replace with your Node.js path
        String appiumPath = "/usr/local/bin/appium"; // Replace with your Appium path

        // Configure the Appium server
        AppiumServiceBuilder builder = new AppiumServiceBuilder()
                .withIPAddress("127.0.0.1") // IP address of the server
                .usingPort(1111) // Port to run the server
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


    public static void killAppiumServer(int port) throws IOException, InterruptedException {

        ProcessBuilder killProcessBuilder = new ProcessBuilder("/bin/bash", "-c", "pkill -f 'appium --port " + port + "'");
        killProcessBuilder.redirectErrorStream(true); // Redirect error stream to standard output

        Process appiumProcess = killProcessBuilder.start();

        // Use CompletableFuture to handle the output asynchronously
        CompletableFuture.runAsync(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(appiumProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // Print each line of the process output
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Appium started on port " + port + ".");
    }


    // Modernized method to start Appium server on a specific port
    public static void startAppiumServer(int port) throws IOException, InterruptedException {
        // Kill any existing Appium server on the specified port
        killAppiumServer(port);

        // Start a new Appium server on the specified port
        ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", "appium --port " + port);
        processBuilder.redirectErrorStream(true); // Redirect error stream to standard output

        Process appiumProcess = processBuilder.start();

        // Use CompletableFuture to handle the output asynchronously
        CompletableFuture.runAsync(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(appiumProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line); // Print each line of the process output
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        System.out.println("Appium started on port " + port + ".");
    }

}