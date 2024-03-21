package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import com.avans.sofa3devops.domainServices.backlogStatePattern.IBacklogItemState;
import com.avans.sofa3devops.domainServices.backlogStatePattern.ToDoState;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Activity implements IItemComponent {
    private UUID id;
    private String name;
    private Boolean finished;
    private User createdBy;
    private User assignedTo;
    private ISprint sprint;

    private IBacklogItemState state;

    public Activity(String name, User createdBy) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdBy = createdBy;
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
        }
    }

    @Override
    public void removeFromSprints(List<ISprint> sprints) {

    }

    // State Methods Start
    public void setState(IBacklogItemState state) {this.state = state;}
    public void toDoState() throws InvalidStateException {this.state.toDoState();}
    public void doingState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState){
            this.state.doingState();
        }
    }
    public void readyForTestingState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState){
            this.state.readyForTestingState();
        }
    }
    public void testingState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState){
            this.state.testingState();
        }
    }
    public void testedState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState){
            this.state.testedState();
        }
    }
    public void doneState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState){
            this.state.doneState();
        }
    }
    // State Methods End

    // general methods
    public UUID getId() {return id;}
    public String getName() {return name;}
    public User getCreated() {return createdBy;}
    public void setSprint(ISprint sprint) {
        if(this.sprint == null) {
            this.sprint = sprint;
        }
    }
}
