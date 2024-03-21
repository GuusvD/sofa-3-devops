package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Email;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Slack;
import com.avans.sofa3devops.domainServices.notificationStrategyPattern.Sms;

public interface INotificationVisitor {
    void visit(Email email);
    void visit(Slack slack);
    void visit(Sms sms);
}
