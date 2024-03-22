package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Message;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class MessageObserverTest {
    User user = new User("John Doe", "j.doe@gmail.com", "Password1234");

    // Message entity and observable test
    @Test
    void givenNewMessageWhenAddingItToOtherMessageThenCallUpdateObserverMethodAndAddMessageToOtherMessage() {
        BacklogItem item = new BacklogItem("BacklogItem", user);
        Thread thread = new Thread("A question?", "Please help me!!", user);
        item.addThread(thread);
        Message oldMessage = new Message("Old message", user);
        Message response = new Message("New message", user);
        Observer mock = mock(Observer.class);
        oldMessage.addObserver(mock);
        thread.addMessage(oldMessage);

        oldMessage.addMessage(response);

        verify(mock).update(eq(oldMessage), eq(null));
        assertThat(oldMessage.getResponses().get(0)).isEqualTo(response);
    }
}
