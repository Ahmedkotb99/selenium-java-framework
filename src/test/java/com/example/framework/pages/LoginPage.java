package com.example.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    private final By emailField = By.name("email");
    private final By emailFieldByFullXpath = By.xpath("/html/body/div[1]/div/div/div/div[2]/form/div/div[1]/div/div[2]/input");
    private final By submitButton = By.cssSelector("button[type='submit']");
    private final By continueButtonByFullXpath = By.xpath("/html/body/div[1]/div/div/div/div[2]/form/div/div[1]/button");
    private final By passwordField = By.xpath("/html/body/div[1]/div/div/div/div[2]/form/div/div/div[2]/div[2]/input");
    private final By passwordFieldByType = By.cssSelector("input[type='password']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage enterEmail(String email) {
        typeAny(email, emailField, emailFieldByFullXpath);
        return this;
    }

    public LoginPage clickContinue() {
        clickAny(submitButton, continueButtonByFullXpath);
        return this;
    }

    public LoginPage enterPassword(String password) {
        typeAny(password, passwordFieldByType, passwordField);
        return this;
    }

    public ProjectsPage login(String email, String password) {
        enterEmail(email);
        clickContinue();
        enterPassword(password);
        clickContinue();
        return new ProjectsPage(driver);
    }
}
