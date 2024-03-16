package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.backlogStatePattern.IBacklogItemState;
import com.avans.sofa3devops.domainServices.backlogStatePattern.ToDoState;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

import java.util.List;
import java.util.UUID;

public class BacklogItem {
    private UUID id;
    private List<Activity> activities;
    private User createdBy;
    private User assignedTo;
    private List<Thread> threads;
    private IBacklogItemState state;

    public BacklogItem(UUID id, List<Activity> activities, User createdBy, User assignedTo, List<Thread> threads) {
        this.id = id;
        this.activities = activities;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.threads = threads;
        this.state = new ToDoState(this);
    }

    public IBacklogItemState getState() {
        return state;
    }

    public void setState(IBacklogItemState state) {
        this.state = state;
    }

    public void toDoState() throws InvalidStateException {
        this.state.toDoState();
    }

    public void doingState() throws InvalidStateException {
        this.state.doingState();
    }

    public void readyForTestingState() throws InvalidStateException {
        this.state.readyForTestingState();
    }

    public void testingState() throws InvalidStateException {
        this.state.testingState();
    }

    public void testedState() throws InvalidStateException {
        this.state.testedState();
    }

    public void doneState() throws InvalidStateException {
        this.state.doneState();
    }

    // General methods

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }
}
