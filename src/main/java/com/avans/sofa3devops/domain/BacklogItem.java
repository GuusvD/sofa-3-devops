package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import com.avans.sofa3devops.domainServices.backlogStatePattern.IBacklogItemState;
import com.avans.sofa3devops.domainServices.backlogStatePattern.ToDoState;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BacklogItem implements IItemComponent {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private UUID id;
    private String name;
    private List<Activity> activities;
    private User createdBy;
    private ISprint sprint;
    private User assignedTo;
    private List<Thread> threads;
    private IBacklogItemState state;
    private Boolean finished;

    public BacklogItem(String name, User createdBy) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.activities = new ArrayList<>();
        this.createdBy = createdBy;
        this.threads = new ArrayList<>();
        this.state = new ToDoState(this, new NotificationService(new NotificationExecutor()));
        this.finished = false;
    }

    // Composite
    public void getAllStories() {
        logger.info(this.getStory());
        for (var activity : this.activities) {
            logger.info(activity.getStory());
        }
    }

    public void setSprint(ISprint sprint){
        if(canEdit()) {
            this.sprint = sprint;
        }
    }

    public void addActivity(Activity activity) {
        if(canEdit()) {
            this.activities.add(activity);
            activity.setSprint(this.sprint);
        }
    }

    public void removeActivity(Activity activity) {
        if (canEdit()) {
            this.activities.remove(activity);
        }
    }

    public boolean containsActivity(Activity activity) {
        return this.activities.contains(activity);
    }

    public List<Activity> getActivities() {
        return this.activities;
    }

    // Composite Methods Start
    @Override
    public String getStory() {
        return getId() + ": " + getName();
    }

    public IBacklogItemState getState() {
        return state;
    }

    @Override
    public User getAssignedTo() {return assignedTo;}

    @Override
    public void setAssignedTo(User assignedTo) {
        if(canEdit()) {
            this.assignedTo = assignedTo;
        }
    }

    public boolean canEdit() {
        return sprint == null || (this.sprint.getState() instanceof CreatedState && this.state instanceof ToDoState);
    }

    @Override
    public boolean getFinished() {
        return this.finished;
    }

    @Override
    public void setFinished() {
        if (this.getState() instanceof DoneState) {
            boolean checkAll = true;
            if (!activities.isEmpty()) {
                for (var activity : activities) {
                    if (!activity.getFinished()) {
                        checkAll = false;
                        break;
                    }
                }
            }

            if (checkAll) {
                this.finished = true;
            }
        }
    }

    @Override
    public void removeFromSprints(List<ISprint> sprints) {
        if(canEdit()) {
            for (var sprint : sprints) {
                sprint.removeBacklogItem(this);
            }
        }
    }
    // Composite Methods End

    // State Methods Start
    public void setState(IBacklogItemState state) {
        this.state = state;
    }

    public void toDoState() throws InvalidStateException {
        this.state.toDoState();
    }

    public void doingState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState) {
            this.state.doingState();
        }
    }

    public void readyForTestingState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState) {
            this.state.readyForTestingState();
        }
    }

    public void testingState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState) {
            this.state.testingState();
        }
    }

    public void testedState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState) {
            this.state.testedState();
        }
    }

    public void doneState() throws InvalidStateException {
        if(this.sprint.getState() instanceof InProgressState) {
            this.state.doneState();
        }
    }
    // State Methods End

    // General methods
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
  
    public void setName(String name) {
        if(canEdit()) {
            this.name = name;
        }
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public List<Thread> getThreads() {
        return this.threads;
    }

    public void addThread(Thread thread) {
        if (!this.threads.contains(thread) && !finished) {
            this.threads.add(thread);
            thread.setBacklogItem(this);
        }
    }

    public void removeThread(Thread thread) {
        if (!finished) {
            this.threads.remove(thread);
        }
    }


}
