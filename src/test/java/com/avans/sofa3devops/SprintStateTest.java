package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Document;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ReviewSprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ClosedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.FinishedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SprintStateTest {
    // Correct state switching
    User user = new User();
    @Test
    void givenRegularSprintWithCreatedStateWhenSwitchingStateThenSwitchToInProgressState() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();

        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);

        sprint.inProgress();

        assertThat(sprint.getState()).isInstanceOf(InProgressState.class);
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateThenSwitchToFinishedState() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();

        sprint.finished();

        assertThat(sprint.getState()).isInstanceOf(FinishedState.class);
    }

    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateThenSwitchToClosedState() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();

        sprint.closed();

        assertThat(sprint.getState()).isInstanceOf(ClosedState.class);
    }

    @Test
    void givenReviewSprintWithFinishedStateAndWithDocumentAndWithReviewWhenSwitchingStateToClosedThenSwitchToClosedState() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ReviewSprint sprint = (ReviewSprint) factory.createReviewSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();
        sprint.setDocument(new Document());
        sprint.setReviewed();

        sprint.closed();

        assertThat(sprint.getState()).isInstanceOf(ClosedState.class);
    }

    // Incorrect state switching
    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateToInProgressThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::inProgress);
        assertEquals("Cannot transition to 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToFinishedThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();
        sprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::finished);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToInProgressThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();
        sprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::inProgress);
        assertEquals("Cannot transition to 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateToClosedThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::closed);
        assertEquals("Cannot transition to 'closed' state!", exception.getMessage());
    }

    @Test
    void givenReviewSprintWithFinishedStateAndWithoutReviewWhenSwitchingStateToClosedThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::closed);
        assertEquals("Cannot transition to 'closed' state!", exception.getMessage());
    }

    // Same state switching
    @Test
    void givenRegularSprintWithInProgressStateWhenSwitchingStateToInProgressThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::inProgress);
        assertEquals("Already in 'in progress' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithFinishedStateWhenSwitchingStateToFinishedThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::finished);
        assertEquals("Already in 'finished' state!", exception.getMessage());
    }

    @Test
    void givenRegularSprintWithClosedStateWhenSwitchingStateToClosedThenThrowException() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ISprint sprint =  factory.createRegularSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();
        sprint.closed();

        InvalidStateException exception = assertThrows(InvalidStateException.class, sprint::closed);
        assertEquals("Already in 'closed' state!", exception.getMessage());
    }

    @Test
    void givenReviewSprintWithFinishedStateWhenSwitchingToClosedStateWithoutDocument() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ReviewSprint sprint = (ReviewSprint) factory.createReviewSprint(1,new Date(), new Date(), user);
        sprint.inProgress();
        sprint.finished();
        sprint.setReviewed();
        sprint.closed();

        assertInstanceOf(FinishedState.class, sprint.getState());

    }

    @Test
    void givenReviewSprintWhithFinishedStateWhenSwitchingToClosedStateWithDocument() throws InvalidStateException {
        SprintFactory factory = new SprintFactory();
        ReviewSprint sprint = (ReviewSprint) factory.createReviewSprint(1,new Date(), new Date(), user);
        Document document = new Document();
        sprint.inProgress();
        sprint.finished();
        sprint.setDocument(document);
        sprint.setReviewed();
        sprint.closed();

        assertInstanceOf(ClosedState.class,sprint.getState());

    }

}
