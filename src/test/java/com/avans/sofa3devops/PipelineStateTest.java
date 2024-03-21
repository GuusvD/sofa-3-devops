package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domain.command.*;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.*;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PipelineStateTest {
    private ISprint regularSprint;
    private ISprint reviewSprint;
    private User creator;
    private ISprintFactory factory;
    private Date endDate;
    private Date startDate;

    @BeforeEach
    void setup() throws Exception {
        endDate = new Date(System.currentTimeMillis() - 86400000);
        startDate = new Date(endDate.getTime() - 7 * 24 * 60 * 60 * 1000);
        creator = new User("John Doe", "j.doe@gmail.com", "Password1234");
        factory = new SprintFactory();
        regularSprint = factory.createRegularSprint(1, startDate, endDate, creator);
        reviewSprint = factory.createReviewSprint(1, startDate, endDate, creator);

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

    // Correct state switching
    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateThenSwitchToExecutedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);

        pipeline.executedState();

        assertThat(pipeline.getState()).isInstanceOf(ExecutedState.class);
    }

    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateThenSwitchToCancelledState() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateThenSwitchToFinishedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();

        pipeline.finishedState();

        assertThat(pipeline.getState()).isInstanceOf(FinishedState.class);
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateThenSwitchToFailedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();

        pipeline.failedState();

        assertThat(pipeline.getState()).isInstanceOf(FailedState.class);
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateThenSwitchToExecutedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();

        pipeline.executedState();

        assertThat(pipeline.getState()).isInstanceOf(ExecutedState.class);
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateThenSwitchToCancelledState() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
    }

    // Incorrect state switching
    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Cannot transition to 'failed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateToCancelledStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::cancelledState);
        assertEquals("Cannot transition to 'cancelled' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToExecutedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::executedState);
        assertEquals("Cannot transition to 'executed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Cannot transition to 'failed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToCancelledStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::cancelledState);
        assertEquals("Cannot transition to 'cancelled' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToExecutedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::executedState);
        assertEquals("Cannot transition to 'executed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Cannot transition to 'failed' state!", exception.getMessage());
    }

    // Same state switching
    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateToExecutedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::executedState);
        assertEquals("Already in 'executed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Already in 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Already in 'failed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToCancelledStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::cancelledState);
        assertEquals("Already in 'cancelled' state!", exception.getMessage());
    }

    // Notifications sending
    @Test
    void givenPipelineWithExecutedStateWhenExecutingPipelineFailsThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.setState(new InitialState(pipeline, mock));
        pipeline.executedState();

        pipeline.failedState();

        assertThat(pipeline.getState()).isInstanceOf(FailedState.class);
        verify(mock).update(any(ExecutedState.class), eq(null));
    }

    @Test
    void givenPipelineWithExecutedStateWhenExecutingPipelineFinishesThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.setState(new InitialState(pipeline, mock));
        pipeline.executedState();

        pipeline.finishedState();

        assertThat(pipeline.getState()).isInstanceOf(FinishedState.class);
        verify(mock).update(any(ExecutedState.class), eq(null));
    }

    @Test
    void givenPipelineWithFailedStateWhenCancellingReleaseThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.setState(new InitialState(pipeline, mock));
        pipeline.executedState();
        pipeline.failedState();

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
        verify(mock).update(any(FailedState.class), eq(null));
    }

    @Test
    void givenPipelineWithInitialStateWhenCancellingReleaseThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.setState(new InitialState(pipeline, mock));

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
        verify(mock).update(any(InitialState.class), eq(null));
    }

    // Execute pipeline
    @Test
    void givenRegularSprintWithFinishedStateWhenExecutingPipelineThenSuccessfullyCompletePipeline() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        Pipeline pipeline = new Pipeline("Test", regularSprint);
        pipeline.setState(new InitialState(pipeline, mock));
        regularSprint.inProgress();
        regularSprint.finished();

        regularSprint.executePipeline();

        assertThat(regularSprint.getPipeline().getState()).isInstanceOf(FinishedState.class);
        assertThat(regularSprint.getReleases().size()).isEqualTo(1);
    }

    @Test
    void givenRegularSprintWithFinishedStateAndNoCommandsWhenExecutingPipelineThenFailPipeline() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        ISprint sprint = factory.createRegularSprint(1, startDate, endDate, creator);
        Pipeline pipeline = new Pipeline("Test", sprint);
        pipeline.setState(new InitialState(pipeline, mock));
        sprint.inProgress();
        sprint.finished();

        sprint.executePipeline();

        assertThat(sprint.getPipeline().getState()).isInstanceOf(FailedState.class);
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenExecutingPipelineThenDoNotExecutePipeline() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        ISprint sprint = factory.createRegularSprint(1, startDate, endDate, creator);
        Pipeline pipeline = new Pipeline("Test", sprint);
        pipeline.setState(new InitialState(pipeline, mock));
        sprint.inProgress();

        sprint.executePipeline();

        assertThat(sprint.getState()).isInstanceOf(InProgressState.class);
        assertThat(sprint.getPipeline().getState()).isInstanceOf(InitialState.class);
    }

    @Test
    void givenReviewSprintWithFinishedStateWhenExecutingPipelineThenSuccessfullyCompletePipeline() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        Pipeline pipeline = new Pipeline("Test", reviewSprint);
        pipeline.setState(new InitialState(pipeline, mock));
        reviewSprint.inProgress();
        reviewSprint.finished();

        reviewSprint.executePipeline();

        assertThat(reviewSprint.getPipeline().getState()).isInstanceOf(FinishedState.class);
        assertThat(reviewSprint.getReleases().size()).isEqualTo(1);
    }

    @Test
    void givenReviewSprintWithFinishedStateAndNoCommandsWhenExecutingPipelineThenFailPipeline() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        ISprint sprint = factory.createReviewSprint(1, startDate, endDate, creator);
        Pipeline pipeline = new Pipeline("Test", sprint);
        pipeline.setState(new InitialState(pipeline, mock));
        sprint.inProgress();
        sprint.finished();

        sprint.executePipeline();

        assertThat(sprint.getPipeline().getState()).isInstanceOf(FailedState.class);
    }

    @Test
    void givenReviewSprintWithInProgressStateWhenExecutingPipelineThenDoNotExecutePipeline() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        ISprint sprint = factory.createReviewSprint(1, startDate, endDate, creator);
        Pipeline pipeline = new Pipeline("Test", sprint);
        pipeline.setState(new InitialState(pipeline, mock));
        sprint.inProgress();

        sprint.executePipeline();

        assertThat(sprint.getState()).isInstanceOf(InProgressState.class);
        assertThat(sprint.getPipeline().getState()).isInstanceOf(InitialState.class);
    }
}
