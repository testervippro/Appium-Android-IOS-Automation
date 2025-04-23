package com.dan.driver;



import com.dan.config.Configuration;
import com.dan.config.ConfigurationManager;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.io.IOException;

public interface IDriver {


    AppiumDriver createInstance(String udid, String platformVersion);
   // AppiumDriver createInstance(String udid,int portAppium, String platformVersion);





}


