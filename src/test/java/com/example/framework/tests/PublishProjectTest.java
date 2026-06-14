package com.example.framework.tests;

import com.example.framework.config.Config;
import com.example.framework.pages.LoginPage;
import com.example.framework.pages.ProjectsPage;
import com.example.framework.pages.SurveyPage;
import com.example.framework.pages.TeachAiPage;
import java.nio.file.Path;
import org.testng.Assert;
import org.testng.annotations.Test;

public class PublishProjectTest extends BaseTest {

    @Test(description = "Verify user can publish the newest project and open the published survey link")
    public void shouldPublishNewestProjectAndOpenSurveyLink() {
        String projectsUrl = Config.get("projectsUrl", "https://evo.dev.theysaid.io/projects");
        String teachAiUrl = Config.get("teachAiUrl", "https://evo.dev.theysaid.io/home/teach-ai");
        String teachAiLinkUrl = Config.get("teachAiLinkUrl", "http://otbokhly.com/");
        String uploadFilePath = Config.get("teachAiUploadFilePath", "test-files/Amsterdam_Travel_Plan_With_Links.pdf");
        String projectGoal = Config.get(
                "projectGoal",
                "Testing the negative scenarios for our meals marketplace."
        );
        String publishSuccessTitle = Config.get("publishSuccessTitle", "Your project has been published!");
        String surveyInstructionText = Config.get(
                "surveyInstructionText",
                "Instruction will be provided in the task bar on the right side .."
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
                .ensureLinkAdded(teachAiLinkUrl)
                .ensureFileAdded(uploadFilePath);

        Assert.assertEquals(teachAiPage.addedUrlText(), teachAiLinkUrl);
        Assert.assertEquals(
                teachAiPage.addedFileNameText(),
                Path.of(uploadFilePath).getFileName().toString()
        );

        ProjectsPage projectsPage = new ProjectsPage(driver()).open(projectsUrl);
        int existingProjectCount = projectsPage.projectRowCount();

        projectsPage.createAiUserTestProject(projectGoal)
                .waitUntilDraftIsReady(projectsUrl)
                .open(projectsUrl)
                .waitUntilProjectRowCountGreaterThan(existingProjectCount)
                .publishNewestProject();

        Assert.assertEquals(projectsPage.publishedModalTitle(), publishSuccessTitle);

        String surveyLink = projectsPage.copyPublishedSurveyLink();
        Assert.assertTrue(surveyLink.startsWith("http"), "Copied survey link should be an HTTP URL.");

        SurveyPage surveyPage = new SurveyPage(driver())
                .openInNewTab(surveyLink)
                .waitUntilLandingHeadingVisible()
                .startSurvey()
                .waitUntilInstructionTextContains(surveyInstructionText);

        Assert.assertTrue(
                normalized(surveyPage.instructionText()).contains(normalized(surveyInstructionText)),
                "Survey instruction text should be visible after starting the survey."
        );
    }

    private String normalized(String value) {
        return value.replaceAll("\\s+", " ").trim();
    }
}
