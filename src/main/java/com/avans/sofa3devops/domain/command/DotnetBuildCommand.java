package com.avans.sofa3devops.domain.command;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.action.Build;

import java.util.logging.Logger;

public class DotnetBuildCommand extends Command {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public DotnetBuildCommand() {
        super();
        setCommand("dotnet build");
    }

    @Override
    public void connectToAction() {
        setAction(new Build());
    }

    @Override
    public boolean execute() {
        logger.info("Execute " + getCommand());
        logger.info("Successfully executed " + getCommand() + " without any errors!");
        return true;
    }

    @Override
    public void print() {
        logger.info(this.getClass().getSimpleName() + " " + getCommand());
    }
}
