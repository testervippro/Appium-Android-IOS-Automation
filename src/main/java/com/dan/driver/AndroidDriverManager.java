package com.dan.driver;

import com.dan.config.Configuration;
import com.dan.config.ConfigurationManager;
import com.dan.utils.CapabilitiesLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import static com.dan.config.ConfigurationManager.configuration;

public final class AndroidDriverManager implements IDriver {

    private static final Logger logger = LogManager.getLogger(AndroidDriverManager.class);
    private AppiumDriver driver;

    @Override
    public AppiumDriver createInstance(String udid, String platformVersion) {
        try {
            Path appPath = Path.of(System.getProperty("user.dir"), "app", "app-android.apk");

            UiAutomator2Options options = new UiAutomator2Options()
                    .setApp(appPath.toString())
                    .setAppPackage("com.saucelabs.mydemoapp.android")
                    .setAppActivity("com.saucelabs.mydemoapp.android.view.activities.SplashActivity");

            driver = new AndroidDriver(new URL("http://localhost:4444/wd/hub"), options);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return driver;
    }


}