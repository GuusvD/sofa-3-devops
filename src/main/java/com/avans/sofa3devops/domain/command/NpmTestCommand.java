package com.avans.sofa3devops.domain.command;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.action.Test;
import java.util.logging.Logger;

public class NpmTestCommand extends Command {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public NpmTestCommand() {
        super();
        setCommand("npm test");
    }

    @Override
    public void connectToAction() {
        setAction(new Test());
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
