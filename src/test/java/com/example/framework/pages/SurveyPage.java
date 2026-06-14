package com.example.framework.pages;

import com.example.framework.config.Config;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SurveyPage extends BasePage {
    private final By landingHeading = By.xpath("/html/body/main/div[1]/div/div/div/h2");
    private final By startSurveyButton = By.xpath("/html/body/main/div[1]/div/button");
    private final By instructionText = By.xpath("/html/body/main/div[1]/div/div[1]/div/div/p");

    public SurveyPage(WebDriver driver) {
        super(driver);
    }

    public SurveyPage openInNewTab(String surveyLink) {
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(surveyLink);
        return this;
    }

    public SurveyPage waitUntilLandingHeadingVisible() {
        waitUntilVisible(landingHeading, Config.getInt("surveyHeadingTimeoutSeconds", 30));
        return this;
    }

    public String landingHeadingText() {
        return textOf(landingHeading);
    }

    public SurveyPage startSurvey() {
        click(startSurveyButton);
        return this;
    }

    public SurveyPage waitUntilInstructionTextContains(String expectedText) {
        String normalizedExpectedText = normalize(expectedText);
        int timeoutSeconds = Config.getInt("surveyInstructionTimeoutSeconds", 30);

        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(driver -> normalize(driver.findElement(instructionText).getText()).contains(normalizedExpectedText));

        return this;
    }

    public String instructionText() {
        return textOf(instructionText);
    }

    private String normalize(String value) {
        return value.replaceAll("\\s+", " ").trim();
    }
}
