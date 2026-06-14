# Selenium Java Automation Framework

This is a small Selenium WebDriver automation framework built with Java, Maven, and TestNG.
It currently contains smoke tests for `https://evo.dev.theysaid.io/`.

Session Recording

The full assessment session recording (including manual exploration, AI usage, and automation work) is available here.
The link is set to view access enabled, as required:

[Session Recording (Google Drive)](https://drive.google.com/file/d/1GCWB6tFrJagJDOtnFOcJQ4lWQSZcCJpD/view?usp=drive_link)

## What Is Included

- Maven project setup with Selenium, TestNG, and WebDriverManager
- Config-driven browser, URL, timeout, and headless settings
- Base test lifecycle for WebDriver setup and teardown
- Page Object Model structure
- Screenshot capture on failed tests
- TestNG suite file
- Login smoke test using the Page Object Model
- Teach AI source setup and AI user test project creation smoke test
- Newest project publish flow and published survey launch smoke test

## Project Structure

```text
selenium-java-framework/
  pom.xml
  testng.xml
  src/test/java/com/example/framework/
    config/
    driver/
    pages/
    tests/
    utils/
  src/test/resources/config.properties
```

## Prerequisites

- Java 17 or newer
- Maven 3.9 or newer
- Chrome, Edge, or Firefox installed

## Run Tests

From this folder:

```bash
mvn clean test
```

Run in headless mode:

```bash
mvn clean test -Dheadless=true
```

Run with another browser:

```bash
mvn clean test -Dbrowser=edge
mvn clean test -Dbrowser=firefox
```

Run against another application URL:

```bash
mvn clean test -DbaseUrl=https://your-application-url.com
```

Run with different login credentials:

```bash
mvn clean test -DloginEmail=user@example.com -DloginPassword=yourPassword
```

The publish survey test starts a screen-sharing flow. Chrome and Edge are configured by default
to auto-select the screen-share source using `autoAllowScreenShare=true` and
`screenShareAutoSelectSource=Entire screen`.

## Configuration

Defaults are stored in:

```text
src/test/resources/config.properties
```

Any property can be overridden from the command line with `-D`.

Common properties:

```properties
baseUrl=https://evo.dev.theysaid.io/
browser=chrome
headless=false
implicitWaitSeconds=0
explicitWaitSeconds=10
pageLoadTimeoutSeconds=30
screenshotDir=test-output/screenshots
loginEmail=<set locally or pass with -DloginEmail=...>
loginPassword=<set locally or pass with -DloginPassword=...>
projectsUrl=https://evo.dev.theysaid.io/projects
teachAiUrl=https://evo.dev.theysaid.io/home/teach-ai
teachAiLinkUrl=http://otbokhly.com/
teachAiUploadFilePath=test-files/Amsterdam_Travel_Plan_With_Links.pdf
sourceUploadTimeoutSeconds=30
projectCreationTimeoutSeconds=45
projectGoal=Testing the negative scenarios for our meals marketplace.
projectsTableTimeoutSeconds=30
publishModalTimeoutSeconds=30
publishSuccessTitle=Your project has been published!
clipboardTimeoutSeconds=10
surveyHeadingTimeoutSeconds=30
surveyInstructionTimeoutSeconds=30
surveyInstructionText=Instruction will be provided in the task bar on the right side ..
autoAllowScreenShare=true
screenShareAutoSelectSource=Entire screen
```

## Add More Tests

1. Create page objects under `src/test/java/com/example/framework/pages`.
2. Create test classes under `src/test/java/com/example/framework/tests`.
3. Extend `BaseTest`.
4. Add the new test class to `testng.xml` if you want it included in the suite.
