package com.avans.sofa3devops.domain.command;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.action.Package;
import java.util.logging.Logger;

public class NpmInstallCommand extends Command {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public NpmInstallCommand() {
        super();
        setCommand("npm install");
    }

    @Override
    public void connectToAction() {
        setAction(new Package());
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
