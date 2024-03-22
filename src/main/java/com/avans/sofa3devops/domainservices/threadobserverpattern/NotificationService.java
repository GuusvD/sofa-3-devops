package com.avans.sofa3devops.domainservices.threadobserverpattern;

import com.avans.sofa3devops.domain.INotification;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Email;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Slack;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Sms;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

public class NotificationService implements Observer {
    private final NotificationExecutor notificationExecutor;

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
