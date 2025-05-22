package com.dan.base;

import com.dan.driver.DriverFactory;
import com.dan.driver.DriverManager;
import com.dan.video.RecorderManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidStartScreenRecordingOptions;
import io.appium.java_client.ios.IOSStartScreenRecordingOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.qameta.allure.Allure;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.time.Duration;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static com.dan.driver.DriverManager.getDriver;
import static com.sun.jna.Platform.isMac;
import static com.sun.jna.Platform.isWindows;

public class BaseTest {

    private AppiumDriver driver;
    protected WebDriverWait wait;
    protected String recordedFilePath;
    protected String nameVideo;
    protected static boolean isRetry = false; // Static flag to track retries

    // Method to set retry flag (called by RetryAnalyzer)
    public static void setRetry(boolean retry) {
        isRetry = retry;
    }


    @BeforeSuite
    public void startGrid() throws Exception {

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
        if(isMac()){
            new Thread(this::runCheckAppScriptAndPrintIfAnr).start();
        }

    }

    @AfterSuite(alwaysRun = true)
    public void cleanup() throws Exception {
        RecorderManager.stopVideoRecording(RecorderManager.RECORDTYPE.MONTE, true);
    }

    @BeforeClass(alwaysRun = true)
    @Parameters({"platform", "udid", "platformVersion"})
    public void setUp(@Optional("Android") String platform,
                      @Optional("emulator-5554") String udid,
                      @Optional("15") String platformVersion) throws Exception {
        if (isRetry) return; // Skip setup during retries
        driver = new DriverFactory().createInstance(platform, udid, platformVersion);
        DriverManager.setDriver(driver);

        RecorderManager.startVideoRecording(RecorderManager.RECORDTYPE.MONTE, "Video01");

        if (driver instanceof CanRecordScreen screenRecorder) {
            Duration timeLimit = Duration.ofMinutes(10); // Increased for retries
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

        wait = new WebDriverWait(getDriver(), Duration.ofSeconds(60));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws IOException, InterruptedException {
        if (isRetry) return; // Skip teardown during retries
        if (getDriver() != null) {
            if (getDriver() instanceof CanRecordScreen screenRecorder) {
                String video = screenRecorder.stopRecordingScreen();
                saveRecording(video);
                attachVideo();
            }
            DriverManager.quit();
        }
    }

    private void saveRecording(String base64Video) throws IOException {
        byte[] decoded = Base64.getDecoder().decode(base64Video);
        String platform = getDriver().getCapabilities().getPlatformName().toString().toLowerCase();
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

    public  void runCheckAppScriptAndPrintIfAnr() {
        String anrMessage = "ANR detected for com.saucelabs.mydemoapp.android. Force stopping.";

        try {
            // Path to your shell script
            String scriptPath = "./check-app.sh";

            // Create command to run
            CommandLine cmdLine = new CommandLine("bash");
            cmdLine.addArgument(scriptPath, false);

            // Prepare to capture output
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(new PumpStreamHandler(outputStream));

            // Run script
            executor.execute(cmdLine);

            // Convert output to string
            String output = outputStream.toString().trim();

            // Check and print if ANR detected
            if (output.contains(anrMessage)) {
                Assert.fail();
            } else {
                System.out.println("No ANR detected in script output.");
            }

        } catch (IOException e) {
            System.err.println("Error executing check-app.sh: " + e.getMessage());
        }
    }
}