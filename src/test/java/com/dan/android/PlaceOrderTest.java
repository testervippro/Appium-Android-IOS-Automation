package com.dan.android;

import com.dan.base.BaseTest;
import com.dan.utils.ExplicitWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.dan.base.RetryAnalyzer;

import static com.dan.config.ConfigurationManager.configuration;
import static com.dan.driver.DriverManager.getDriver;
import static com.sun.jna.Platform.isMac;
import static com.sun.jna.Platform.isWindows;


public class PlaceOrderTest extends BaseTest {

    @Test(retryAnalyzer =  RetryAnalyzer.class)
    public void placeOrder() throws IOException {
        if(isRetry){
            String launchCmdStr = String.format("adb shell am start -n %s/%s", configuration().androidAppPackage(), configuration().androidAppActivity());
            CommandLine launchCmd = CommandLine.parse(launchCmdStr);
            new DefaultExecutor().execute(launchCmd);
        }

        // Open menu
        var menuIV = By.id("com.saucelabs.mydemoapp.android:id/menuIV");
        ExplicitWait.waitForElementVisible(wait, menuIV).click();

        // Navigate to login
        var loginLeftMenu = By.xpath("//android.widget.TextView[@content-desc=\"Login Menu Item\"]");
        ExplicitWait.waitForElementVisible(wait, loginLeftMenu).click();

        // Enter credentials
        ExplicitWait.waitForElementVisible(wait, AppiumBy.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameET\"]"))
                .sendKeys("bob@example.com");
        ExplicitWait.waitForElementVisible(wait, AppiumBy.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/passwordET\"]"))
                .sendKeys("10203040");

        // Click login
        var btnLogin = By.xpath("//android.widget.Button[@content-desc=\"Tap to login with given credentials\"]");
        ExplicitWait.waitForElementClickable(wait, btnLogin).click();

        // Select product
        var product = By.xpath("(//android.widget.ImageView[@content-desc=\"Product Image\"])[1]");
        ExplicitWait.waitForElementVisible(wait, product).click();

        // Add to cart
        var addToCart = By.xpath("//android.widget.Button[@content-desc=\"Tap to add product to cart\"]");

        scrollDown(getDriver());

        ExplicitWait.waitForElementClickable(wait, addToCart).click();

        // Go to cart
        var cart = By.xpath("//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/cartTV\"]");
        ExplicitWait.waitForElementClickable(wait, cart).click();

        // Proceed to checkout
        var checkoutButton = By.xpath("//android.widget.Button[@content-desc=\"Confirms products for checkout\"]");
        ExplicitWait.waitForElementClickable(wait, checkoutButton).click();

        // Fill out checkout form
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/fullNameET\"]"))
                .sendKeys("Test User");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/address1ET\"]"))
                .sendKeys("123 Main St");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/address2ET\"]"))
                .sendKeys("Apt 1");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/cityET\"]"))
                .sendKeys("Test City");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/stateET\"]"))
                .sendKeys("Test State");

        scrollDown(getDriver());

        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/zipET\"]"))
                .sendKeys("12345");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/countryET\"]"))
                .sendKeys("USA");

        // Save checkout info
        var saveCheckout = By.xpath("//android.widget.Button[@content-desc=\"Saves user info for checkout\"]");
        ExplicitWait.waitForElementClickable(wait, saveCheckout).click();

        // Enter payment details
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameET\"]"))
                .sendKeys("Test User");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/cardNumberET\"]"))
                .sendKeys("4111111111111111");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/expirationDateET\"]"))
                .sendKeys("12/25");
        ExplicitWait.waitForElementVisible(wait, By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/securityCodeET\"]"))
                .sendKeys("123");

        // Review order
        var reviewOrder = By.xpath("//android.widget.Button[@content-desc=\"Saves payment info and launches screen to review checkout data\"]");
        ExplicitWait.waitForElementClickable(wait, reviewOrder).click();

        // Place order
        var placeOrder = By.xpath("//android.widget.Button[@content-desc=\"Completes the process of checkout\"]");
        ExplicitWait.waitForElementClickable(wait, placeOrder).click();

        // Verify checkout success
        var successMessage = By.xpath("//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/completeTV\"]");
        String checkOutSuccess = ExplicitWait.waitForElementVisible(wait, successMessage).getText();
        Assert.assertEquals(checkOutSuccess, "Checkout Complete", "Checkout success message mismatch");
    }


    public void scrollDown(AppiumDriver driver) {
        Dimension size = driver.manage().window().getSize();

        int startX = size.width / 2;
        int startY = (int) (size.height * 0.7);
        int endY = (int) (size.height * 0.3);

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence swipe = new Sequence(finger, 1);

        swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), startX, startY));
        swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), startX, endY));
        swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(swipe));
    }


}
