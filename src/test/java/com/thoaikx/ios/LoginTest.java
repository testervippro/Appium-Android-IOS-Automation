package com.thoaikx.ios;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.thoaikx.base.BaseMobileIOS;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;

//sample  basic flow auto mobileIos 
public class LoginTest extends BaseMobileIOS {

    @Test
    public void placeOrder() throws InterruptedException {

        Map<String, Object> params1 = new HashMap<String, Object>();
        params1.put("direction", "up");

        var menuIV = By.xpath("//XCUIElementTypeButton[@name=\"More-tab-item\"]");
        waitForElementVisible(menuIV).click();
        ;

        // login menu
        var loginLeftMenu = By.xpath("//XCUIElementTypeOther[@name=\"Login Button\"]");
        waitForElementVisible(loginLeftMenu).click();

        // input name, pass word
        // not hiden keyword
        // driver.findElement(AppiumBy.xpath("//XCUIElementTypeTextField")).sendKeys("bob@example.com");
        // driver.findElement(By.xpath("//XCUIElementTypeSecureTextField")).sendKeys("10203040");

        // params1.put("element", ((RemoteWebElement)ele).getId());
        driver.executeScript("mobile:swipe", params1);
        // click on list user to not open keyboard
        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"bob@example.com\"]")).click();

        // click login
        var btnLogin = By.xpath("(//XCUIElementTypeStaticText[@name=\"Login\"])[2]");
        waitForElementVisible(btnLogin).click();

        // add one product to card
        var product1 = By.xpath("(//XCUIElementTypeImage[@name=\"Product Image\"])[1]");
        waitForElementClickable(product1).click();

        // click add to card
        Thread.sleep(2000);
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"AddToCart\"]")).click();

        // card menu
        driver.findElement(AppiumBy.accessibilityId("Cart-tab-item")).click();

        // process checkout
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"ProceedToCheckout\"]")).click();

        // fill out form
        // name
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Rebecca Winter\"]")).sendKeys("test01");

        // address 1
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Mandorley 112\"]")).sendKeys("test01");

        // address 2
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Entrance 1\"]")).sendKeys("test");


        // up,down,left,right
        // params1.put("element", ((RemoteWebElement)ele).getId());
        driver.executeScript("mobile:swipe", params1);

        // city
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Truro\"]")).sendKeys("city");

        // state
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Cornwall\"]")).sendKeys("State");
        Thread.sleep(1000);

        // zipcode
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"89750\"]")).sendKeys("123");

        // country
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"United Kingdom\"]")).sendKeys("HN\n");

        // process
        params1.put("direction", "up");
        // params1.put("element", ((RemoteWebElement)ele).getId());
        driver.executeScript("mobile:swipe", params1);

        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"To Payment\"]")).click();
        Thread.sleep(30000);

        // fill credit car
        // full name
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Maxim Winter\"]")).sendKeys("test01");

        // card number
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"3258 1265 7568 7896\"]"))
                .sendKeys("258 1265 7568 7896");

        // scroll
        // process
        params1.put("direction", "up");
        // params1.put("element", ((RemoteWebElement)ele).getId());
        driver.executeScript("mobile:swipe", params1);

        // date
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"03/25\"]")).sendKeys("12/25");

        // code
        driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"123\"]")).sendKeys("123");

        // reveiver order
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Review Order\"]")).click();

        // place order
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Place Order\"]")).click();

        // verify
        var checkOutSuccess = driver.findElement(By.xpath("\n" + //
                "//XCUIElementTypeStaticText[@name=\"Checkout Complete\"]")).getText();

        Assert.assertEquals(checkOutSuccess, "Checkout Complete");

    }
}
