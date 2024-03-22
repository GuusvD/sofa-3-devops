package com.avans.sofa3devops.domainservices.threadvisitorpattern;

import com.avans.sofa3devops.domain.INotificationVisitor;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Email;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Slack;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Sms;

public class NotificationExecutor implements INotificationVisitor {

    @Override
    public void visit(Email email) {
        email.sendMessage();
    }

    @Override
    public void visit(Slack slack) {
        slack.sendMessage();
    }

    @Override
    public void visit(Sms sms) {
        sms.sendMessage();
    }
}
