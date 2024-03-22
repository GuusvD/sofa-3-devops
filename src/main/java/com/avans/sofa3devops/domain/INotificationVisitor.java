package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.notificationstrategypattern.Email;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Slack;
import com.avans.sofa3devops.domainservices.notificationstrategypattern.Sms;

public interface INotificationVisitor {
    void visit(Email email);

    void visit(Slack slack);

    void visit(Sms sms);
}
