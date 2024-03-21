package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;
import java.util.*;

public class Message extends Observable {
    private UUID id;
    private String body;
    private User creator;
    private List<Message> responses;
    private Thread thread;
    private Date sent;

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
            this.thread=thread;
        }
    }

    public void addMessage(Message newMessage) {
        if(this.thread.canEdit()) {
            responses.add(newMessage);
            newMessage.setThread(this.thread);

            setChanged();
            notifyObservers();
        }
    }

    public void setBody(String body) {
        if(this.thread.canEdit()) {
            this.body=body;
        }
    }

    public void removeMessage(Message message) {
        if(this.thread.canEdit()) {
            this.responses.remove(message);
        }
    }
    public String getBody() {return this.body;}
}
