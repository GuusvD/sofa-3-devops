package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.List;
import java.util.UUID;

public class Project {
    private UUID id;
    private String name;
    private User owner;
    private List<User> participants;
    private List<ISprint> sprints;
    private List<BacklogItem> projectBacklog;
    private List<IReport> _reportStrategies;
    private Pipeline pipeline;

    public Project (String name, List<IReport> reportStrategies, Pipeline pipeline) {
        this.name = name;
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
}
