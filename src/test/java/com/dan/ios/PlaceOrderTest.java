package com.dan.ios;

import com.dan.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

import static com.dan.utils.ExplicitWait.waitForElementClickable;
import static com.dan.utils.ExplicitWait.waitForElementVisible;

// Sample basic flow for mobile iOS login and order placement
public class PlaceOrderTest extends BaseTest {

    @Test
    public void placeOrder() throws InterruptedException {



        // Using driver to retrieve the Appium driver instance
        var menuIV = By.xpath("//XCUIElementTypeButton[@name=\"More-tab-item\"]");
        waitForElementVisible(wait, menuIV).click();

        // Login menu
        var loginLeftMenu = By.xpath("//XCUIElementTypeOther[@name=\"Login Button\"]");
        waitForElementVisible(wait, loginLeftMenu).click();


        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"bob@example.com\"]")).click();

        // Click login button
        var btnLogin = By.xpath("(//XCUIElementTypeStaticText[@name=\"Login\"])[2]");
        waitForElementVisible(wait, btnLogin).click();

        // Add product to cart
        var product1 = By.xpath("(//XCUIElementTypeImage[@name=\"Product Image\"])[1]");
        waitForElementClickable(wait, product1).click();

        // Add to cart
        Thread.sleep(2000);
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"AddToCart\"]")).click();

        // Open cart
        driver.findElement(AppiumBy.accessibilityId("Cart-tab-item")).click();

        // Proceed to checkout
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"ProceedToCheckout\"]")).click();

        // Fill out the form
        // Name
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Rebecca Winter\"]")).sendKeys("test01");

        // Address 1
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Mandorley 112\"]")).sendKeys("test01");

        // Address 2
        var address2 = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Entrance 1\"]"));
        address2.sendKeys("test");

        // City
        var city = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Truro\"]"));
        Thread.sleep(2000);
        city.sendKeys("city");

        // State
        var state = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Cornwall\"]"));
        Thread.sleep(2000);
        state.sendKeys("State");

        address2.click();


        // Zip Code
        var zipCode = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"89750\"]"));
        zipCode.sendKeys("123");

        address2.click();

        // Country
        WebElement country = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"United Kingdom\"]"));
        Thread.sleep(5000);
        country.sendKeys("HN");

        Thread.sleep(2000);
        address2.click();

        Thread.sleep(2000);
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"To Payment\"]")).click();
        Thread.sleep(3000);

        // Fill out credit card information
        var fullName = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Maxim Winter\"]"));
        fullName.sendKeys("test01");

        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"3258 1265 7568 7896\"]"))
                .sendKeys("258 1265 7568 7896");


        // Expiry Date
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"03/25\"]")).sendKeys("12/25");

        // CVV
        WebElement code = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"123\"]"));
        code.sendKeys("123");
        code.sendKeys(Keys.ENTER);

        // Review order
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Review Order\"]")).click();

        // Place the order
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Place Order\"]")).click();

        // Verify the checkout success message
        var checkOutSuccess = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Checkout Complete\"]")).getText();

        Assert.assertEquals(checkOutSuccess, "Checkout Complete");
    }
}
