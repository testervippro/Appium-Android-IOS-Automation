package com.dan.utils;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class Element {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(60);

    // Wait for the element to be visible
    public static void waitForVisibility(AppiumDriver driver, By locator) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Wait for the element to be clickable
    public static void waitForClickable(AndroidDriver driver, By locator) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.elementToBeClickable(locator));
    }

    // Find the WebElement
    private static Optional<WebElement> findElement(AndroidDriver driver, By locator) {
        try {
            return Optional.of(driver.findElement(locator));
        } catch (Exception e) {
            return Optional.empty(); // Handle element not found gracefully
        }
    }

    // Perform an action on the element (e.g., click, sendKeys)
    public static void perform(AndroidDriver driver, By locator, Consumer<WebElement> action) {
        findElement(driver, locator).ifPresent(action);
    }

    // Get a value from the element (e.g., getText, getAttribute)
    public static <T> T get(AndroidDriver driver, By locator, Function<WebElement, T> function) {
        return findElement(driver, locator).map(function).orElse(null);
    }

    // Wait for text to be present in the element
    public static void waitForTextToBePresent(AndroidDriver driver, By locator, String text) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
            .until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }

    // Get attribute value from the element
    public static String getAttribute(AndroidDriver driver, By locator, String attributeName) {
        return get(driver, locator, e -> e.getAttribute(attributeName));
    }


}
