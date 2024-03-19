package com.avans.sofa3devops.domain.action;

import com.avans.sofa3devops.domain.Action;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;

import java.util.logging.Logger;

public class Utility extends Action {
    private final String name = getClass().getName();
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Integer sortIndex = 7;
    public String getName() {return name;}

    @Override
    public void print() {
        logger.info("Starting utility action: " + getName());

        for (IPipeComponent command : getCommands()) {
            command.print();
        }

    }
}
