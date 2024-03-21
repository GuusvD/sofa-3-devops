package com.avans.sofa3devops.domain;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

public class Message extends Observable {
    private UUID id;
    private String body;
    private User creator;
    private List<Message> responses;
    private Thread thread;
    private Date sent;

    public Message(UUID id, String body, User creator, List<Message> responses, Date sent) {
        this.id = id;
        this.body = body;
        this.creator = creator;
        this.responses = responses;
        this.sent = sent;
    }

    public List<Message> getResponses() {
        return responses;
    }

    public void setThread(Thread thread) {
        if (this.thread != null) {
            this.thread=thread;
        }
    }

    public void addMessage(Message newMessage) {
        if(this.thread.canEdit()) {
            responses.add(newMessage);

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
}
