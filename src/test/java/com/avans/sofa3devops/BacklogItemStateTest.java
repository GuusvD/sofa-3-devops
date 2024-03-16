package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.backlogStatePattern.*;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BacklogItemStateTest {
    // Correct state switching
    User user = new User();

    @Test
    void givenBacklogItemWithToDoStateWhenSwitchingStateThenSwitchToDoingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());

        item.doingState();

        assertThat(item.getState()).isInstanceOf(DoingState.class);
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateThenSwitchToReadyForTestingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();

        item.readyForTestingState();

        assertThat(item.getState()).isInstanceOf(ReadyForTestingState.class);
    }

    @Test
    void givenBacklogItemWithDoingStateWhenSwitchingStateThenSwitchToToDoState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();

        item.toDoState();

        assertThat(item.getState()).isInstanceOf(ToDoState.class);
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateThenSwitchToDoingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();
        item.readyForTestingState();

        item.doingState();

        assertThat(item.getState()).isInstanceOf(DoingState.class);
    }

    @Test
    void givenBacklogItemWithReadyForTestingStateWhenSwitchingStateThenSwitchToTestingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();
        item.readyForTestingState();

        item.testingState();

        assertThat(item.getState()).isInstanceOf(TestingState.class);
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateThenSwitchToDoingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        item.doingState();

        assertThat(item.getState()).isInstanceOf(DoingState.class);
    }

    @Test
    void givenBacklogItemWithTestingStateWhenSwitchingStateThenSwitchToTestedState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();
        item.readyForTestingState();
        item.testingState();

        item.testedState();

        assertThat(item.getState()).isInstanceOf(TestedState.class);
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateThenSwitchToTestingState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        item.testingState();

        assertThat(item.getState()).isInstanceOf(TestingState.class);
    }

    @Test
    void givenBacklogItemWithTestedStateWhenSwitchingStateThenSwitchToDoneState() throws InvalidStateException {
        BacklogItem item = new BacklogItem(UUID.randomUUID(), new ArrayList<>(), user, user, new ArrayList<>());
        item.doingState();
        item.readyForTestingState();
        item.testingState();
        item.testedState();

        item.doneState();

        assertThat(item.getState()).isInstanceOf(DoneState.class);
    }

    // Incorrect state switching

}
