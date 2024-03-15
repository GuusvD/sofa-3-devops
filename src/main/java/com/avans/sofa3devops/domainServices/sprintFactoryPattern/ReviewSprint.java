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
    private Release release;
    private Pipeline pipeline;
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
        this.state.closedState();
    }

    // General methods

    public void addBacklogItem(BacklogItem backlog) {
        this.backlog.add(backlog);
    }

    public void removeBacklogItem(BacklogItem backlog) {
        this.backlog.remove(backlog);
    }

    public void addDeveloper(User user) {
        this.developers.add(user);
    }

    public void removeDeveloper(User user) {
        this.developers.remove(user);
    }

    // Getters & Setters
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public Date getStart() {
        return start;
    }
    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }
    public void setEnd(Date end) {
        this.end = end;
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

    public Release getRelease() {
        return release;
    }
    public void setRelease(Release release) {
        this.release = release;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }
    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
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
