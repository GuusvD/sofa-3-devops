package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Project {
    private UUID id;
    private String name;
    private User owner;
    private List<User> participants;
    private List<ISprint> sprints;
    private List<BacklogItem> projectBacklog;
    private final List<IReport> _reportStrategies;
    private Pipeline pipeline;

    public Project (String name, List<IReport> reportStrategies, Pipeline pipeline) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.participants = new ArrayList<>();
        this.projectBacklog = new ArrayList<>();
        this.sprints = new ArrayList<>();
        _reportStrategies = reportStrategies;
        this.pipeline = pipeline;
    }

    public void printReports() {
        for(IReport strategy : _reportStrategies ) {
            strategy.printReport(this);
        }
    }

    public String getName() {
        return this.name;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    // methods
    // add/remove sprint
    // add/remove backLogItem

    public void addParticipant(User user) {
        if(!this.participants.contains(user)) {
            this.participants.add(user);
        }
    }

    public void addBacklogItem (BacklogItem item) {this.projectBacklog.add(item);}
    public void addSprint(ISprint sprint) {this.sprints.add(sprint);}
    public void removeBacklogItem (BacklogItem item) {
        boolean delete = true;
        for (var sprint : sprints) {
            if(sprint.containBacklogItem(item) && !(sprint.getState() instanceof CreatedState)) {
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

    public void setOwner(User owner) {this.owner = owner;}

    public List<User> getParticipants() {return this.participants;}

    public List<BacklogItem> getProjectBacklog() {return this.projectBacklog;}

    void getBacklog() {
        for(var backlog : projectBacklog) {
            backlog.getAllStories();
        }
    }
}
