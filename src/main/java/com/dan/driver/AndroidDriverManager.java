package com.dan.driver;

import com.dan.utils.AdbUtils;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.nio.file.Path;

import static com.dan.config.ConfigurationManager.configuration;
import static com.sun.jna.Platform.isWindows;


public final class AndroidDriverManager implements IDriver {

    private static final Logger logger = LogManager.getLogger(AndroidDriverManager.class);
    private AppiumDriver driver;

    @Override
    public AppiumDriver createInstance(String udid, String platformVersion) {

        try {
            Path appPath = Path.of(System.getProperty("user.dir"), configuration().androidAppPath());

            UiAutomator2Options options = new UiAutomator2Options()
                    .setPlatformName("Android")
                    .setUdid(udid)
                    .setPlatformVersion(platformVersion)
                    .setAppPackage(configuration().androidAppPackage())
                    .setAppActivity(configuration().androidAppActivity());

            if (configuration().isDockerRealDevice()) {
                // In Docker, you might need a different path
                options.setApp("/home/androidusr/app-android.apk");

                if(isWindows()){
                    // Add the remoteAdbHost capability to ensure ADB connection works in Docker/Windows
                    options.setCapability("appium:remoteAdbHost", "host.docker.internal");
                }

            }
            else {
                // Default path for non-Docker environments
                options.setApp(appPath.toString());
                }



            driver = new AndroidDriver(new URL(configuration().gridUrl()), options);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return driver;
    }

}
