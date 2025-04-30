
package com.dan.driver;


import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

public final class IOSDriverManager implements IDriver {

    private static final Logger logger = LogManager.getLogger(IOSDriverManager.class);
    private AppiumDriver driver;

    @Override
    public AppiumDriver createInstance(String udid, String platformVersion) {
        try {
            Path appPath = Path.of(System.getProperty("user.dir"), "app", "My Demo App.app");

            XCUITestOptions options = new XCUITestOptions();
            options.setDeviceName("iPhone 13");

            options.setUdid("91CC769D-8C83-4310-A6EB-9F3471F04082");
            options.setPlatformVersion("15.0");
            options.setApp(appPath.toString());
            options.doesConnectHardwareKeyboard();
            options.doesForceSimulatorSoftwareKeyboardPresence();

            driver = new IOSDriver(new URL("http://localhost:4444/wd/hub"), options);


        } catch (MalformedURLException e) {
            logger.error("Failed to initialize iOS driver for device {}: {}", udid, e.getMessage());
            throw new RuntimeException("Failed to create iOS driver instance", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return driver;
    }


}