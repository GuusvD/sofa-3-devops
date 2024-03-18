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

public class NotificationService implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        NotificationExecutor executor = new NotificationExecutor();

        List<INotification> notifications = Arrays.asList(new Email(), new Slack(), new Sms());
        for (INotification notification : notifications) {
            notification.accept(executor);
        }
    }
}
