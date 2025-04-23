/*
 * MIT License
 *
 * Copyright (c) 2018 Elias Nogueira
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dan.driver;


import com.dan.config.Configuration;
import com.dan.config.ConfigurationManager;
import com.dan.utils.CapabilitiesLoader;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.remote.AutomationName;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import static com.dan.config.ConfigurationManager.configuration;

public final class IOSDriverManager implements IDriver {

    private static final Logger logger = LogManager.getLogger(IOSDriverManager.class);
    private AppiumDriver driver;

    @Override
    public AppiumDriver createInstance(String udid, String platformVersion) {
        try {
            var caps = CapabilitiesLoader.loadCapabilitiesAndroidFromJson("/ios.json");
//            if (configuration().target().equalsIgnoreCase("grid")) {
//                driver = new IOSDriver(new URL("http://127.0.0.1:4444"), caps);
//            }
            driver = new IOSDriver(new URL("http://127.0.0.1:4444"), caps);

        } catch (MalformedURLException e) {
            logger.error("Failed to initialize iOS driver for device {}: {}", udid, e.getMessage());
            throw new RuntimeException("Failed to create iOS driver instance", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return driver;
    }


}