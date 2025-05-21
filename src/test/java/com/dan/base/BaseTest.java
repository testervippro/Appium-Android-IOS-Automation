package com.dan.base;

import com.dan.driver.DriverFactory;
import com.dan.video.RecorderManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Allure;
import java.io.ByteArrayOutputStream;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected String recordedFilePath;
    protected String nameVideo;

    @AfterSuite(alwaysRun = true)
    public void cleanup() throws Exception {
        RecorderManager.stopVideoRecording(RecorderManager.RECORDTYPE.MONTE, true);

    }

    @BeforeSuite
    public void startGrid() throws Exception {

        // install package once
       // new DefaultExecutor().execute(CommandLine.parse("npm --prefix ./nodejs install"));

        // Kill port and auto update ip
        CommandLine updateIP = CommandLine.parse("node nodejs/src/update-Ip.js");
        CommandLine killPort = CommandLine.parse("node nodejs/src/kill-port.js");

        DefaultExecutor exec = new DefaultExecutor();
        DefaultExecutor execKillPort = new DefaultExecutor();
        exec.setStreamHandler(new PumpStreamHandler(System.out));
        exec.execute(updateIP);
        execKillPort.execute(killPort);

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

        TimeUnit.SECONDS.sleep(10);
    }


    @BeforeTest(alwaysRun = true)
    public void startVideo() throws Exception {
        RecorderManager.startVideoRecording(RecorderManager.RECORDTYPE.MONTE, "Video01");
    }


    @BeforeTest(alwaysRun = true)
    @Parameters({"platform", "udid", "platformVersion"})
    public void preCondition(@Optional("Android") String platform,
                             @Optional("emulator-5554") String udid,
                             @Optional("") String platformVersion) throws IOException {
        driver = new DriverFactory().createInstance(platform, udid, platformVersion);

        if (driver instanceof CanRecordScreen screenRecorder) {
            Duration timeLimit = Duration.ofMinutes(5);
            if (platform.equalsIgnoreCase("android")) {
                screenRecorder.startRecordingScreen(
                        new AndroidStartScreenRecordingOptions().withTimeLimit(timeLimit)
                );
            } else if (platform.equalsIgnoreCase("ios")) {
                screenRecorder.startRecordingScreen(
                        new IOSStartScreenRecordingOptions().withTimeLimit(timeLimit)
                );
            }
        }

        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() throws IOException, InterruptedException {
        if (driver != null) {
            if (driver instanceof CanRecordScreen screenRecorder) {
                String video = screenRecorder.stopRecordingScreen();
                saveRecording(video);
                attachVideo();
            }
            driver.quit();
        }
    }

    private void saveRecording(String base64Video) throws IOException {
        byte[] decoded = Base64.getDecoder().decode(base64Video);
        String platform = driver.getCapabilities().getPlatformName().toString().toLowerCase();
        Path videoDir = Paths.get("videos");

        if (!Files.exists(videoDir)) {
            Files.createDirectories(videoDir);
        }

        recordedFilePath = videoDir.resolve("recording_" + platform + ".mp4").toString();
        nameVideo = "recording_" + platform;
        Files.write(Paths.get(recordedFilePath), decoded);
    }

    private void attachVideo() throws IOException, InterruptedException {
        RecorderManager.VideoRecord.compressVideo(recordedFilePath);
        Allure.addAttachment("Video", "video/mp4",
                Files.newInputStream(Paths.get(recordedFilePath)), "mp4");
    }
}
