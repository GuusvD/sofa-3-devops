package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.FinishedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ISprintState;

import java.io.InvalidObjectException;
import java.util.*;
import java.util.logging.Logger;

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

    private Logger logger = Logger.getLogger(this.getClass().getName());

    public ReviewSprint(int number, Date start, Date end, User user) throws Exception {
        this.state = new CreatedState(this);
        this.number = number;
        this.start = start;
        this.end = end;
        this.backlog = new ArrayList<>();
        this.developers = new ArrayList<>();
        this.developers.add(user);
        this.reviewed = false;
        this.pipeline = new Pipeline("Sprint:" + number);
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
        if(reviewed) {
            this.state.finishedState();
        } else {
            logger.info("Sprint isn't reviewed yet! Make sure a document is uploaded and confirmed!");
        }
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
            backlog.setSprint(this);
        }
    }

    public void removeBacklogItem(BacklogItem backlog) {
        if (state instanceof CreatedState) {
            this.backlog.remove(backlog);
        }
    }

    @Override
    public void addActionsToPipeline(List<IPipeComponent> actions) throws InvalidObjectException {

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
        if (getState().getClass() == FinishedState.class) {
            boolean successful = pipeline.execute();

            if (!successful) {
                pipeline.failedState();
            } else {
                pipeline.finishedState();
                addRelease(new Release(this, pipeline));
            }
        }
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

    public void addRelease(Release release) {
        this.releases.add(release);
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
