package com.avans.sofa3devops.domain.action;

import com.avans.sofa3devops.domain.Action;
import com.avans.sofa3devops.domainservices.compositeinterfaces.IPipeComponent;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;

import java.util.logging.Logger;

public class Package extends Action {

    private final String name = getClass().getSimpleName();
    private final Logger logger = Logger.getLogger(getClass().getName());

    public Package() {
        super(2);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean execute() throws InvalidStateException {
        logger.info("Starting package action: " + getName());
        for (IPipeComponent command : getCommands()) {
            boolean successful = command.execute();

            if (!successful) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void print() {
        logger.info(this.getClass().getSimpleName());
        for (IPipeComponent command : getCommands()) {
            command.print();
        }
    }
}
