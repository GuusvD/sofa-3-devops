package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domain.*;

import com.avans.sofa3devops.domainServices.sprintStatePattern.ISprintState;


import java.util.Date;
import java.util.List;

public class RegularSprint implements ISprint {
    private ISprintState state;
    private int number;
    private Date start;
    private Date end;
    private List<BacklogItem> backlog;
    private List<User> developers;
    private Document document;
    private Release release;
    private Pipeline pipeline;

    public RegularSprint(ISprintState state, int number, Date start, Date end, List<BacklogItem> backlog, List<User> developers, Document document, Release release, Pipeline pipeline) {
        this.state = state;
        this.number = number;
        this.start = start;
        this.end = end;
        this.backlog = backlog;
        this.developers = developers;
        this.document = document;
        this.release = release;
        this.pipeline = pipeline;
    }

    @Override
    public void setState(ISprintState state) {
        this.state = state;
    }

    @Override
    public ISprintState getState() {
        return this.state;
    }
}
