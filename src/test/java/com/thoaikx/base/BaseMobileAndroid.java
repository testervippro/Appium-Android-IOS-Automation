package com.thoaikx.base;

import com.thoaikx.utils.ExplicitWait;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.thoaikx.utils.AppiumServerManager;
import com.thoaikx.utils.CapabilitiesLoader;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import static com.thoaikx.config.ConfigurationManager.configuration;

public class BaseMobileAndroid implements ExplicitWait {

    protected WebDriverWait wait;

    protected AppiumDriver driver;

    @BeforeMethod
    public void setUpAndroid() throws Exception {

        if(configuration().target().equals("android-ios")) {
            AppiumServerManager.startAppiumServer(configuration().portAndroid());
            Thread.sleep(50000);
        }
        else  {
            AppiumServerManager.startServer();
        }
        // Load capabilities from JSON file
        UiAutomator2Options caps = CapabilitiesLoader.loadCapabilitiesAndroidFromJson("/android.json");
        System.out.println(caps);

        // Initialize the Appium driver
        driver = new AndroidDriver(new URL("http://127.0.0.1:"+configuration().portAndroid()), caps);
             // Initialize WebDriverWait with a default timeout of 60 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }



    @AfterMethod
    public void tearDown() throws IOException, InterruptedException {
        if (driver != null) {
            driver.quit();
        }


        if(configuration().target().equals("android-ios")) {
            AppiumServerManager.killAppiumServer(configuration().portAndroid());
           // Thread.sleep(50000);
        }
        else  {
            AppiumServerManager.stopServer();
        }
    }

    }
    