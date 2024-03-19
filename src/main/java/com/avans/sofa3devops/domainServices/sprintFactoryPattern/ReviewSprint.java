package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ISprintState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReviewSprint implements ISprint {
    private ISprintState state;
    private int number;
    private Date start;
    private Date end;
    private List<BacklogItem> backlog;
    private List<User> developers;
    private Document document;
    private List<Release> releases;
    private boolean reviewed;

    public ReviewSprint(int number, Date start, Date end, User user) {
        this.state = new CreatedState(this);
        this.number = number;
        this.start = start;
        this.end = end;
        this.backlog = new ArrayList<>();
        this.developers = new ArrayList<>();
        this.developers.add(user);
        this.reviewed = false;
    }

    @Override
    public void setState(ISprintState state) {
        this.state = state;
    }

    @Override
    public ISprintState getState() {
        return this.state;
    }

    @Override
    public void inProgress() throws InvalidStateException {
        this.state.inProgressState();
    }

    @Override
    public void finished() throws InvalidStateException {
        this.state.finishedState();
    }

    @Override
    public void closed() throws InvalidStateException {
        if (this.reviewed) {
            this.state.closedState();
        } else {
            throw new InvalidStateException("Cannot transition to 'closed' state! Sprint is not reviewed!");
        }

    }

    @Override
    public boolean containBacklogItem(BacklogItem item) {
        return this.backlog.contains((item));
    }

    @Override
    public boolean backlogContainsActivity(Activity activity) {
        for (var item : backlog) {
            if (item.containsActivity(activity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void sprintEdit(int number, Date start, Date end) {
        if (this.state instanceof CreatedState) {
            this.number = number;
            this.start = start;
            this.end = end;
        }
    }

    // General methods
    public void addBacklogItem(BacklogItem backlog) {
        if (state instanceof CreatedState && !this.backlog.contains(backlog)) {
            this.backlog.add(backlog);
        }
    }

    public void removeBacklogItem(BacklogItem backlog) {
        if (state instanceof CreatedState) {
            this.backlog.remove(backlog);
        }
    }

    public void addDeveloper(User user) {
        if (state instanceof CreatedState && !developers.contains(user)) {
            this.developers.add(user);
        }
    }

    public void removeDeveloper(User user) {
        if (state instanceof CreatedState) {
            this.developers.remove(user);
        }
    }

    // Getters & Setters
    public int getNumber() {
        return number;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public List<BacklogItem> getBacklog() {
        return backlog;
    }

    public List<User> getDevelopers() {
        return developers;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void setReleases(List<Release> releases) {
        this.releases = releases;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed() {
        if (this.document != null) {
            this.reviewed = true;
        }
    }
}
