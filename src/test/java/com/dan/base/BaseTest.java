package com.dan.base;

import com.dan.driver.DriverFactory;
import com.dan.video.RecorderManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.Base64;

public class BaseTest {

    protected AppiumDriver driver;
    protected WebDriverWait wait;
    protected String recordedFilePath;
    protected  String nameVideo;

    @BeforeSuite
    public void startGrid() throws Exception {
        CommandLine updateIP = CommandLine.parse("node nodejs/src/update-Ip.js");
        CommandLine killPort = CommandLine.parse("node nodejs/src/kill-port.js");

        DefaultExecutor exec = new DefaultExecutor();
        exec.setStreamHandler(new PumpStreamHandler(System.out));
        exec.execute(updateIP);
        exec.execute(killPort);

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

        Thread.sleep(10000);
    }

    @BeforeTest(alwaysRun = true)
    public void startVideo() throws Exception {
        // Optional: Monte screen recorder
        RecorderManager.startVideoRecording(RecorderManager.RECORDTYPE.MONTE, "Video01");
    }

    @AfterSuite(alwaysRun = true)
    public void stopVideo() throws Exception {
        RecorderManager.stopVideoRecording(RecorderManager.RECORDTYPE.MONTE, true);
    }

    @BeforeTest(alwaysRun = true)
    @Parameters({"platform", "udid", "platformVersion"})
    public void preCondition(@Optional("Android") String platform,
                             @Optional("emulator-5554") String udid,
                             @Optional("") String platformVersion) throws IOException {

        driver = new DriverFactory().createInstance(platform, udid, platformVersion);

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

        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }


    @AfterTest(alwaysRun = true)
    public void tearDown() throws IOException, InterruptedException {
        if (driver != null && driver instanceof CanRecordScreen screenRecorder) {
            String video = screenRecorder.stopRecordingScreen();
            saveRecording(video);
            attachVideo();

        }

        if (driver != null) {
            driver.quit();
        }
    }

    private void saveRecording(String base64Video) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64Video);

            String platform = driver.getCapabilities().getPlatformName().toString().toLowerCase();
            Path videoDir = Paths.get( "videos");
            if (!Files.exists(videoDir)) {
                Files.createDirectories(videoDir);
            }

            recordedFilePath = videoDir.resolve("recording_" + platform + ".mp4").toString();
            nameVideo = videoDir.resolve("recording_" + platform).toString();
            Files.write(Paths.get(recordedFilePath), decoded);


        } catch (Exception e) {
            System.err.println("Error while saving/attaching screen recording: " + e.getMessage());
        }
    }

    void  attachVideo() throws IOException, InterruptedException {

        RecorderManager.VideoRecord.compressVideo(recordedFilePath);
        //Attach video
        Allure.addAttachment("Video", "video/mp4",
                com.google.common.io.Files.asByteSource(new File(recordedFilePath)).openStream(), "mp4");

    }
}
