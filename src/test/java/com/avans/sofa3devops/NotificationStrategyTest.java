package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.INotificationVisitor;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Email;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Slack;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Sms;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

import static org.mockito.Mockito.*;

@SpringBootTest
public class NotificationStrategyTest {
    @Test
    void givenSlackWhenCallingVisitThenCallSendMessageAndGenerateALog() {
        Logger logger = mock(Logger.class);
        Slack slack = new Slack(logger);

        slack.sendMessage();

        verify(logger).info("Sent Slack message!");
    }

    @Test
    void givenEmailWhenCallingVisitThenCallSendMessageAndGenerateALog() {
        Logger logger = mock(Logger.class);
        Email email = new Email(logger);

        email.sendMessage();

        verify(logger).info("Sent email message!");
    }

    @Test
    void givenSmsWhenCallingVisitThenCallSendMessageAndGenerateALog() {
        Logger logger = mock(Logger.class);
        Sms sms = new Sms(logger);

        sms.sendMessage();

        verify(logger).info("Sent SMS message!");
    }

    @Test
    void givenSlackWhenCallingAcceptThenCallVisitOnVisitor() {
        INotificationVisitor visitor = mock(NotificationExecutor.class);
        Slack slack = new Slack(Logger.getLogger(Slack.class.getName()));

        slack.accept(visitor);

        verify(visitor).visit(slack);
    }

    @Test
    void givenEmailWhenCallingAcceptThenCallVisitOnVisitor() {
        INotificationVisitor visitor = mock(NotificationExecutor.class);
        Email email = new Email(Logger.getLogger(Email.class.getName()));

        email.accept(visitor);

        verify(visitor).visit(email);
    }

    @Test
    void givenSmsWhenCallingAcceptThenCallVisitOnVisitor() {
        INotificationVisitor visitor = mock(NotificationExecutor.class);
        Sms sms = new Sms(Logger.getLogger(Sms.class.getName()));

        sms.accept(visitor);

        verify(visitor).visit(sms);
    }
}
