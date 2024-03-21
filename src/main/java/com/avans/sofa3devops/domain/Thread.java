package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;

import java.util.*;

public class Thread extends Observable {
    private UUID id;
    private String title;
    private String body;
    private List<Message> messages;
    private BacklogItem backlogItem;
    private final User createdBy;
    private final Date created;

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
        if(canEdit()) {
            messages.add(newMessage);
            newMessage.setThread(this);

            setChanged();
            notifyObservers();
        }
    }
    public void removeMessage(Message message) {
        if(canEdit()) {
            this.messages.remove(message);
        }
    }

    public String getTitle() {return title;}
    public String getBody() {return body;}
    public BacklogItem getBacklogItem() {return backlogItem;}

    public boolean canEdit() {
        return !this.backlogItem.getFinished();
    }


}
