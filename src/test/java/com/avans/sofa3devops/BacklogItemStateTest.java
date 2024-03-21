package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.backlogStatePattern.*;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class BacklogItemStateTest {
    // Correct state switching
    private User user;
    private ISprintFactory factory;
    private ISprint sprint;

    @BeforeEach
    void setUp() throws Exception {
       user =  new User("John Doe", "j.doe@gmail.com", "Password1234");
       factory = new SprintFactory();
       sprint = factory.createRegularSprint(1,new Date(),new Date(),user);
    }

    @Test
    void givenBacklogItemWithToDoStateWhenSwitchingStateThenSwitchToDoingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        assertThat(item.getState()).isInstanceOf(DoingState.class);
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateThenSwitchToReadyForTestingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        item.readyForTestingState();

        assertThat(item.getState()).isInstanceOf(ReadyForTestingState.class);
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateThenSwitchToToDoState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        item.toDoState();

        assertThat(item.getState()).isInstanceOf(ToDoState.class);
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateThenSwitchToToDoState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();

        item.toDoState();

        assertThat(item.getState()).isInstanceOf(ToDoState.class);
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateThenSwitchToTestingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();

        item.testingState();

        assertThat(item.getState()).isInstanceOf(TestingState.class);
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateThenSwitchToToDoState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        item.toDoState();

        assertThat(item.getState()).isInstanceOf(ToDoState.class);
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateThenSwitchToDoingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        item.doingState();

        assertThat(item.getState()).isInstanceOf(DoingState.class);
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateThenSwitchToTestedState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        item.testedState();

        assertThat(item.getState()).isInstanceOf(TestedState.class);
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateThenSwitchToReadyForTestingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        item.readyForTestingState();

        assertThat(item.getState()).isInstanceOf(ReadyForTestingState.class);
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateThenSwitchToTestingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        item.testingState();

        assertThat(item.getState()).isInstanceOf(TestingState.class);
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateThenSwitchToDoneState() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        item.doneState();

        assertThat(item.getState()).isInstanceOf(DoneState.class);
    }

    // Incorrect state switching
    @Test
    void givenBacklogItemWithToDoStateWhenSwitchingStateToReadyForTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::readyForTestingState);
        assertEquals("Cannot transition to 'ready for testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithToDoStateWhenSwitchingStateToTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testingState);
        assertEquals("Cannot transition to 'testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithToDoStateWhenSwitchingStateToTestedThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testedState);
        assertEquals("Cannot transition to 'tested' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithToDoStateWhenSwitchingStateToDoneThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doneState);
        assertEquals("Cannot transition to 'done' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateToTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testingState);
        assertEquals("Cannot transition to 'testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateToTestedThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testedState);
        assertEquals("Cannot transition to 'tested' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateToDoneThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doneState);
        assertEquals("Cannot transition to 'done' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateToDoingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doingState);
        assertEquals("Cannot transition to 'doing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateToTestedThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testedState);
        assertEquals("Cannot transition to 'tested' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateToDoneThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doneState);
        assertEquals("Cannot transition to 'done' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateToReadyForTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::readyForTestingState);
        assertEquals("Cannot transition to 'ready for testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateToDoneThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doneState);
        assertEquals("Cannot transition to 'done' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateToToDoThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::toDoState);
        assertEquals("Cannot transition to 'to do' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateToDoingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doingState);
        assertEquals("Cannot transition to 'doing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoneStateWhenSwitchingStateToToDoThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();
        item.doneState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::toDoState);
        assertEquals("Cannot transition to 'to do' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoneStateWhenSwitchingStateToDoingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();
        item.doneState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doingState);
        assertEquals("Cannot transition to 'doing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoneStateWhenSwitchingStateToReadyForTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();
        item.doneState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::readyForTestingState);
        assertEquals("Cannot transition to 'ready for testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoneStateWhenSwitchingStateToTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();
        item.doneState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testingState);
        assertEquals("Cannot transition to 'testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoneStateWhenSwitchingStateToTestedThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();
        item.doneState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testedState);
        assertEquals("Cannot transition to 'tested' state!", exception.getMessage());
    }

    // Same state switching
    @Test
    void givenBacklogItemWithToDoStateWhenSwitchingStateToToDoThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::toDoState);
        assertEquals("Already in 'to do' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateToDoingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doingState);
        assertEquals("Already in 'doing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateToReadyForTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::readyForTestingState);
        assertEquals("Already in 'ready for testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateToTestingThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testingState);
        assertEquals("Already in 'testing' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateToTestedThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::testedState);
        assertEquals("Already in 'tested' state!", exception.getMessage());
    }

    @Test
    void givenBacklogItemWithDoneStateWhenSwitchingStateToDoneThenThrowException() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Backlog", user);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();
        item.doneState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, item::doneState);
        assertEquals("Already in 'done' state!", exception.getMessage());
    }

    // Notifications sending
    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingToReadyForTestingStateThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        BacklogItem item = new BacklogItem("Backlog", user);
        item.setState(new ToDoState(item, mock));
        item.doingState();

        item.readyForTestingState();

        assertThat(item.getState()).isInstanceOf(ReadyForTestingState.class);
        verify(mock).update(any(DoingState.class), eq(null));
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingToToDoStateThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        BacklogItem item = new BacklogItem("Backlog", user);
        item.setState(new ToDoState(item, mock));
        item.doingState();
        item.readyForTestingState();

        item.toDoState();

        assertThat(item.getState()).isInstanceOf(ToDoState.class);
        verify(mock).update(any(ReadyForTestingState.class), eq(null));
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingToReadyForTestingStateThenSendNotification() throws Exception {
        NotificationService mock = mock(NotificationService.class);
        BacklogItem item = new BacklogItem("Backlog", user);
        item.setState(new ToDoState(item, mock));
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        item.readyForTestingState();

        assertThat(item.getState()).isInstanceOf(ReadyForTestingState.class);
        verify(mock).update(any(TestedState.class), eq(null));
    }
}
