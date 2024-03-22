package com.avans.sofa3devops.domain.command;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.action.Sources;

import java.util.logging.Logger;

public class GitCloneCommand extends Command {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public GitCloneCommand() {
        super();
        setCommand("git clone");
    }

    @Override
    public void connectToAction() {
        setAction(new Sources());
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
