package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Message;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.backlogstatepattern.DoneState;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ThreadTest {
    private BacklogItem item;
    private Thread thread;
    private Message message;

    @BeforeEach
    void setup() throws Exception {
        User createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
        item = new BacklogItem("Item", createdByUser);
        thread = new Thread("Title", "Body", createdByUser);
        message = new Message("Body", createdByUser);
    }

    @Test
    void givenFinishedBacklogItemWithThreadWhenMessageIsAddedThenListEqualsZero() throws InvalidStateException {
        item.addThread(thread);
        item.setState(new DoneState());
        item.setFinished();

        thread.addMessage(message);

        assertThat(thread.getMessages()).hasSize(0);
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenMessageIsAddedThenListEqualsOne() {
        item.addThread(thread);
        item.setState(new DoneState());

        thread.addMessage(message);
        thread.removeMessage(message);

        assertThat(thread.getMessages()).hasSize(0);
    }

    @Test
    void givenFinishedBacklogItemWithThreadWhenMessageIsRemovedThenListEqualsOne() {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);
        item.setFinished();

        thread.removeMessage(message);

        assertThat(thread.getMessages()).hasSize(1);
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenMessageIsRemovedThenListEqualsZero() {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);

        thread.removeMessage(message);

        assertThat(thread.getMessages()).hasSize(0);
    }
}
