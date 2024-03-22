package com.avans.sofa3devops.domain.command;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.action.Package;

import java.util.logging.Logger;

public class DotnetRestoreCommand extends Command {

    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public DotnetRestoreCommand() {
        super();
        setCommand("dotnet restore");
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
