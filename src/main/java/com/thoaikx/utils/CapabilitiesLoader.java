package com.thoaikx.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.Map;

public class CapabilitiesLoader {

  public static UiAutomator2Options loadCapabilitiesAndroidFromJson(String name) throws Exception {
    // Read JSON file into a Map
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> capabilitiesMap = objectMapper.readValue(new File(System.getProperty("user.dir") + name), Map.class);

    // Convert the Map to UiAutomator2Options
    UiAutomator2Options optionsAndroid = new UiAutomator2Options();
    
    //IOS 
    XCUITestOptions	 optionsIOS = new XCUITestOptions();	
    
    capabilitiesMap.forEach(optionsAndroid::setCapability);

    return optionsAndroid;
}

public static XCUITestOptions loadCapabilitiesIOSFromJson(String name) throws Exception {
  // Read JSON file into a Map
  ObjectMapper objectMapper = new ObjectMapper();
  Map<String, Object> capabilitiesMap = objectMapper.readValue(new File(System.getProperty("user.dir") + name), Map.class);

  
  //IOS 
  XCUITestOptions	 optionsIOS = new XCUITestOptions();	
  
  capabilitiesMap.forEach(optionsIOS::setCapability);

  return optionsIOS;
}



}