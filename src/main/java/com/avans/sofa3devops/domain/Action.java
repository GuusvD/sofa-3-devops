package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public abstract class Action implements IPipeComponent {
    private UUID id;
    private String name;

    private List<IPipeComponent> commands;

    public Action() {
        this.commands = new ArrayList<>();
    }

    public void add(IPipeComponent action) {
        this.commands.add(action);
    }

    public void remove(IPipeComponent action) {
        this.commands.remove(action);
    }

    public abstract void print();

    public List<IPipeComponent> getCommands() {
        return commands;
    }


}
