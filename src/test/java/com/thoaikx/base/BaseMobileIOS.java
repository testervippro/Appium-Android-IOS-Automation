package com.thoaikx.base;

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
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.google.common.collect.ImmutableMap;
import com.thoaikx.utils.AppiumServerManager;
import com.thoaikx.utils.CapabilitiesLoader;

import java.net.URL;
import java.time.Duration;

public class BaseMobileIOS {

    protected WebDriverWait wait;

    protected AppiumDriver driver;


    @BeforeClass
    public void setUpIOS() throws Exception {

        AppiumServerManager.startServer();

        // Load capabilities from JSON file
        XCUITestOptions caps =  CapabilitiesLoader.loadCapabilitiesIOSFromJson("/ios.json");

        // Initialize the Appium driver
        driver = new IOSDriver(new URL("http://127.0.0.1:8888/wd/hub"), caps);
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



    	@SuppressWarnings("deprecation")
        public static void SwipeScreen(WebElement el, IOSDriver driver) throws InterruptedException {
		WebElement Panel = el;
		Dimension dimension = Panel.getSize();
		
		int Anchor = Panel.getSize().getHeight()/2; 
		
		Double ScreenWidthStart = dimension.getWidth() * 0.8;
		int scrollStart = ScreenWidthStart.intValue();
		
		Double ScreenWidthEnd = dimension.getWidth() * 0.2;
		int scrollEnd = ScreenWidthEnd.intValue();
		
		new TouchAction((PerformsTouchActions) driver)
		.press(PointOption.point(scrollStart, Anchor))
		.waitAction(WaitOptions.waitOptions(Duration.ofSeconds(1)))
		.moveTo(PointOption.point(scrollEnd, Anchor))
		.release().perform();
		
		Thread.sleep(3000);
	}

    
    }
    