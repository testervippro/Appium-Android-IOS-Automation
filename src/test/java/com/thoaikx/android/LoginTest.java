package com.thoaikx.android;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.thoaikx.base.BaseMobileAndroid;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.pagefactory.AndroidFindBy;

public class LoginTest  extends BaseMobileAndroid{


    @Test
    public   void login() throws InterruptedException {
 
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

        
    }

}
