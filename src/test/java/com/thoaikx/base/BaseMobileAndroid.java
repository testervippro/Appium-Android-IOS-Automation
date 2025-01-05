package com.thoaikx.base;

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

import java.net.URL;
import java.time.Duration;

public class BaseMobileAndroid {

    protected WebDriverWait wait;

    protected AppiumDriver driver;

    @BeforeClass
    public void setUpAndroid() throws Exception {

        AppiumServerManager.startServer();

        // Load capabilities from JSON file
        UiAutomator2Options caps = CapabilitiesLoader.loadCapabilitiesAndroidFromJson("/android.json");
        System.out.println(caps);

        // Initialize the Appium driver
        driver = new AndroidDriver(new URL("http://127.0.0.1:8888/wd/hub"), caps);
             // Initialize WebDriverWait with a default timeout of 60 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }



    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }

        AppiumServerManager.stopServer();
    }

       // Reusable method to wait for an element to be visible
    public WebElement waitForElementVisible(By menuIconLocator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(menuIconLocator));
    }

    // Reusable method to wait for an element to be clickable
    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForTextToBePresentInElement(By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // Reusable method to wait for an element to be invisible
    public void waitForInvisibilityOfElementLocated(By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    }
    