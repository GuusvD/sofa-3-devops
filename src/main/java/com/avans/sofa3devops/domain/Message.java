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

    public void addMessage(Message newMessage) {
        responses.add(newMessage);

        setChanged();
        notifyObservers();
    }
}
