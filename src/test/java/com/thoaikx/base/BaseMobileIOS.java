package com.thoaikx.base;

import com.thoaikx.config.ConfigurationManager;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.PointOption;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import com.google.common.collect.ImmutableMap;
import com.thoaikx.utils.AppiumServerManager;
import com.thoaikx.utils.CapabilitiesLoader;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import static com.thoaikx.config.ConfigurationManager.configuration;

public class BaseMobileIOS {

    protected WebDriverWait wait;

    protected AppiumDriver driver;


    @BeforeMethod
    public void setUpIOS() throws Exception {


        if(configuration().target().equals("android-ios")) {
            AppiumServerManager.startAppiumServer(configuration().portIOS());
            Thread.sleep(50000);
        }
        else  {
            AppiumServerManager.startServer();
        }

        // Load capabilities from JSON file
        XCUITestOptions caps =  CapabilitiesLoader.loadCapabilitiesIOSFromJson("/ios.json");

        // Initialize the Appium driver
        driver = new IOSDriver(new URL("http://127.0.0.1:1111/wd/hub/"), caps);
             // Initialize WebDriverWait with a default timeout of 60 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }


    @AfterMethod
    public void tearDown() throws IOException, InterruptedException {
        if (driver != null) {
            driver.quit();
        }

        if(configuration().target().equals("android-ios")) {
            AppiumServerManager.killAppiumServer(configuration().portIOS());
            //Thread.sleep(50000);
        }
        else  {
            AppiumServerManager.stopServer();
        }
    }

	}

