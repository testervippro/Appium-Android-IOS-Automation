package com.dan.driver;


import io.appium.java_client.AppiumDriver;

import java.net.MalformedURLException;

public class DriverFactory {

    public AppiumDriver createInstance(String platform, String udid, String platformVersion) throws MalformedURLException {
        AppiumDriver driver;
        Platform mobilePlatform = Platform.valueOf(platform.toUpperCase());

        switch (mobilePlatform) {
            case IOS -> driver = new IOSDriverManager().createInstance(udid, platformVersion);
            case ANDROID -> driver = new AndroidDriverManager().createInstance(udid, platformVersion);
            default -> throw new RuntimeException(
                    "Platform not supported! Please set 'ios' or 'android' as a parameter.");
        }

        return driver;
    }
}

