package com.avans.sofa3devops.domain.action;

import com.avans.sofa3devops.domain.Action;
import com.avans.sofa3devops.domain.Release;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;

import java.util.logging.Logger;

public class Sources extends Action {
    private final String name = getClass().getSimpleName();
    private final Logger logger = Logger.getLogger(getClass().getName());
   
    public Sources() {
        super(1);
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean execute() {
        logger.info("Starting source action: " + getName());

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