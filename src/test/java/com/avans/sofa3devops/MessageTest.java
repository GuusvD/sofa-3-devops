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

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageTest {
    private BacklogItem item;
    private Thread thread;
    private Message message;
    private Message response;
    @BeforeEach
    void setup() throws Exception {
        User createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
        ISprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, new Date(), new Date(), createdByUser);
        item = new BacklogItem("Item", createdByUser);
        thread = new Thread("Title","Body", createdByUser);
        message = new Message("Body", createdByUser);
        response = new Message("Response", createdByUser);
    }
    @Test
    void givenFinishedBacklogItemWithThreadWhenResponseMessageIsAddedThenListEqualsZero() throws InvalidStateException {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);
        item.setFinished();

        message.addMessage(response);

        assertThat(message.getResponses()).hasSize(0);
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenResponseMessageIsAddedThenListEqualsOne() {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);
        message.addMessage(response);

        assertThat(message.getResponses()).hasSize(1);
    }

    @Test
    void givenFinishedBacklogItemWithThreadWhenResponseMessageIsRemovedThenListEqualsOne() {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);
        message.addMessage(response);
        item.setFinished();

        message.removeMessage(response);

        assertThat(message.getResponses()).hasSize(1);
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenResponseMessageIsRemovedThenListEqualsZero() {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);

        message.removeMessage(response);

        assertThat(message.getResponses()).hasSize(0);
    }

    @Test
    void givenFinishedBacklogItemWithThreadWhenSetBodyOnMessageIsCalledThenBodyDoesNotEqualNewBody() {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);
        message.addMessage(response);
        item.setFinished();

        message.setBody("New Body");

        assertThat(message.getBody()).isEqualTo("Body");
    }

    @Test
    void givenUnfinishedBacklogItemWithThreadWhenSetBodyOnMessageIsCalledThenBodyDoesNotEqualNewBody() {
        item.addThread(thread);
        item.setState(new DoneState());
        thread.addMessage(message);

        message.setBody("New Body");

        assertThat(message.getBody()).isEqualTo("New Body");
    }
}
