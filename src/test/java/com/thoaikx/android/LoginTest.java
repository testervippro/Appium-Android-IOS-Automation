package com.thoaikx.android;


import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.thoaikx.base.BaseMobileAndroid;

import io.appium.java_client.AppiumBy;

//Full name
//XCUIElementTypeTextField[@value="Maxim Winter"]

//Card number
//XCUIElementTypeTextField[@value="3258 1265 7568 7896"]

//Xp
//XCUIElementTypeTextField[@value="03/25"]

 // Code

//XCUIElementTypeTextField[@value="123"]

//Check box
//XCUIElementTypeButton[@name="CheckBoxUnselected Icons"]

//receiver order bin
//XCUIElementTypeButton[@name="Review Order"]

//Place order
//XCUIElementTypeButton[@name="Review Order"]

//XCUIElementTypeStaticText[@name="Checkout Complete"]

//XCUIElementTypeButton[@name="ContinueShopping"]


//Androidi

//Product 1
        //android.widget.ImageView[@content-desc="Product Image"][1]

//Add to card
//android.widget.Button[@content-desc="Tap to add product to cart"]

//Go to card
//android.widget.TextView[@resource-id="com.saucelabs.mydemoapp.android:id/cartTV"]


// process checkout
//android.widget.Button[@content-desc="Confirms products for checkout"]

//full name
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/fullNameET"]

//address 1
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/address1ET"]

//addres 2
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/address2ET"]

// city
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/cityET"]

//state
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/stateET"]

//zipcode
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/zipET"]

// city
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/countryET"]

//to payment
//android.widget.Button[@content-desc="Saves user info for checkout"]


//full name
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/nameET"]

//Card number
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/cardNumberET"]

//Ed
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/expirationDateET"]

//code
//android.widget.EditText[@resource-id="com.saucelabs.mydemoapp.android:id/securityCodeET"]

//review. Order
//android.widget.Button[@content-desc="Saves payment info and launches screen to review checkout data"]

//place order

//android.widget.Button[@content-desc="Completes the process of checkout"]
//Text success
//android.widget.TextView[@resource-id="com.saucelabs.mydemoapp.android:id/completeTV"]

public class LoginTest  extends BaseMobileAndroid{


    @Test
    public   void placeOrder() throws InterruptedException {

        var menuIV = By.id("com.saucelabs.mydemoapp.android:id/menuIV");
        waitForElementVisible( menuIV).click();;

        //login left menu
        var loginLeftMenu = By.xpath("//android.widget.TextView[@content-desc=\"Login Menu Item\"]");
        waitForElementVisible(loginLeftMenu).click();

        //input name, pass word
        Thread.sleep(1000);
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameET\"]")).sendKeys("bob@example.com");;
        driver.findElement(AppiumBy.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/passwordET\"]")).sendKeys("10203040");

        //click  login
        var btnLogin = By.xpath("//android.widget.Button[@content-desc=\"Tap to login with given credentials\"]");
        waitForElementClickable(btnLogin).click();

        Thread.sleep(2000);
        //Click product 1
        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Product Image\"][1] ")).click();
        Thread.sleep(2000);

        //click card
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Tap to add product to cart\"]")).click();
        Thread.sleep(2000);

        //click card
        driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/cartTV\"]")).click();

        //check out
        Thread.sleep(2000);
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Confirms products for checkout\"]")).click();

        Thread.sleep(2000);
        //fillout form
        //full namme
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/fullNameET\"]")).sendKeys("123");

        //adress 1
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/address1ET\"]")).sendKeys("123");

        //addrees 2
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/address2ET\"]")).sendKeys("123");

        //city
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/cityET\"]")).sendKeys("123");

        //state
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/stateET\"]")).sendKeys("HN");

        //zipcode
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/zipET\"]")).sendKeys("HN");

        //city
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/countryET\"]")).sendKeys("HN");

        //process checkout
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Saves user info for checkout\"]")).click();

        Thread.sleep(20000);
        //full name
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/nameET\"]")).sendKeys("123");

        //card number
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/cardNumberET\"]")).sendKeys("123");

        //eD
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/expirationDateET\"]")).sendKeys("12/25");

        //code
        driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"com.saucelabs.mydemoapp.android:id/securityCodeET\"]")).sendKeys("123");

        //receive order
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Saves payment info and launches screen to review checkout data\"]")).click();
        Thread.sleep(2000);

        //place order
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Completes the process of checkout\"]")).click();
        Thread.sleep(2000);

        //verify
        var checkOutSuccess = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.saucelabs.mydemoapp.android:id/completeTV\"]")).getText();

        Assert.assertEquals(checkOutSuccess,"Checkout Complete");


    }

}
