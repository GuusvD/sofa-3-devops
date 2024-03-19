package com.avans.sofa3devops.domainServices.notificationStrategyPattern;

import com.avans.sofa3devops.domain.INotification;
import com.avans.sofa3devops.domain.INotificationVisitor;

import java.util.logging.Logger;

public class Slack implements INotification {
    private Logger logger;

    public Slack(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void accept(INotificationVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void sendMessage() {
        logger.info("Sent Slack message!");
    }
}
