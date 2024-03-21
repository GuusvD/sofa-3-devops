package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Project;
import com.avans.sofa3devops.domainServices.gitStrategyPattern.GitHub;
import com.avans.sofa3devops.domainServices.gitStrategyPattern.GitLab;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Pdf;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.logging.Logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class GitStrategyTest {
    // GitHub tests
    @Test
    void givenGitHubWhenPullThenPullGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitHub gitHub = new GitHub(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitHub);

        project.pull();

        verify(loggerMock).info("GitHub: Pulling code!");
    }

    @Test
    void givenGitHubWhenPushThenPushGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitHub gitHub = new GitHub(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitHub);

        project.push();

        verify(loggerMock).info("GitHub: Pushing code!");
    }

    @Test
    void givenGitHubWhenCommitThenCommitGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitHub gitHub = new GitHub(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitHub);

        project.commit();

        verify(loggerMock).info("GitHub: Committing code!");
    }

    @Test
    void givenGitHubWhenStatusThenStatusGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitHub gitHub = new GitHub(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitHub);

        project.status();

        verify(loggerMock).info("GitHub: Retrieving status!");
    }

    @Test
    void givenGitHubWhenCheckoutThenCheckoutGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitHub gitHub = new GitHub(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitHub);

        project.checkout();

        verify(loggerMock).info("GitHub: Checking out!");
    }

    @Test
    void givenGitHubWhenStashThenStashGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitHub gitHub = new GitHub(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitHub);

        project.stash();

        verify(loggerMock).info("GitHub: Stashing code!");
    }

    // GitLab tests
    @Test
    void givenGitLabWhenPullThenPullGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitLab gitLab = new GitLab(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitLab);

        project.pull();

        verify(loggerMock).info("GitLab: Pulling code!");
    }

    @Test
    void givenGitLabWhenPushThenPushGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitLab gitLab = new GitLab(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitLab);

        project.push();

        verify(loggerMock).info("GitLab: Pushing code!");
    }

    @Test
    void givenGitLabWhenCommitThenCommitGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitLab gitLab = new GitLab(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitLab);

        project.commit();

        verify(loggerMock).info("GitLab: Committing code!");
    }

    @Test
    void givenGitLabWhenStatusThenStatusGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitLab gitLab = new GitLab(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitLab);

        project.status();

        verify(loggerMock).info("GitLab: Retrieving status!");
    }

    @Test
    void givenGitLabWhenCheckoutThenCheckoutGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitLab gitLab = new GitLab(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitLab);

        project.checkout();

        verify(loggerMock).info("GitLab: Checking out!");
    }

    @Test
    void givenGitLabWhenStashThenStashGitHub() {
        Logger loggerMock = mock(Logger.class);
        GitLab gitLab = new GitLab(loggerMock);
        Project project = new Project("Test", Arrays.asList(new Pdf()), gitLab);

        project.stash();

        verify(loggerMock).info("GitLab: Stashing code!");
    }
}
