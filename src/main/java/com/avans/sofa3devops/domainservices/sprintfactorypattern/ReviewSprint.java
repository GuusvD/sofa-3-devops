package com.avans.sofa3devops.domainservices.sprintfactorypattern;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domainservices.exceptions.AssemblyException;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.exceptions.PipelineException;
import com.avans.sofa3devops.domainservices.pipelinestatepattern.ExecutedState;
import com.avans.sofa3devops.domainservices.pipelinestatepattern.InitialState;
import com.avans.sofa3devops.domainservices.sprintstatepattern.CreatedState;
import com.avans.sofa3devops.domainservices.sprintstatepattern.FinishedState;
import com.avans.sofa3devops.domainservices.sprintstatepattern.ISprintState;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;
import com.avans.sofa3devops.domainservices.threadvisitorpattern.NotificationExecutor;

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
    private Pipeline pipeline;

    public ReviewSprint(int number, Date start, Date end, User user) throws PipelineException, AssemblyException {
        this.state = new CreatedState(this, new NotificationService(new NotificationExecutor()));
        this.number = number;
        this.start = start;
        this.end = end;
        this.backlog = new ArrayList<>();
        this.developers = new ArrayList<>();
        this.developers.add(user);
        this.reviewed = false;
        this.pipeline = new Pipeline("Sprint: " + number, this);
        this.releases = new ArrayList<>();
    }

    @Override
    public ISprintState getState() {
        return this.state;
    }

    @Override
    public void setState(ISprintState state) {
        this.state = state;
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
            throw new InvalidStateException("Cannot transition to 'closed' state! Sprint is not reviewed or pipeline is not cancelled/finished!");
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

    @Override
    public void addCommandToAction(Command command) {
        if (pipelineIsRunning()) {
            pipeline.addCommandToAction(command);
        }
    }

    @Override
    public void removeCommandToAction(Command command) {
        if (pipelineIsRunning()) {
            pipeline.removeCommandToAction(command);
        }
    }

    // General methods
    public void addBacklogItem(BacklogItem backlog) {
        if (state instanceof CreatedState && !this.backlog.contains(backlog)) {
            this.backlog.add(backlog);
            backlog.setSprint(this);
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

    @Override
    public void executePipeline() throws InvalidStateException {
        if (state instanceof FinishedState) {
            boolean successful = pipeline.execute();

            if (!successful) {
                pipeline.failedState();
            } else {
                pipeline.finishedState();
                addRelease(new Release(this, pipeline));
            }
        }
    }

    @Override
    public Pipeline getPipeline() {
        return pipeline;
    }

    public boolean pipelineIsRunning() {
        return !(this.pipeline.getState() instanceof ExecutedState);
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
        if (state instanceof FinishedState && !(pipeline.getState() instanceof InitialState || pipeline.getState() instanceof ExecutedState)) {
            this.document = document;
        }
    }

    public List<Release> getReleases() {
        return releases;
    }

    public void addRelease(Release release) {
        if (state instanceof FinishedState && pipeline.getState() instanceof com.avans.sofa3devops.domainservices.pipelinestatepattern.FinishedState) {
            this.releases.add(release);
        }
    }

    public void setReviewed() {
        if (state instanceof FinishedState && this.document != null && !(pipeline.getState() instanceof InitialState || pipeline.getState() instanceof ExecutedState)) {
            this.reviewed = true;
        }
    }
}
