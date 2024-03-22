package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainservices.compositeinterfaces.IPipeComponent;

import java.util.UUID;

public abstract class Command implements IPipeComponent {

    private final UUID id;
    private String commandCall;
    private Action action;

    protected Command() {
        id = UUID.randomUUID();
        connectToAction();
    }

    public String getCommand() {
        return commandCall;
    }

    public void setCommand(String command) {
        this.commandCall = command;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public abstract void connectToAction();

    public abstract boolean execute();

    public UUID getId() {
        return id;
    }
}
