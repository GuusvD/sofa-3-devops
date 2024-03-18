package com.avans.sofa3devops.domainServices.threadVisitorPattern;

import com.avans.sofa3devops.domain.INotificationVisitor;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Email;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Slack;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Sms;

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
