package com.example.framework.tests;

import com.example.framework.config.Config;
import com.example.framework.pages.LoginPage;
import com.example.framework.pages.ProjectsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(description = "Verify user can login and is redirected to the projects page")
    public void shouldLoginSuccessfully() {
        String expectedProjectsUrl = Config.get("projectsUrl", "https://evo.dev.theysaid.io/projects");

        ProjectsPage projectsPage = new LoginPage(driver())
                .login(
                        Config.get("loginEmail", ""),
                        Config.get("loginPassword", "")
                );

        projectsPage.waitUntilLoaded(expectedProjectsUrl);

        Assert.assertEquals(projectsPage.currentUrl(), expectedProjectsUrl);
    }
}
