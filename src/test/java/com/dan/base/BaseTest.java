package com.dan.base;

import com.dan.driver.DriverFactory;
import com.dan.video.RecorderManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Step;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;

public class BaseTest {

    protected AppiumDriver driver;
    protected WebDriverWait wait;

    @BeforeSuite
    public void startGrid() throws Exception {

        // Start Grid and kill previous port
        CommandLine updateIP = CommandLine.parse("node nodejs/src/update-Ip.js");
        CommandLine killPort = CommandLine.parse("node nodejs/src/kill-port.js");

        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(new PumpStreamHandler(System.out));
        exec.execute(updateIP);
        exec.execute(killPort);

        // Start grid in a separate thread
        Thread gridThread = new Thread(() -> {
            try {
                CommandLine runGrid = CommandLine.parse("node nodejs/src/run-grid.js");
                new DefaultExecutor().execute(runGrid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        gridThread.setDaemon(true);
        gridThread.start();

        // Wait for grid to initialize
        Thread.sleep(10000);
    }


    @BeforeTest(alwaysRun = true)
    public void startVideo() throws Exception {
        // Stop video recording after tests
        RecorderManager.startVideoRecording(RecorderManager.RECORDTYPE.MONTE,"Video01" );
    }
    @AfterSuite(alwaysRun = true)
    public void stopVideo() throws Exception {
        // Stop video recording after tests
        RecorderManager.stopVideoRecording(RecorderManager.RECORDTYPE.MONTE, true);
    }

    @BeforeTest(alwaysRun = true)
    @Parameters({"platform", "udid", "platformVersion"})
    public void preCondition(@Optional("Android") String platform,
                             @Optional("emulator-5554") String udid,
                             @Optional("") String platformVersion) throws IOException {

        // Create driver instance based on platform
        driver = new DriverFactory().createInstance(platform, udid, platformVersion);

        // Start screen recording for supported platforms
        if (driver instanceof CanRecordScreen screenRecorder) {
            if (platform.equalsIgnoreCase("android")) {
                screenRecorder.startRecordingScreen(
                        new AndroidStartScreenRecordingOptions().withTimeLimit(Duration.ofMinutes(5))
                );
            } else if (platform.equalsIgnoreCase("ios")) {
                screenRecorder.startRecordingScreen(
                        new IOSStartScreenRecordingOptions().withTimeLimit(Duration.ofMinutes(5))
                );
            }
        }

        // Wait for elements
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() throws IOException {
        // Stop screen recording and save the video file
        if (driver != null && driver instanceof CanRecordScreen screenRecorder) {
            String video = screenRecorder.stopRecordingScreen();
            saveRecording(video);
        }

        // Quit driver
        if (driver != null) {
            driver.quit();
        }
    }

    private void saveRecording(String video) {
        try {
            // Decode the video from Base64 and write it to a file
            byte[] decoded = Base64.getDecoder().decode(video);

            // Ensure the video folder exists
            Path videoDir = Paths.get("videos");
            if (!Files.exists(videoDir)) {
                Files.createDirectories(videoDir);
            }

            // Save video file with platform-specific name
            String platform = ((RemoteWebDriver) driver).getCapabilities().getPlatformName().toString();
            Files.write(videoDir.resolve("recording_" + platform + ".mp4"), decoded);

        } catch (IllegalArgumentException e) {
            System.err.println("Failed to decode video: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving the video file: " + e.getMessage());
        }
    }
}
