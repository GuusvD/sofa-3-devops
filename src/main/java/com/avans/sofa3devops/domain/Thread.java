package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;

import java.util.*;

public class Thread extends Observable {
    private final UUID id;
    private final String title;
    private final String body;
    private final User createdBy;
    private final Date created;
    private List<Message> messages;
    private BacklogItem backlogItem;

    public Thread(String title, String body, User createdBy) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.body = body;
        this.messages = new ArrayList<>();
        this.createdBy = createdBy;
        this.created = new Date();

        this.addObserver(new NotificationService(new NotificationExecutor()));
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message newMessage) {
        if (canEdit()) {
            newMessage.setThread(this);
            messages.add(newMessage);

            setChanged();
            notifyObservers();
        }
    }

    public void removeMessage(Message message) {
        if (canEdit()) {
            this.messages.remove(message);
        }
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public BacklogItem getBacklogItem() {
        return backlogItem;
    }

    public void setBacklogItem(BacklogItem item) {
        this.backlogItem = item;
    }

    public boolean canEdit() {
        return !this.backlogItem.getFinished();
    }

    public UUID getId() {
        return id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public Date getCreated() {
        return created;
    }
}
