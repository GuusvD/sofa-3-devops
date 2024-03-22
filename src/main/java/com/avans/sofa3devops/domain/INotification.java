package com.avans.sofa3devops.domain;

public interface INotification {
    void accept(INotificationVisitor visitor);

    void sendMessage();
}
