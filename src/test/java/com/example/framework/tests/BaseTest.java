package com.example.framework.tests;

import com.example.framework.config.Config;
import com.example.framework.driver.DriverFactory;
import com.example.framework.utils.ScreenshotUtil;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        WebDriver driver = DriverFactory.createDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.getInt("implicitWaitSeconds", 0)));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(Config.getInt("pageLoadTimeoutSeconds", 30)));
        driver.get(Config.baseUrl());
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = DriverFactory.currentDriver();
        if (driver != null && !result.isSuccess()) {
            ScreenshotUtil.capture(driver, result.getMethod().getMethodName());
        }
        DriverFactory.quitDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }
}
