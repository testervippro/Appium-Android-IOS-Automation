
package com.dan.driver;


import com.dan.utils.IOSSimulatorUltis;
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
        IOSSimulatorUltis.DeviceInfo deviceInfo = IOSSimulatorUltis.getDeviceInfoByName("Iphone 13");

        try {
            Path appPath = Path.of(System.getProperty("user.dir"), "app", "My Demo App.app");

            XCUITestOptions options = new XCUITestOptions();
            options.usePrebuiltWda();
            options.setDeviceName(deviceInfo.getDeviceName());
            options.setUdid(deviceInfo.getUdid());
            options.setPlatformVersion(deviceInfo.getPlatformVersion());
            options.setApp(appPath.toString());

            options.doesFullReset();
            options.doesConnectHardwareKeyboard();
            options.connectHardwareKeyboard();
            options.doesForceSimulatorSoftwareKeyboardPresence();
            options.forceSimulatorSoftwareKeyboardPresence();

            // Adding timeouts
            // Base on this issue https://github.com/appium/appium/issues/15377
            options.setCapability("wdaLaunchTimeout", 40000); // Set the timeout for WDA to launch
            options.setCapability("wdaStartupRetries", 3); // Retry 3 times
            options.setCapability("wdaStartupRetryInterval", 5000); // Retry interval of 5 seconds

            // Connection and implicit wait timeout
            options.setCapability("appium:connectTimeout", 60000); // Set connection timeout (60 seconds)
            options.setCapability("appium:newCommandTimeout", 1800); // Timeout for no new command (30 minutes)

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