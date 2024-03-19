package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domain.actions.*;
import com.avans.sofa3devops.domain.actions.Package;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;

import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Pipeline implements IPipeComponent {
    private String name;
    private List<IPipeComponent> actions;
    private List<Release> releases;
    private Release release;
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Pipeline(String name) {
        this.name = name;
        this.actions = new ArrayList<>();
        this.releases = new ArrayList<>();
    }

    public void setActions(List<IPipeComponent> actions) {
        this.actions = actions;
    }


    @Override
    public void start(Release release) {
        this.release = release;
        releases.add(release);
        run();
        build();
        stop();
    }

    @Override
    public void run() {
        actions.forEach(action -> action.start(release));
    }

    @Override
    public void build() {

    }

    @Override
    public void stop() {

    }
}
