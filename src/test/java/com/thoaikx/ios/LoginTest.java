package com.thoaikx.ios;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.thoaikx.base.BaseMobileIOS;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;

import static com.thoaikx.utils.ExplicitWait.waitForElementClickable;
import static com.thoaikx.utils.ExplicitWait.waitForElementVisible;

//sample  basic flow auto mobileIos 
public class LoginTest extends BaseMobileIOS {

    @Test
    public void placeOrder() throws InterruptedException {

        Map<String, Object> params1 = new HashMap<String, Object>();
        params1.put("direction", "up");

        var menuIV = By.xpath("//XCUIElementTypeButton[@name=\"More-tab-item\"]");
        waitForElementVisible(wait,menuIV).click();
        ;

        // login menu
        var loginLeftMenu = By.xpath("//XCUIElementTypeOther[@name=\"Login Button\"]");
        waitForElementVisible(wait,loginLeftMenu).click();

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
        waitForElementVisible(wait,btnLogin).click();

        // add one product to card
        var product1 = By.xpath("(//XCUIElementTypeImage[@name=\"Product Image\"])[1]");
        waitForElementClickable(wait,product1).click();

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
       var  address2 = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Entrance 1\"]"));
       address2.sendKeys("test");



        // city
       var city = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Truro\"]"));
       Thread.sleep(2000);
       city.sendKeys("city");

        // state
       var state =  driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Cornwall\"]"));
        Thread.sleep(2000);
       state.sendKeys("State");


        // zipcode
       var zipCode = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"89750\"]"));
        Thread.sleep(2000);
        zipCode.sendKeys("123");



        // country
        WebElement country = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"United Kingdom\"]"));
        //country.click();
        Thread.sleep(5000);
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeKeyboard//XCUIElementTypeKey[@name='Return']")));
        country.sendKeys("HN");
        //country.sendKeys(Keys.RETURN); // or use Keys.ENTER

        //driver.executeScript("mobile:swipe", params1);


        //up,down,left,right
        ///params1.put("element", ((RemoteWebElement)ele).getId());


        //word around hiden key
//        // address 2
   // driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"test01\"]")).click();


        // process
//        params1.put("direction", "up");
//        // params1.put("element", ((RemoteWebElement)ele).getId());
//        driver.executeScript("mobile:swipe", params1);

        driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"To Payment\"]")).click();
        Thread.sleep(30000);

        // fill credit car

        // full name
        var fullName = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"Maxim Winter\"]"));
        fullName.sendKeys("test01");

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
      WebElement code =  driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"123\"]"));
      code.sendKeys("123");
      code.sendKeys(Keys.ENTER);

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
