package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;
import java.util.UUID;

public abstract class Command implements IPipeComponent {

    private UUID id;
    private String command;
    private Action action;

    public Command() {
        id = UUID.randomUUID();
        connectToAction();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public abstract void connectToAction();

    public abstract boolean execute();
}