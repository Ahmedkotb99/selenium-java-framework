package com.example.framework.utils;

import com.example.framework.config.Config;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private ScreenshotUtil() {
    }

    public static Path capture(WebDriver driver, String testName) {
        try {
            Path screenshotDirectory = Path.of(Config.get("screenshotDir", "test-output/screenshots"));
            Files.createDirectories(screenshotDirectory);

            String fileName = safeName(testName) + "-" + LocalDateTime.now().format(FORMATTER) + ".png";
            Path destination = screenshotDirectory.resolve(fileName);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination;
        } catch (IOException exception) {
            throw new RuntimeException("Unable to save screenshot", exception);
        }
    }

    private static String safeName(String value) {
        return value.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}
