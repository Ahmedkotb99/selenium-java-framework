package com.example.framework.driver;

import com.example.framework.config.Config;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        String browser = Config.get("browser", "chrome").toLowerCase(Locale.ROOT);
        boolean headless = Config.getBoolean("headless", false);

        WebDriver driver = switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            case "edge" -> createEdgeDriver(headless);
            case "chrome" -> createChromeDriver(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        DRIVER.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver was not created. Check the test setup.");
        }
        return driver;
    }

    public static WebDriver currentDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.setExperimentalOption("prefs", chromiumPrefs());
        addScreenShareOptions(options);
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.setExperimentalOption("prefs", chromiumPrefs());
        addScreenShareOptions(options);
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
        }
        return new EdgeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
        }
        return new FirefoxDriver(options);
    }

    private static Map<String, Object> chromiumPrefs() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("profile.default_content_setting_values.clipboard", 1);
        return prefs;
    }

    private static void addScreenShareOptions(ChromeOptions options) {
        if (!Config.getBoolean("autoAllowScreenShare", true)) {
            return;
        }

        options.addArguments("--enable-usermedia-screen-capturing");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--allow-http-screen-capture");
        options.addArguments("--auto-select-desktop-capture-source="
                + Config.get("screenShareAutoSelectSource", "Entire screen"));
    }

    private static void addScreenShareOptions(EdgeOptions options) {
        if (!Config.getBoolean("autoAllowScreenShare", true)) {
            return;
        }

        options.addArguments("--enable-usermedia-screen-capturing");
        options.addArguments("--use-fake-ui-for-media-stream");
        options.addArguments("--allow-http-screen-capture");
        options.addArguments("--auto-select-desktop-capture-source="
                + Config.get("screenShareAutoSelectSource", "Entire screen"));
    }
}
