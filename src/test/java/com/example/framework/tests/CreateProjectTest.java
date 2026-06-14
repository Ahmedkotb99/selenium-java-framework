package com.example.framework.tests;

import com.example.framework.config.Config;
import com.example.framework.pages.LoginPage;
import com.example.framework.pages.ProjectsPage;
import com.example.framework.pages.TeachAiPage;
import java.nio.file.Path;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CreateProjectTest extends BaseTest {

    @Test(description = "Verify user can add Teach AI sources and create a drafted AI user test project")
    public void shouldCreateAiUserTestProjectAfterAddingTeachAiSources() {
        String projectsUrl = Config.get("projectsUrl", "https://evo.dev.theysaid.io/projects");
        String teachAiUrl = Config.get("teachAiUrl", "https://evo.dev.theysaid.io/home/teach-ai");
        String teachAiLinkUrl = Config.get("teachAiLinkUrl", "http://otbokhly.com/");
        String uploadFilePath = Config.get("teachAiUploadFilePath", "test-files/Amsterdam_Travel_Plan_With_Links.pdf");
        String projectGoal = Config.get(
                "projectGoal",
                "Testing the negative scenarios for our meals marketplace."
        );

        new LoginPage(driver())
                .login(
                        Config.get("loginEmail", ""),
                        Config.get("loginPassword", "")
                )
                .waitUntilLoaded(projectsUrl);

        TeachAiPage teachAiPage = new TeachAiPage(driver())
                .openFromSideNav(teachAiUrl)
                .waitUntilReady()
                .addLink(teachAiLinkUrl)
                .addFile(uploadFilePath);

        Assert.assertEquals(teachAiPage.addedUrlText(), teachAiLinkUrl);
        Assert.assertEquals(
                teachAiPage.addedFileNameText(),
                Path.of(uploadFilePath).getFileName().toString()
        );

        ProjectsPage projectsPage = new ProjectsPage(driver())
                .open(projectsUrl)
                .createAiUserTestProject(projectGoal)
                .waitUntilPublishButtonVisible();

        Assert.assertTrue(projectsPage.isPublishButtonVisible(), "Publish button should be visible.");
    }
}
