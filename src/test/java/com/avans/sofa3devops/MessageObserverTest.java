package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domain.Thread;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Observer;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MessageObserverTest {
    User user = new User(UUID.randomUUID(), "John Doe", "j.doe@gmail.com", "Password1234");

    // Message entity and observable test
    @Test
    void givenNewMessageWhenAddingItToOtherMessageThenCallUpdateObserverMethodAndAddMessageToOtherMessage() {
        BacklogItem item = new BacklogItem("BacklogItem", user, user);
        Thread thread = new Thread(UUID.randomUUID(), "A question?", "Please help me!!", new ArrayList<>(), item, user, new Date());
        Message oldMessage = new Message(UUID.randomUUID(), "Old message", user, new ArrayList<>(), new Date());
        Message response = new Message(UUID.randomUUID(), "New message", user, new ArrayList<>(), new Date());
        Observer mock = mock(Observer.class);
        oldMessage.addObserver(mock);
        thread.addMessage(oldMessage);

        oldMessage.addMessage(response);

        verify(mock).update(eq(oldMessage), eq(null));
        assertThat(oldMessage.getResponses().get(0)).isEqualTo(response);
    }
}
