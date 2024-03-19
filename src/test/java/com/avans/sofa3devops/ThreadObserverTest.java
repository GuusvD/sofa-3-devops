package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Email;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Slack;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Sms;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class ThreadObserverTest {
    User user = new User(UUID.randomUUID(), "John Doe", "j.doe@gmail.com", "Password1234");

    // Thread entity and observable test
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
