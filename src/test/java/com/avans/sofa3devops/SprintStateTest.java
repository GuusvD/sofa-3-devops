package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Document;
import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ReviewSprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ClosedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.FinishedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
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
    private final Date pastDate = new Date(System.currentTimeMillis() - 86400000);
    private final Date startDate = new Date(pastDate.getTime() - 7 * 24 * 60 * 60 * 1000);

    User user = new User("John Doe", "j.doe@gmail.com", "Password1234");

    @Test
    void givenRegularSprintWithCreatedStateWhenSwitchingStateThenSwitchToInProgressState() throws Exception {
        SprintFactory factory = new SprintFactory();

        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);

        sprint.inProgress();

        assertThat(sprint.getState()).isInstanceOf(InProgressState.class);
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateThenSwitchToFinishedState() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();

        sprint.finished();

        assertThat(sprint.getState()).isInstanceOf(FinishedState.class);
    }

    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateThenSwitchToClosedState() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();

        sprint.closed();

        assertThat(sprint.getState()).isInstanceOf(ClosedState.class);
    }

    @Test
    void givenReviewSprintWithFinishedStateAndWithDocumentAndWithReviewWhenSwitchingStateToClosedThenSwitchToClosedState() throws Exception {
        SprintFactory factory = new SprintFactory();
        ReviewSprint sprint = (ReviewSprint) factory.createReviewSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();
        sprint.setDocument(new Document());
        sprint.setReviewed();

        sprint.closed();

        assertThat(sprint.getState()).isInstanceOf(ClosedState.class);
    }

    // Incorrect state switching
    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateToInProgressThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::inProgress);
        assertEquals("Cannot transition to 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToFinishedThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();
        sprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::finished);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToInProgressThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();
        sprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::inProgress);
        assertEquals("Cannot transition to 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateToClosedThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::closed);
        assertEquals("Cannot transition to 'closed' state!", exception.getMessage());
    }

    @Test
    void givenReviewSprintWithFinishedStateAndWithoutReviewWhenSwitchingStateToClosedThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::closed);
        assertEquals("Cannot transition to 'closed' state! Sprint is not reviewed!", exception.getMessage());
    }

    // Same state switching
    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateToInProgressThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::inProgress);
        assertEquals("Already in 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithInProgressStateAndDateNotPassedWhenSwitchingStateToInProgressThenTrowException() throws Exception {
        Date currentDate = new Date();
        long millisecondsToAdd = 7 * 24 * 60 * 60 * 1000; // Adding 7 days
        Date futureDate = new Date(currentDate.getTime() + millisecondsToAdd);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, futureDate, user);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::finished);
        assertEquals("Cannot transition to 'finished' state! Sprint hasn't reached its end date!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateToFinishedThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::finished);
        assertEquals("Already in 'finished' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToClosedThenThrowException() throws Exception {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();
        sprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::closed);
        assertEquals("Already in 'closed' state!", exception.getMessage());
    }

    @Test
    void givenReviewSprintWithFinishedStateWhenSwitchingToClosedStateWithoutDocument() throws Exception {
        SprintFactory factory = new SprintFactory();
        ReviewSprint sprint = (ReviewSprint) factory.createReviewSprint(1, startDate, pastDate, user);
        sprint.inProgress();
        sprint.finished();
        sprint.setReviewed();
        sprint.closed();

        assertInstanceOf(FinishedState.class, sprint.getState());
    }

    @Test
    void givenReviewSprintWithFinishedStateWhenSwitchingToClosedStateWithDocument() throws Exception {
        SprintFactory factory = new SprintFactory();
        ReviewSprint sprint = (ReviewSprint) factory.createReviewSprint(1, startDate, pastDate, user);
        Document document = new Document();
        sprint.inProgress();
        sprint.finished();
        sprint.setDocument(document);
        sprint.setReviewed();
        sprint.closed();

        assertInstanceOf(ClosedState.class, sprint.getState());
    }

    // Notifications sending
    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingToClosedStateThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
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
        ISprint sprint = factory.createRegularSprint(1, startDate, pastDate, user);
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
}
