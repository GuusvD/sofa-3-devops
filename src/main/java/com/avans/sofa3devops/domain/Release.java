package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.List;
import java.util.UUID;

public class Release {
    private UUID id;
    private ISprint sprint;
    private Pipeline pipeline;

    private List<IPipeComponent> actions;

    public Release(List<IPipeComponent> actions) {
        this.actions = actions;
    }

    public List<IPipeComponent> getActions() {
        return actions;
    }
}
