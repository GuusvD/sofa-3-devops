package com.avans.sofa3devops.domain;

import java.util.*;

public class Thread extends Observable {
    private UUID id;
    private String title;
    private String body;
    private List<Message> messages;
    private BacklogItem backlogItem;
    private User createdBy;
    private Date created;

    public Thread(String title, String body, BacklogItem backlogItem, User createdBy) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.body = body;
        this.messages = new ArrayList<>();
        this.backlogItem = backlogItem;
        this.createdBy = createdBy;
        this.created = new Date();
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message newMessage) {
        messages.add(newMessage);

        setChanged();
        notifyObservers();
    }

    public String getTitle() {return title;}
    public String getBody() {return body;}
    public BacklogItem getBacklogItem() {return backlogItem;}
}
