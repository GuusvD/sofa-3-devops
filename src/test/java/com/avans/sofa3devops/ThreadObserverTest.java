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

public class ThreadObserverTest {
    User user = new User(UUID.randomUUID(), "John Doe", "j.doe@gmail.com", "Password1234");

    @Test
    void givenNewMessageWhenAddingItToThreadThenCallUpdateObserverMethodAndAddMessageToThread() {
        BacklogItem item = new BacklogItem("BacklogItem", user, user);
        Thread thread = new Thread(UUID.randomUUID(), "A question?", "Please help me!!", new ArrayList<>(), item, user, new Date());
        Message newMessage = new Message(UUID.randomUUID(), "Your answer!", user, new ArrayList<>(), new Date());
        Observer mock = mock(Observer.class);
        thread.addObserver(mock);

        thread.addMessage(newMessage);

        verify(mock).update(eq(thread), eq(null));
        assertThat(thread.getMessages().get(0)).isEqualTo(newMessage);
    }
}
