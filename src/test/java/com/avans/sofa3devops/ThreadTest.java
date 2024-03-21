package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Message;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Date;

public class ThreadTest {
    private ISprintFactory factory;
    private ISprint sprint;
    private User createdByUser;
    private BacklogItem item;
    private Thread thread;
    private Message message;
    @BeforeEach
    void setup() throws Exception {
        createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
        factory = new SprintFactory();
        sprint = factory.createRegularSprint(1,new Date(),new Date(),createdByUser);
        item = new BacklogItem("Item",createdByUser);
        thread = new Thread("Title","Body",createdByUser);
        message = new Message("Body",createdByUser);
    }
    @Test
    void givenFinishedBacklogItemWithThreadWhenMessageIsAddedThenListEqualsZero() throws InvalidStateException {
        item.addThread(thread);
        item.setState(new DoneState(item));
        item.setFinished();

        thread.addMessage(message);

        assertThat(thread.getMessages()).hasSize(0);
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenMessageIsAddedThenListEqualsOne() {
        item.addThread(thread);
        item.setState(new DoneState(item));

        thread.addMessage(message);
        thread.removeMessage(message);

        assertThat(thread.getMessages()).hasSize(0);
    }

    @Test
    void givenFinishedBacklogItemWithThreadWhenMessageIsRemovedThenListEqualsOne() {
        item.addThread(thread);
        item.setState(new DoneState(item));
        thread.addMessage(message);
        item.setFinished();

        thread.removeMessage(message);

        assertThat(thread.getMessages()).hasSize(1);
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenMessageIsRemovedThenListEqualsZero() {
        item.addThread(thread);
        item.setState(new DoneState(item));
        thread.addMessage(message);

        thread.removeMessage(message);

        assertThat(thread.getMessages()).hasSize(0);
    }



}
