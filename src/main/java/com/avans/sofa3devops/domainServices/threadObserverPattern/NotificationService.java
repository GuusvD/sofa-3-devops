package com.avans.sofa3devops.domainServices.threadObserverPattern;

import com.avans.sofa3devops.domain.INotification;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Email;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Slack;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Sms;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class NotificationService implements Observer {
    private NotificationExecutor notificationExecutor;

    public NotificationService(NotificationExecutor notificationExecutor) {
        this.notificationExecutor = notificationExecutor;
    }

    @Override
    public void update(Observable o, Object arg) {
        List<INotification> notifications = Arrays.asList(new Email(Logger.getLogger(Email.class.getName())), new Slack(Logger.getLogger(Slack.class.getName())), new Sms(Logger.getLogger(Sms.class.getName())));
        for (INotification notification : notifications) {
            notification.accept(notificationExecutor);
        }
    }
}
