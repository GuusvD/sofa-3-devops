package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.List;

public class Project {
    private String name;
    private List<ISprint> sprints;
    private List<BacklogItem> projectBacklog;

    private List<IReport> _reportStrategies;

    public Project (String name, List<IReport> reportStrategies) {
        this.name = name;
        _reportStrategies = reportStrategies;
    }

    public void printReports() {
        for(IReport strategy : _reportStrategies ) {
            strategy.printReport(this);
        }
    }

    public String getName() {
        return this.name;
    }

    // methods
    // add/remove sprint
    // add/remove backLogItem
}
