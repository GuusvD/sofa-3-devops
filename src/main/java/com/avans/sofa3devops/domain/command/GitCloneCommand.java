package com.avans.sofa3devops.domain.command;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.action.Sources;
import java.util.logging.Logger;

public class GitCloneCommand extends Command {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public GitCloneCommand() {
        super();
        setCommand("git clone");
    }

    @Override
    public void connectToAction() {
        setAction(new Sources());
    }

    @Override
    public void execute() {
        logger.info("Execute " + getCommand());
        logger.info("Succesfully executed " + getCommand() + " without any errors!");
    }

    @Override
    public void print() {

    }
}
