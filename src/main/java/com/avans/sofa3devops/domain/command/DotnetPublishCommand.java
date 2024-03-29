package com.avans.sofa3devops.domain.command;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.action.Deploy;

import java.util.logging.Logger;

public class DotnetPublishCommand extends Command {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public DotnetPublishCommand() {
        super();
        setCommand("dotnet publish");
    }

    @Override
    public void connectToAction() {
        setAction(new Deploy());
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
