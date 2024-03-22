package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.backlogstatepattern.DoneState;
import com.avans.sofa3devops.domainservices.backlogstatepattern.IBacklogItemState;
import com.avans.sofa3devops.domainservices.backlogstatepattern.ToDoState;
import com.avans.sofa3devops.domainservices.compositeinterfaces.IItemComponent;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.sprintstatepattern.CreatedState;
import com.avans.sofa3devops.domainservices.sprintstatepattern.InProgressState;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class BacklogItem implements IItemComponent {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final UUID id;
    private String name;
    private List<Activity> activities;
    private User createdBy;
    private ISprint sprint;
    private User assignedTo;
    private List<Thread> threads;
    private IBacklogItemState state;
    private boolean finished;

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

    public void setSprint(ISprint sprint) {
        if (canEdit()) {
            this.sprint = sprint;
        }
    }

    public void addActivity(Activity activity) {
        if (canEdit()) {
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

    // State Methods Start
    public void setState(IBacklogItemState state) {
        this.state = state;
    }

    @Override
    public User getAssignedTo() {
        return assignedTo;
    }

    @Override
    public void setAssignedTo(User assignedTo) {
        if (canEdit()) {
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
    // Composite Methods End

    public void removeFromSprints(List<ISprint> sprints) {
        if (canEdit()) {
            for (var s : sprints) {
                s.removeBacklogItem(this);
            }
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

    // General methods
    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (canEdit()) {
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
        if (!this.threads.contains(thread) && !this.finished) {
            this.threads.add(thread);
            thread.setBacklogItem(this);
        }
    }

    public void removeThread(Thread thread) {
        if (!this.finished) {
            this.threads.remove(thread);
        }
    }


}
