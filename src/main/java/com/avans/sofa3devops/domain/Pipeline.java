package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.IPipelineState;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.InitialState;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Pipeline implements IPipeComponent {
    private String name;
    private List<IPipeComponent> actions;
    private List<Release> releases;
    private Release release;
    private IPipelineState state;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Pipeline(String name) {
        this.name = name;
        this.actions = new ArrayList<>();
        this.releases = new ArrayList<>();
        this.state = new InitialState(this);
    }

    public void setActions(List<IPipeComponent> actions) {
        this.actions = actions;
    }

    public IPipelineState getState() {
        return state;
    }

    public void setState(IPipelineState state) {
        this.state = state;
    }

    // Pipeline controller methods
    public void executedState() throws InvalidStateException {
        this.state.executedState();
    }

    public void finishedState() throws InvalidStateException {
        this.state.finishedState();
    }

    public void failedState() throws InvalidStateException {
        this.state.failedState();
    }

    public void cancelledState() throws InvalidStateException {
        this.state.cancelledState();
    }

    @Override
    public void print() {
        
    }
}
