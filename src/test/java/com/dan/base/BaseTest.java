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
    private Process emulatorProcess;

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

        TimeUnit.SECONDS.sleep(10);
    }

    @BeforeSuite(dependsOnMethods = "startGrid")
    public void launchDevice() throws IOException, InterruptedException {
        // Check if emulator is already running
        if (isEmulatorRunning()) {
            System.out.println("Emulator is already running");
            return;
        }

        // Launch emulator in background
        String launchEmulator = "emulator -avd Iphone -no-snapshot-save -no-boot-anim -no-audio";
        new Thread(() -> {
            try {
                new DefaultExecutor().execute(CommandLine.parse(launchEmulator));
            } catch (IOException e) {
                throw new RuntimeException("Failed to start emulator", e);
            }
        }).start();

        // Wait for emulator to be fully ready
        waitForEmulatorReady();
    }

    private boolean isEmulatorRunning() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        CommandLine cmd = CommandLine.parse("adb devices");
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(new PumpStreamHandler(outputStream));

        executor.execute(cmd);

        String output = outputStream.toString();
        return output.contains("emulator-5554") && output.contains("device");
    }

    private void waitForEmulatorReady() throws InterruptedException, IOException {
        int maxAttempts = 10;
        int attempt = 0;

        System.out.println("Waiting for emulator to be ready...");

        while (attempt < maxAttempts) {
            if (isEmulatorRunning() && isBootCompleted()) {
                System.out.println("Emulator is ready!");
                return;
            }

            Thread.sleep(10_000);
            attempt++;
            System.out.println("Waiting for emulator... Attempt " + attempt);
        }

        throw new RuntimeException("Emulator failed to start within timeout");
    }

    private boolean isBootCompleted() throws IOException, InterruptedException {
        Process process = new ProcessBuilder("adb", "-s", "emulator-5554", "shell", "getprop", "sys.boot_completed").start();
        process.waitFor();
        String output = new String(process.getInputStream().readAllBytes()).trim();
        return "1".equals(output);
    }

    @BeforeTest(alwaysRun = true)
    public void startVideo() throws Exception {
        RecorderManager.startVideoRecording(RecorderManager.RECORDTYPE.MONTE, "Video01");
    }

    @AfterSuite(alwaysRun = true)
    public void cleanup() throws Exception {
        if (emulatorProcess != null && emulatorProcess.isAlive()) {
            emulatorProcess.destroy();
        }
        RecorderManager.stopVideoRecording(RecorderManager.RECORDTYPE.MONTE, true);
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
