package com.dan.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.options.XCUITestOptions;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class CapabilitiesLoader {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static UiAutomator2Options loadCapabilitiesAndroidFromJson(String relativePath) throws Exception {
    Path filePath = Path.of(System.getProperty("user.dir"), relativePath);
    Map<String, Object> capabilitiesMap = mapper.readValue(Files.newInputStream(filePath), Map.class);

    UiAutomator2Options options = new UiAutomator2Options();
    capabilitiesMap.forEach(options::setCapability);
    return options;
  }

  public static XCUITestOptions loadCapabilitiesIOSFromJson(String relativePath) throws Exception {
    Path filePath = Path.of(System.getProperty("user.dir"), relativePath);
    Map<String, Object> capabilitiesMap = mapper.readValue(Files.newInputStream(filePath), Map.class);

    XCUITestOptions options = new XCUITestOptions();
    capabilitiesMap.forEach(options::setCapability);
    return options;
  }

  public static List<Map<String, Object>> loadAndroidDevicesFromJson(String relativePath) throws Exception {
    Path filePath = Path.of(System.getProperty("user.dir"), relativePath);

    if (!Files.exists(filePath)) {
      throw new IllegalArgumentException("❌ Config file not found at path: " + filePath);
    }

    try (InputStream input = Files.newInputStream(filePath)) {
      return mapper.readValue(input, new TypeReference<>() {});
    }
  }
}
