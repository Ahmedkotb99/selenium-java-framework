package com.example.framework.pages;

import com.example.framework.config.Config;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class TeachAiPage extends BasePage {
    private final By teachAiSideNavLink = By.xpath("/html/body/div[1]/div[1]/aside/nav/ul/li[2]/a/span[2]");
    private final By addLinkButton = By.xpath("/html/body/div[1]/div[1]/main/div/main/div/div/aside/div/div[1]/div[1]/div[2]/div[1]/button");
    private final By addFileButton = By.xpath("/html/body/div[1]/div[1]/main/div/main/div/div/aside/div/div[1]/div[1]/div[2]/div[2]/button");
    private final By linkInput = By.xpath("/html/body/div[1]/div[1]/main/div/main/div/div/main/div/div[2]/div/div[2]/div/input");
    private final By confirmLinkButton = By.xpath("/html/body/div[1]/div[1]/main/div/main/div/div/main/div/div[2]/div/div[3]/button[2]/div");
    private final By fileInput = By.cssSelector("input[type='file']");
    private final By confirmFileButton = By.xpath("/html/body/div[1]/div[1]/main/div/main/div/div/main/div/div[2]/div/div[4]/button[2]");
    private final By addedUrlText = By.xpath("/html/body/div[1]/div[1]/main/div/main/div/div/aside/div/div[1]/div[2]/div[1]/div[1]/span");
    private final By addedFileNameText = By.xpath("/html/body/div[1]/div[1]/main/div/main/div/div/aside/div/div[1]/div[2]/div[2]/div[1]/span");

    public TeachAiPage(WebDriver driver) {
        super(driver);
    }

    public TeachAiPage openFromSideNav(String expectedTeachAiUrl) {
        click(teachAiSideNavLink);
        waitForUrlToBe(expectedTeachAiUrl);
        return this;
    }

    public TeachAiPage waitUntilReady() {
        waitUntilVisible(addLinkButton);
        waitUntilVisible(addFileButton);
        return this;
    }

    public TeachAiPage addLink(String url) {
        click(addLinkButton);
        type(linkInput, url);
        click(confirmLinkButton);
        waitUntilVisible(addedUrlText, Config.getInt("sourceUploadTimeoutSeconds", 30));
        return this;
    }

    public TeachAiPage ensureLinkAdded(String url) {
        if (!visibleTextEquals(addedUrlText, url, 2)) {
            addLink(url);
        }
        return this;
    }

    public TeachAiPage addFile(String filePath) {
        Path uploadPath = resolveUploadPath(filePath);
        if (!Files.exists(uploadPath)) {
            throw new IllegalArgumentException("Upload file was not found: " + uploadPath);
        }

        click(addFileButton);
        uploadFile(fileInput, uploadPath.toString());
        click(confirmFileButton);
        waitUntilVisible(addedFileNameText, Config.getInt("sourceUploadTimeoutSeconds", 30));
        return this;
    }

    public TeachAiPage ensureFileAdded(String filePath) {
        String expectedFileName = Path.of(filePath).getFileName().toString();
        if (!visibleTextEquals(addedFileNameText, expectedFileName, 2)) {
            addFile(filePath);
        }
        return this;
    }

    public String addedUrlText() {
        return textOf(addedUrlText);
    }

    public String addedFileNameText() {
        return textOf(addedFileNameText);
    }

    private Path resolveUploadPath(String filePath) {
        Path configuredPath = Path.of(filePath);
        if (Files.exists(configuredPath)) {
            return configuredPath.toAbsolutePath();
        }

        URL resource = TeachAiPage.class.getClassLoader().getResource(filePath);
        if (resource != null) {
            try {
                return Path.of(resource.toURI()).toAbsolutePath();
            } catch (URISyntaxException exception) {
                throw new IllegalArgumentException("Upload resource path is invalid: " + filePath, exception);
            }
        }

        return configuredPath.toAbsolutePath();
    }
}
