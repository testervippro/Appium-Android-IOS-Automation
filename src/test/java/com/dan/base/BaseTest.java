package com.dan.base;


import com.dan.driver.DriverFactory;
import com.dan.driver.IDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.net.MalformedURLException;
import java.time.Duration;

public class BaseTest {

    protected AppiumDriver driver = null;
    protected WebDriverWait wait;

//    AppiumServiceBuilder builder = new AppiumServiceBuilder()
//            .withIPAddress("127.0.0.1")
//            .usingPort(0);
//
//    AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
//    public int usedPort = service.getUrl().getPort();
    private AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();
    @BeforeClass(alwaysRun = true)
    @Parameters({"platform", "udid", "platformVersion"})
    public void preCondition(@Optional("Android") String platform, @Optional("emulator-5554") String udid, @Optional("") String platformVersion) throws MalformedURLException {
         service.start();
        driver = new DriverFactory().createInstance(platform, udid, platformVersion);
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            driver.quit();
        }
       service.stop();
//        Thread.sleep(1000);
//        stopAppiumService(service);
    }



    public static void stopAppiumService(AppiumDriverLocalService service) {
        if (service != null) {
            try {
                if (service.isRunning()) {
                    service.stop();

                    // Additional cleanup for assurance
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        Runtime.getRuntime().exec("taskkill /F /IM node.exe");
                    } else {
                        Runtime.getRuntime().exec("pkill -f appium");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error stopping Appium: " + e.getMessage());
            }
        }
    }
}
