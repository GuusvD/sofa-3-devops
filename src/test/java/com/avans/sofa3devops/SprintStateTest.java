package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.Document;
import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domain.command.*;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprintFactory;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ReviewSprint;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.SprintFactory;
import com.avans.sofa3devops.domainservices.sprintstatepattern.ClosedState;
import com.avans.sofa3devops.domainservices.sprintstatepattern.CreatedState;
import com.avans.sofa3devops.domainservices.sprintstatepattern.FinishedState;
import com.avans.sofa3devops.domainservices.sprintstatepattern.InProgressState;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class SprintStateTest {
    // Correct state switching
    private Date endDate;
    private Date startDate;
    private User user;
    private ISprintFactory factory;
    private ISprint regularSprint;
    private ISprint reviewSprint;

    @BeforeEach
    void setup() throws Exception {
        endDate = new Date(System.currentTimeMillis() - 86400000);
        startDate = new Date(endDate.getTime() - 7 * 24 * 60 * 60 * 1000);
        user = new User("John Doe", "j.doe@gmail.com", "Password1234");
        factory = new SprintFactory();
        regularSprint = factory.createRegularSprint(1, startDate, endDate, user);
        reviewSprint = factory.createReviewSprint(1, startDate, endDate, user);

        Command commandAnalysis = new DotnetAnalyseCommand();
        Command commandBuild = new DotnetBuildCommand();
        Command commandDeploy = new DotnetPublishCommand();
        Command commandPackage = new DotnetRestoreCommand();
        Command commandSource = new GitCloneCommand();
        Command commandTest = new DotnetTestCommand();
        Command commandUtility = new DotnetCleanCommand();

        regularSprint.addCommandToAction(commandAnalysis);
        regularSprint.addCommandToAction(commandBuild);
        regularSprint.addCommandToAction(commandDeploy);
        regularSprint.addCommandToAction(commandPackage);
        regularSprint.addCommandToAction(commandSource);
        regularSprint.addCommandToAction(commandTest);
        regularSprint.addCommandToAction(commandUtility);

        reviewSprint.addCommandToAction(commandAnalysis);
        reviewSprint.addCommandToAction(commandBuild);
        reviewSprint.addCommandToAction(commandDeploy);
        reviewSprint.addCommandToAction(commandPackage);
        reviewSprint.addCommandToAction(commandSource);
        reviewSprint.addCommandToAction(commandTest);
        reviewSprint.addCommandToAction(commandUtility);
    }

    @Test
    void givenRegularSprintWithCreatedStateWhenSwitchingStateThenSwitchToInProgressState() throws Exception {

        regularSprint.inProgress();

        assertThat(regularSprint.getState()).isInstanceOf(InProgressState.class);
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateThenSwitchToFinishedState() throws Exception {
        regularSprint.inProgress();

        regularSprint.finished();

        assertThat(regularSprint.getState()).isInstanceOf(FinishedState.class);
    }

    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateThenSwitchToClosedState() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();
        regularSprint.executePipeline();

        regularSprint.closed();

        assertThat(regularSprint.getState()).isInstanceOf(ClosedState.class);
    }

    @Test
    void givenReviewSprintWithFinishedStateAndWithDocumentAndWithReviewWhenSwitchingStateToClosedThenSwitchToClosedState() throws Exception {
        ReviewSprint reviewSprint = (ReviewSprint) factory.createReviewSprint(1, startDate, endDate, user);
        reviewSprint.inProgress();
        reviewSprint.finished();
        reviewSprint.executePipeline();
        reviewSprint.setDocument(new Document());
        reviewSprint.setReviewed();

        reviewSprint.closed();

        assertThat(reviewSprint.getState()).isInstanceOf(ClosedState.class);
    }

    // Incorrect state switching
    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateToInProgressThenThrowException() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();


        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::inProgress);
        assertEquals("Cannot transition to 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToFinishedThenThrowException() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();
        regularSprint.executePipeline();

        regularSprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::finished);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToInProgressThenThrowException() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();
        regularSprint.executePipeline();

        regularSprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::inProgress);
        assertEquals("Cannot transition to 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateToClosedThenThrowException() throws Exception {
        regularSprint.inProgress();
        regularSprint.executePipeline();


        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::closed);
        assertEquals("Cannot transition to 'closed' state! Pipeline is not cancelled/finished!", exception.getMessage());
    }

    @Test
    void givenReviewSprintWithFinishedStateAndWithoutReviewWhenSwitchingStateToClosedThenThrowException() throws Exception {
        ReviewSprint reviewSprint = (ReviewSprint) factory.createReviewSprint(1, startDate, endDate, user);
        reviewSprint.inProgress();
        reviewSprint.finished();
        reviewSprint.executePipeline();


        InvalidStateException exception = assertThrows(InvalidStateException.class, reviewSprint::closed);
        assertEquals("Cannot transition to 'closed' state! Sprint is not reviewed or pipeline is not cancelled/finished!", exception.getMessage());
    }

    // Same state switching
    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateToInProgressThenThrowException() throws Exception {
        regularSprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::inProgress);
        assertEquals("Already in 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithInProgressStateAndDateNotPassedWhenSwitchingStateToInProgressThenTrowException() throws Exception {

        Date currentDate = new Date();
        long millisecondsToAdd = 7 * 24 * 60 * 60 * 1000; // Adding 7 days
        Date futureDate = new Date(currentDate.getTime() + millisecondsToAdd);
        ISprint sprint = factory.createRegularSprint(1, startDate, futureDate, user);
        sprint.inProgress();
        sprint.executePipeline();


        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::finished);
        assertEquals("Cannot transition to 'finished' state! Sprint hasn't reached its end date!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateToFinishedThenThrowException() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();

        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::finished);
        assertEquals("Already in 'finished' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToClosedThenThrowException() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();
        regularSprint.executePipeline();

        regularSprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::closed);
        assertEquals("Already in 'closed' state!", exception.getMessage());
    }

    @Test
    void givenReviewSprintWithFinishedStateWhenSwitchingToClosedStateWithoutDocument() throws Exception {
        ReviewSprint reviewSprint = (ReviewSprint) factory.createReviewSprint(1, startDate, endDate, user);
        reviewSprint.inProgress();
        reviewSprint.finished();
        reviewSprint.executePipeline();

        reviewSprint.setReviewed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, regularSprint::closed);
        assertEquals("Cannot transition to 'closed' state! Pipeline is not cancelled/finished!", exception.getMessage());
    }

    @Test
    void givenReviewSprintWithFinishedStateWhenSwitchingToClosedStateWithDocument() throws Exception {
        ReviewSprint reviewSprint = (ReviewSprint) factory.createReviewSprint(1, startDate, endDate, user);
        Document document = new Document();
        reviewSprint.inProgress();
        reviewSprint.finished();
        reviewSprint.executePipeline();

        reviewSprint.setDocument(document);
        reviewSprint.setReviewed();
        reviewSprint.closed();

        assertInstanceOf(ClosedState.class, reviewSprint.getState());
    }

    // Notifications sending
    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingToClosedStateThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, endDate, user);
        Pipeline pipeline = sprint.getPipeline();
        sprint.setState(new CreatedState(sprint, mock));
        sprint.inProgress();
        sprint.finished();
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        sprint.closed();

        assertThat(sprint.getState()).isInstanceOf(ClosedState.class);
        verify(mock).update(any(FinishedState.class), eq(null));
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingToFinishedStateThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, endDate, user);
        sprint.setState(new CreatedState(sprint, mock));
        sprint.inProgress();

        sprint.finished();

        assertThat(sprint.getState()).isInstanceOf(FinishedState.class);
        verify(mock).update(any(InProgressState.class), eq(null));
    }

    @Test
    void givenRegularSprintWithInProgressStateAndNotReachedEndDateWhenSwitchingToFinishedStateThenThrowException() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        SprintFactory factory = new SprintFactory();
        LocalDateTime localDateTime = LocalDateTime.now().plusHours(1);
        Date wrongDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        ISprint sprint = factory.createRegularSprint(1, startDate, wrongDate, user);
        sprint.setState(new CreatedState(sprint, mock));
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::finished);
        assertEquals("Cannot transition to 'finished' state! Sprint hasn't reached its end date!", exception.getMessage());
        assertThat(sprint.getState()).isInstanceOf(InProgressState.class);
    }

    // Set document
    @Test
    void givenRegularSprintWithFinishedStateAndPipelineWithFinishedStateWhenSettingDocumentThenSetDocument() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();
        regularSprint.executePipeline();
        Document document = new Document();

        regularSprint.setDocument(document);

        assertThat(regularSprint.getDocument()).isEqualTo(document);
    }

    @Test
    void givenRegularSprintWithFinishedStateAndPipelineWithExecutedStateWhenSettingDocumentThenDoNotSetDocument() throws Exception {
        regularSprint.inProgress();
        regularSprint.finished();
        regularSprint.getPipeline().executedState();
        Document document = new Document();

        regularSprint.setDocument(document);

        assertThat(regularSprint.getDocument()).isEqualTo(null);
    }

    @Test
    void givenRegularSprintWithInProgressStateAndPipelineWithFinishedStateWhenSettingDocumentThenDoNotSetDocument() throws Exception {
        regularSprint.inProgress();
        regularSprint.executePipeline();
        Document document = new Document();

        regularSprint.setDocument(document);

        assertThat(regularSprint.getDocument()).isEqualTo(null);
    }

    //
    @Test
    void givenReviewSprintWithFinishedStateAndPipelineWithFinishedStateWhenSettingDocumentThenSetDocument() throws Exception {
        reviewSprint.inProgress();
        reviewSprint.finished();
        reviewSprint.executePipeline();
        Document document = new Document();

        reviewSprint.setDocument(document);

        assertThat(reviewSprint.getDocument()).isEqualTo(document);
    }

    @Test
    void givenReviewSprintWithFinishedStateAndPipelineWithExecutedStateWhenSettingDocumentThenDoNotSetDocument() throws Exception {
        reviewSprint.inProgress();
        reviewSprint.finished();
        reviewSprint.getPipeline().executedState();
        Document document = new Document();

        reviewSprint.setDocument(document);

        assertThat(reviewSprint.getDocument()).isEqualTo(null);
    }

    @Test
    void givenReviewSprintWithInProgressStateAndPipelineWithFinishedStateWhenSettingDocumentThenDoNotSetDocument() throws Exception {
        reviewSprint.inProgress();
        reviewSprint.executePipeline();
        Document document = new Document();

        reviewSprint.setDocument(document);

        assertThat(reviewSprint.getDocument()).isEqualTo(null);
    }
}
