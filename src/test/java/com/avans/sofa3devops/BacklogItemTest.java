package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BacklogItemTest {

    private User createdByUser;
    private ISprint sprint;
    @BeforeEach
    void setup() throws Exception {
        ISprintFactory factory = new SprintFactory();
        sprint = factory.createRegularSprint(1,new Date(),new Date(),createdByUser);
        createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
    }

    @Test
    void givenBacklogItemThatIsNotInASprintWhenCanEditIsCalledThenReturnValueEqualsTrue(){
        BacklogItem item = new BacklogItem("Item",createdByUser);



        assertTrue(item.canEdit());
    }
    @Test
    void givenBacklogItemInToDoStateThatIsInASprintInCreatedStateWhenCanEditIsCalledThenReturnValueEqualsFalse() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Item",createdByUser);
        sprint.addBacklogItem(item);

        assertTrue(item.canEdit());
    }
    @Test
    void givenBacklogItemInToDoStateThatIsInASprintNotInCreatedStateWhenCanEditIsCalledThenReturnValueEqualsFalse() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Item",createdByUser);
        sprint.addBacklogItem(item);
        sprint.inProgress();

        assertFalse(item.canEdit());
    }
    @Test
    void givenBacklogItemNotInToDoStateThatIsInASprintNotInCreatedStateWhenCanEditIsCalledThenReturnValueEqualsFalse() throws InvalidStateException {
        BacklogItem item = new BacklogItem("Item",createdByUser);
        sprint.addBacklogItem(item);
        sprint.addBacklogItem(item);
        sprint.inProgress();
        item.doingState();

        assertFalse(item.canEdit());
    }


    @Test
    void givenBacklogItemWithThreadWhenThreadIsAddedThenListHasValueOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title","Body",createdByUser);

        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(1);
        assertThat(thread.getBacklogItem()).isEqualTo(item);
    }

    @Test
    void givenBacklogItemWhenThreadIsAddedTwiceThenListHasValueOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title","Body",createdByUser);

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
        Thread thread = new Thread("Title","Body",createdByUser);

        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(0);
    }

    @Test
    void givenFinishedBacklogItemWithThreadWhenRemoveThreadIsCalledThenThreadListEqualsZero() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title","Body",createdByUser);
        item.addThread(thread);
        item.setState(new DoneState());
        item.setFinished();

        item.removeThread(thread);

        assertThat(item.getThreads()).hasSize(1);

    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenRemoveThreadIsCalledThenThreadListEqualsOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title","Body",createdByUser);
        item.addThread(thread);
        item.setState(new DoneState());

        item.removeThread(thread);

        assertThat(item.getThreads()).hasSize(0);
    }

}