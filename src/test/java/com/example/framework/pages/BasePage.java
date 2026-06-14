package com.example.framework.pages;

import com.example.framework.config.Config;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {
    protected final WebDriver driver;
    private final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(Config.getInt("explicitWaitSeconds", 10)));
    }

    protected void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    protected WebElement waitUntilVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilVisible(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitUntilPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected WebElement waitUntilPresent(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected boolean isVisible(By locator, int timeoutSeconds) {
        try {
            return waitUntilVisible(locator, timeoutSeconds).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    protected boolean visibleTextEquals(By locator, String expectedText, int timeoutSeconds) {
        try {
            return expectedText.equals(waitUntilVisible(locator, timeoutSeconds).getText());
        } catch (TimeoutException exception) {
            return false;
        }
    }

    protected void clickAny(By... locators) {
        RuntimeException lastException = null;
        for (By locator : locators) {
            try {
                click(locator);
                return;
            } catch (RuntimeException exception) {
                lastException = exception;
            }
        }
        throw lastException == null ? new IllegalArgumentException("No locators were provided") : lastException;
    }

    protected void type(By locator, String value) {
        WebElement element = waitUntilVisible(locator);
        element.clear();
        element.sendKeys(value);
    }

    protected void uploadFile(By fileInputLocator, String absoluteFilePath) {
        waitUntilPresent(fileInputLocator).sendKeys(absoluteFilePath);
    }

    protected void typeAny(String value, By... locators) {
        RuntimeException lastException = null;
        for (By locator : locators) {
            try {
                type(locator, value);
                return;
            } catch (RuntimeException exception) {
                lastException = exception;
            }
        }
        throw lastException == null ? new IllegalArgumentException("No locators were provided") : lastException;
    }

    protected String textOf(By locator) {
        return waitUntilVisible(locator).getText();
    }

    protected String attributeOf(By locator, String attributeName) {
        return waitUntilVisible(locator).getAttribute(attributeName);
    }

    protected void waitForUrlToBe(String expectedUrl) {
        wait.until(ExpectedConditions.urlToBe(expectedUrl));
    }

    protected void waitUntilTextIs(By locator, String expectedText, int timeoutSeconds) {
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.textToBe(locator, expectedText));
    }
}
