package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.backlogstatepattern.DoneState;
import com.avans.sofa3devops.domainservices.backlogstatepattern.IBacklogItemState;
import com.avans.sofa3devops.domainservices.backlogstatepattern.ToDoState;
import com.avans.sofa3devops.domainservices.compositeinterfaces.IItemComponent;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.sprintstatepattern.InProgressState;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;

import java.util.UUID;

public class Activity implements IItemComponent {
    private final UUID id;
    private final String name;
    private final User createdBy;
    private Boolean finished;
    private User assignedTo;
    private ISprint sprint;

    private IBacklogItemState state;

    public Activity(String name, User createdBy) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdBy = createdBy;
        this.state = new ToDoState(this, new NotificationService(new NotificationExecutor()));
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

    // State Methods Start
    public void setState(IBacklogItemState state) {
        this.state = state;
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

    public void toDoState() throws InvalidStateException {
        this.state.toDoState();
    }

    public void doingState() throws InvalidStateException {
        if (this.sprint.getState() instanceof InProgressState) {
            this.state.doingState();
        }
    }

    public void readyForTestingState() throws InvalidStateException {
        if (this.sprint.getState() instanceof InProgressState) {
            this.state.readyForTestingState();
        }
    }

    public void testingState() throws InvalidStateException {
        if (this.sprint.getState() instanceof InProgressState) {
            this.state.testingState();
        }
    }

    public void testedState() throws InvalidStateException {
        if (this.sprint.getState() instanceof InProgressState) {
            this.state.testedState();
        }
    }

    public void doneState() throws InvalidStateException {
        if (this.sprint.getState() instanceof InProgressState) {
            this.state.doneState();
        }
    }
    // State Methods End

    // general methods
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setSprint(ISprint sprint) {
        if (this.sprint == null) {
            this.sprint = sprint;
        }
    }
}
