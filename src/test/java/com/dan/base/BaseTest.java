package com.dan.base;


import com.dan.driver.DriverFactory;
import com.dan.driver.IDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.qameta.allure.Step;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;

public class BaseTest {

    protected AppiumDriver driver = null;
    protected WebDriverWait wait;


    //
    @BeforeSuite
    public void startGrid() throws IOException {

        // Update hubHost IP
        CommandLine updateIP = CommandLine.parse("node nodejs/src/update-Ip.js");
        CommandLine killPort = CommandLine.parse("node nodejs/src/kill-port.js");

        DefaultExecutor executorUpdateIP = new DefaultExecutor();
        executorUpdateIP.setStreamHandler(new PumpStreamHandler(System.out)); // Optional: to log output
        executorUpdateIP.execute(updateIP);
        executorUpdateIP.execute(killPort);

        Thread thread = new Thread(() -> {
            try {
                CommandLine runGrid = CommandLine.parse("node nodejs/src/run-grid.js");

                DefaultExecutor executorRunGrid = new DefaultExecutor();
                executorRunGrid.setStreamHandler(new PumpStreamHandler(System.out)); // Optional: to log output
                executorRunGrid.execute(runGrid);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();

        // Optional wait to let the grid boot up before tests start
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    @BeforeTest(alwaysRun = true)
    @Parameters({"platform", "udid", "platformVersion"})
    public void preCondition(@Optional("Android") String platform, @Optional("emulator-5554") String udid, @Optional("") String platformVersion) throws IOException {

        driver = new DriverFactory().createInstance(platform, udid, platformVersion);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @AfterTest(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            driver.quit();
        }
    }

}
