//package com.dan.wait;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.sql.DriverManager;
//import java.time.Duration;
//
//public class WaitUtils {
//
//    public static WebElement waitForElementVisible(By locator, int seconds) {
//        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds));
//        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//    }
//
//    public static WebElement waitForElementClickable(By locator, int seconds) {
//        WebDriverWait wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(seconds));
//        return wait.until(ExpectedConditions.elementToBeClickable(locator));
//    }
//}
