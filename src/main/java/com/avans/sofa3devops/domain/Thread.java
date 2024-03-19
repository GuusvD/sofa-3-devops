package com.avans.sofa3devops.domain;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.UUID;

public class Thread extends Observable {
    private UUID id;
    private String title;
    private String body;
    private List<Message> messages;
    private BacklogItem backlogItem;
    private User createdBy;
    private Date created;

    public Thread(UUID id, String title, String body, List<Message> messages, BacklogItem backlogItem, User createdBy, Date created) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.messages = messages;
        this.backlogItem = backlogItem;
        this.createdBy = createdBy;
        this.created = created;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message newMessage) {
        messages.add(newMessage);

        setChanged();
        notifyObservers();
    }
}
