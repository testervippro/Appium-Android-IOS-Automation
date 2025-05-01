package com.dan.base;


import com.dan.driver.DriverFactory;
import com.dan.driver.IDriver;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.qameta.allure.Step;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;

public class BaseTest {

    protected AppiumDriver driver = null;
    protected WebDriverWait wait;

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
