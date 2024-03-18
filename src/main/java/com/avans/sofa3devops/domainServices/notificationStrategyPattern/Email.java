package com.avans.sofa3devops.domainServices.notificationStrategyPattern;

import com.avans.sofa3devops.domain.INotification;
import com.avans.sofa3devops.domain.INotificationVisitor;

import java.util.logging.Logger;

public class Email implements INotification {

    @Override
    public void accept(INotificationVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void sendMessage() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("Sent email message!");
    }
}
