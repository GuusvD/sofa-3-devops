package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.compositeinterfaces.IPipeComponent;

import java.util.*;

public abstract class Action implements IPipeComponent {
    private final int sortIndex;
    private UUID id;
    private List<IPipeComponent> commands;

    protected Action(int sortIndex) {
        this.id = UUID.randomUUID();
        this.sortIndex = sortIndex;
        this.commands = new ArrayList<>();
    }

    public void add(IPipeComponent action) {
        this.commands.add(action);
    }

    public void remove(IPipeComponent action) {
        this.commands.remove(action);
    }

    public UUID getId() {
        return id;
    }

    public int getIndex() {
        return sortIndex;
    }

    public List<IPipeComponent> getCommands() {
        return commands;
    }

}
