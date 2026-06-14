package com.example.framework.pages;

import com.example.framework.config.Config;
import com.example.framework.utils.ClipboardUtil;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProjectsPage extends BasePage {
    private final By addProjectButton = By.xpath("/html/body/div[1]/div[1]/main/div/header/div/button[2]");
    private final By createAiUserTestButton = By.xpath("/html/body/div[4]/div[3]/button");
    private final By projectGoalTextArea = By.xpath("/html/body/div[4]/div[1]/div[2]/textarea");
    private final By draftProjectButton = By.xpath("/html/body/div[4]/div[2]/button");
    private final By publishButton = By.xpath("/html/body/div[1]/div[1]/main/div/header/div/div[1]/div/button");
    private final By projectsTable = By.xpath("/html/body/div[1]/div[1]/main/div/main/div[2]/div[1]/div/div[1]");
    private final By projectRows = By.xpath("/html/body/div[1]/div[1]/main/div/main/div[2]/div[1]/div/div[1]/table/tbody/tr");
    private final By newestProjectRow = By.xpath("/html/body/div[1]/div[1]/main/div/main/div[2]/div[1]/div/div[1]/table/tbody/tr[last()]");
    private final By newestProjectOptionsButton = By.xpath("/html/body/div[1]/div[1]/main/div/main/div[2]/div[1]/div/div[1]/table/tbody/tr[last()]/td[6]/button");
    private final By newestProjectPublishButton = By.xpath("/html/body/div[1]/div[1]/main/div/main/div[2]/div[1]/div/div[1]/table/tbody/tr[last()]/td[6]/div/div/button[1]");
    private final By publishedModalTitle = By.xpath("/html/body/div[4]/div[1]/div/h2");
    private final By copyPublishedLinkButton = By.xpath("/html/body/div[4]/div[2]/div/div[2]/div/div/div[2]/button");

    public ProjectsPage(WebDriver driver) {
        super(driver);
    }

    public ProjectsPage open(String expectedProjectsUrl) {
        driver.get(expectedProjectsUrl);
        waitForUrlToBe(expectedProjectsUrl);
        return this;
    }

    public void waitUntilLoaded(String expectedProjectsUrl) {
        waitForUrlToBe(expectedProjectsUrl);
    }

    public ProjectsPage createAiUserTestProject(String projectGoal) {
        click(addProjectButton);
        click(createAiUserTestButton);
        type(projectGoalTextArea, projectGoal);
        click(draftProjectButton);
        return this;
    }

    public ProjectsPage waitUntilDraftIsReady(String projectsUrl) {
        int timeoutSeconds = Config.getInt("projectCreationTimeoutSeconds", 45);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(driver -> projectsUrl.equals(driver.getCurrentUrl()) || anyElementVisible(publishButton));
        return this;
    }

    public ProjectsPage waitUntilProjectsTableHasRows() {
        int timeoutSeconds = Config.getInt("projectsTableTimeoutSeconds", 30);
        waitUntilVisible(projectsTable, timeoutSeconds);
        waitUntilPresent(newestProjectRow, timeoutSeconds);
        return this;
    }

    public int projectRowCount() {
        if (!isVisible(projectsTable, 5)) {
            return 0;
        }
        return driver.findElements(projectRows).size();
    }

    public ProjectsPage waitUntilProjectRowCountGreaterThan(int previousRowCount) {
        int timeoutSeconds = Config.getInt("projectsTableTimeoutSeconds", 30);
        waitUntilVisible(projectsTable, timeoutSeconds);
        new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(driver -> driver.findElements(projectRows).size() > previousRowCount);
        return this;
    }

    public ProjectsPage publishNewestProject() {
        waitUntilProjectsTableHasRows();
        click(newestProjectOptionsButton);
        click(newestProjectPublishButton);
        waitUntilPublishedModalTitleIs(Config.get("publishSuccessTitle", "Your project has been published!"));
        return this;
    }

    public ProjectsPage waitUntilPublishedModalTitleIs(String expectedTitle) {
        waitUntilTextIs(publishedModalTitle, expectedTitle, Config.getInt("publishModalTimeoutSeconds", 30));
        return this;
    }

    public String publishedModalTitle() {
        return textOf(publishedModalTitle);
    }

    public String copyPublishedSurveyLink() {
        ClipboardUtil.clear();
        click(copyPublishedLinkButton);
        return ClipboardUtil.readText(Config.getInt("clipboardTimeoutSeconds", 10));
    }

    public ProjectsPage waitUntilPublishButtonVisible() {
        waitUntilVisible(publishButton, Config.getInt("projectCreationTimeoutSeconds", 45));
        return this;
    }

    public boolean isPublishButtonVisible() {
        return waitUntilVisible(publishButton).isDisplayed();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    private boolean anyElementVisible(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
