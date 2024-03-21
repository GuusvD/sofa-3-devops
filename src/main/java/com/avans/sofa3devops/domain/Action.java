package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class Action implements IPipeComponent {
    private UUID id;
    private String name;
    private int sortIndex;

    private List<IPipeComponent> commands;

    public Action(int sortIndex) {
        this.id = UUID.randomUUID();
        this.sortIndex = sortIndex;
        this.commands = new ArrayList<IPipeComponent>();
    }

    public void add(IPipeComponent action) {
        this.commands.add(action);
    }

    public void remove(IPipeComponent action) {
        this.commands.remove(action);
    }

    public abstract boolean execute() throws InvalidStateException;

    public int getIndex() {
        return sortIndex;
    }

    public List<IPipeComponent> getCommands() {
        return commands;
    }
}
