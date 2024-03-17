package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import com.avans.sofa3devops.domainServices.backlogStatePattern.IBacklogItemState;
import com.avans.sofa3devops.domainServices.backlogStatePattern.ToDoState;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Activity implements IItemComponent {
    private UUID id;
    private String name;
    private Boolean finished;
    private List<Thread> threads;
    private User createdBy;
    private User assignedTo;

    private IBacklogItemState state;

    public Activity(String name, User createdBy, User assignedTo) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.threads = new ArrayList<>();
        this.state = new ToDoState(this);
        this.finished = false;
    }

    // Composite Methods Start
    @Override
    public String getStory() {
        return this.getId() + ": " + this.getName();
    }
    @Override
    public IBacklogItemState getState() {
        return this.state;
    }
    @Override
    public User getAssignedTo() {
        return this.assignedTo;
    }
    @Override
    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }
    @Override
    public boolean getFinished() {
        return this.finished;
    }
    @Override
    public void setFinished() {
        if (state instanceof DoneState) {
            this.finished = true;
            // to-do: close threads
        }
    }

    @Override
    public void addThread(Thread thread) {
        this.threads.add(thread);
    }
    // Composite Methods End

    // State Methods Start
    public void setState(IBacklogItemState state) {
        this.state = state;
    }
    public void toDoState() throws InvalidStateException {this.state.toDoState();}
    public void doingState() throws InvalidStateException {this.state.doingState();}
    public void readyForTestingState() throws InvalidStateException {this.state.readyForTestingState();}
    public void testingState() throws InvalidStateException {this.state.testingState();}
    public void testedState() throws InvalidStateException {this.state.testedState();}
    public void doneState() throws InvalidStateException {this.state.doneState();}
    // State Methods End

    // general methods
    public UUID getId() {return id;}
    public String getName() {return name;}
    public User getCreated() {return createdBy;}
}
