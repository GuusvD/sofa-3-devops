package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.backlogstatepattern.DoneState;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprintFactory;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.SprintFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BacklogItemTest {

    private User createdByUser;
    private ISprint sprint;

    @BeforeEach
    void setup() throws Exception {
        ISprintFactory factory = new SprintFactory();
        sprint = factory.createRegularSprint(1, new Date(), new Date(), createdByUser);
        createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
    }

    @Test
    void givenBacklogItemThatIsNotInASprintWhenCanEditIsCalledThenReturnValueEqualsTrue() {
        BacklogItem item = new BacklogItem("Item", createdByUser);


        assertTrue(item.canEdit());
    }

    @Test
    void givenBacklogItemInToDoStateThatIsInASprintInCreatedStateWhenCanEditIsCalledThenReturnValueEqualsFalse() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Item", createdByUser);
        sprint.addBacklogItem(item);

        assertTrue(item.canEdit());
    }

    @Test
    void givenBacklogItemInToDoStateThatIsInASprintNotInCreatedStateWhenCanEditIsCalledThenReturnValueEqualsFalse() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Item", createdByUser);
        sprint.addBacklogItem(item);
        sprint.inProgress();

        assertFalse(item.canEdit());
    }

    @Test
    void givenBacklogItemNotInToDoStateThatIsInASprintNotInCreatedStateWhenCanEditIsCalledThenReturnValueEqualsFalse() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Item", createdByUser);
        sprint.addBacklogItem(item);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        assertFalse(item.canEdit());
    }


    @Test
    void givenBacklogItemWithThreadWhenThreadIsAddedThenListHasValueOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title", "Body", createdByUser);

        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(1);
        assertThat(thread.getBacklogItem()).isEqualTo(item);
    }

    @Test
    void givenBacklogItemWhenThreadIsAddedTwiceThenListHasValueOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title", "Body", createdByUser);

        item.addThread(thread);
        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(1);
        assertThat(thread.getBacklogItem()).isEqualTo(item);
    }

    @Test
    void givenFinishedBackLogItemWhenThreadIsAddedThenItemIsNotAdded() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        item.setState(new DoneState());
        item.setFinished();
        Thread thread = new Thread("Title", "Body", createdByUser);

        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(0);
    }

    @Test
    void givenFinishedBacklogItemWithThreadWhenRemoveThreadIsCalledThenThreadListEqualsOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title", "Body", createdByUser);
        item.addThread(thread);
        item.setState(new DoneState());
        item.setFinished();

        item.removeThread(thread);

        assertThat(item.getThreads()).hasSize(1);
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenRemoveThreadIsCalledThenThreadListEqualsZero() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title", "Body", createdByUser);
        item.addThread(thread);
        item.setState(new DoneState());

        item.removeThread(thread);

        assertThat(item.getThreads()).hasSize(0);
    }
}