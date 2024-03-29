package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Message;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Email;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Slack;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Sms;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Observable;
import java.util.Observer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ThreadObserverTest {
    User user = new User("John Doe", "j.doe@gmail.com", "Password1234");

    // Thread entity and observable test
    @Test
    void givenNewMessageWhenAddingItToThreadThenCallUpdateObserverMethodAndAddMessageToThread() {
        BacklogItem item = new BacklogItem("BacklogItem", user);
        Thread thread = new Thread("A question?", "Please help me!!", user);
        item.addThread(thread);
        Message newMessage = new Message("Your answer!", user);
        Observer mock = mock(Observer.class);
        thread.addObserver(mock);

        thread.addMessage(newMessage);

        verify(mock).update(eq(thread), eq(null));
        assertThat(thread.getMessages().get(0)).isEqualTo(newMessage);
    }

    // NotificationService test
    @Test
    void givenObservableWhenCallingUpdateThenCallExecutorForAllNotificationTypes() {
        Observable observable = mock(Observable.class);
        Object arg = new Object();
        NotificationExecutor executorMock = mock(NotificationExecutor.class);
        NotificationService notificationService = new NotificationService(executorMock);

        notificationService.update(observable, arg);

        verify(executorMock, times(1)).visit(any(Sms.class));
        verify(executorMock, times(1)).visit(any(Email.class));
        verify(executorMock, times(1)).visit(any(Slack.class));
    }

    // Executor tests
    @Test
    void givenEmailWhenCallingVisitThenCallSendMessageAndGenerateALog() {
        Email emailMock = mock(Email.class);
        NotificationExecutor executor = new NotificationExecutor();

        executor.visit(emailMock);

        verify(emailMock, times(1)).sendMessage();
    }

    @Test
    void givenSmsWhenCallingVisitThenCallSendMessageAndGenerateALog() {
        Sms smsMock = mock(Sms.class);
        NotificationExecutor executor = new NotificationExecutor();

        executor.visit(smsMock);

        verify(smsMock, times(1)).sendMessage();
    }

    @Test
    void givenSlackWhenCallingVisitThenCallSendMessageAndGenerateALog() {
        Slack slackMock = mock(Slack.class);
        NotificationExecutor executor = new NotificationExecutor();

        executor.visit(slackMock);

        verify(slackMock, times(1)).sendMessage();
    }
}
