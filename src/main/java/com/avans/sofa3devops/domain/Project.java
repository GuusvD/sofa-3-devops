package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.gitstrategypattern.IGitCommands;
import com.avans.sofa3devops.domainservices.reportstrategypattern.IReport;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.sprintstatepattern.CreatedState;

import java.util.*;
import java.util.logging.Logger;

public class Project {
    private final UUID id;
    private final List<IReport> reportStrategies;
    private final IGitCommands gitStrategy;
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private String name;
    private List<User> participants;
    private List<ISprint> sprints;
    private List<BacklogItem> projectBacklog;

    public Project(String name, List<IReport> reportStrategies, IGitCommands gitStrategy) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.participants = new ArrayList<>();
        this.projectBacklog = new ArrayList<>();
        this.sprints = new ArrayList<>();
        this.reportStrategies = reportStrategies;
        this.gitStrategy = gitStrategy;
    }

    public void pull() {
        gitStrategy.pull();
    }

    public void push() {
        gitStrategy.push();
    }

    public void commit() {
        gitStrategy.commit();
    }

    public void status() {
        gitStrategy.status();
    }

    public void checkout() {
        gitStrategy.checkout();
    }

    public void stash() {
        gitStrategy.stash();
    }

    public void printReports() {
        for (IReport strategy : reportStrategies) {
            strategy.printReport(this);
        }
    }

    public String getName() {
        return this.name;
    }

    public void addParticipant(User user) {
        if (!this.participants.contains(user)) {
            this.participants.add(user);
        }
    }

    public void addBacklogItem(BacklogItem item) {
        this.projectBacklog.add(item);
    }

    public void addSprint(ISprint sprint) {
        this.sprints.add(sprint);
    }

    public void removeSprint(ISprint sprint) {
        if (sprint.getState() instanceof CreatedState) {
            this.sprints.remove(sprint);
        }
    }

    public void removeBacklogItem(BacklogItem item) {
        boolean delete = true;
        for (var sprint : sprints) {
            if (sprint.containBacklogItem(item) && !(sprint.getState() instanceof CreatedState)) {
                delete = false;
                break;
            }
        }
        if (delete) {
            this.projectBacklog.remove(item);
            item.removeFromSprints(sprints);
        }
    }

    public void removeActivity(Activity activity) {
        boolean delete = true;
        for (var sprint : sprints) {
            if (sprint.backlogContainsActivity(activity) && !(sprint.getState() instanceof CreatedState)) {
                delete = false;
                break;
            }
        }
        if (delete) {
            for (var item : projectBacklog) {
                if (item.containsActivity(activity)) {
                    item.removeActivity(activity);
                }
            }

        }
    }

    public List<User> getParticipants() {
        return this.participants;
    }

    public List<BacklogItem> getProjectBacklog() {
        return this.projectBacklog;
    }

    public List<ISprint> getSprints() {
        return this.sprints;
    }

    void getBacklog() {
        for (var backlog : projectBacklog) {
            backlog.getAllStories();
        }
    }

    public void getAllProjectThreads() {
        List<Thread> forum = new ArrayList<>();
        for (var item : projectBacklog) {
            forum.addAll(item.getThreads());
        }

        for (Thread thread : forum) {
            logger.info("Title: " + thread.getTitle() + "\nBody: " + thread.getBody() + "\n");
        }
    }

    public UUID getId() {
        return id;
    }

}
