package com.dan.utils;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface ExplicitWait {


    // Method to wait for an element to be visible
    static WebElement waitForElementVisible(WebDriverWait wait, By locator) {

        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Method to wait for an element to be clickable
    static WebElement waitForElementClickable(WebDriverWait wait, By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Method to wait for specific text to be present in an element
    static void waitForTextToBePresentInElement(WebDriverWait wait, By locator, String text) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // Method to wait for an element to be invisible
    static void waitForInvisibilityOfElementLocated(WebDriverWait wait, By locator) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
}

