package com.dan.base;


import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import static com.sun.jna.Platform.isMac;

public class RetryAnalyzer implements IRetryAnalyzer {
    private final  static boolean os = isMac();
    private static final int MAX_RETRY_COUNT = os ? 3:0;
    private int retryCount = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            BaseTest.setRetry(true); // Set retry flag
            return true; // Retry the test
        }

        BaseTest.setRetry(false); // Reset flag after last retry
        return false; // No more retries
    }
}