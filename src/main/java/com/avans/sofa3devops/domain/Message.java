package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;

import java.util.*;

public class Message extends Observable {
    private final UUID id;
    private final User creator;
    private final Date sent;
    private String body;
    private List<Message> responses;
    private Thread thread;

    public Message(String body, User creator) {
        this.id = UUID.randomUUID();
        this.body = body;
        this.creator = creator;
        this.addObserver(new NotificationService(new NotificationExecutor()));
        this.responses = new ArrayList<>();
        this.sent = new Date();
    }

    public List<Message> getResponses() {
        return responses;
    }

    public void setThread(Thread thread) {
        if (this.thread == null) {
            this.thread = thread;
        }
    }

    public void addMessage(Message newMessage) {
        if (this.thread.canEdit()) {
            responses.add(newMessage);
            newMessage.setThread(this.thread);

            setChanged();
            notifyObservers();
        }
    }

    public void removeMessage(Message message) {
        if (this.thread.canEdit()) {
            this.responses.remove(message);
        }
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        if (this.thread.canEdit()) {
            this.body = body;
        }
    }

    public UUID getId() {
        return id;
    }

    public User getCreator() {
        return creator;
    }

    public Date getSent() {
        return sent;
    }
}
