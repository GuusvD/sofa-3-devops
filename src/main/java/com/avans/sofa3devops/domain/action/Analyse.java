package com.avans.sofa3devops.domain.action;

import com.avans.sofa3devops.domain.Action;
import org.springframework.stereotype.Component;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;

import java.util.logging.Logger;

@Component
public class Analyse extends Action {

    private final String name = getClass().getName();
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final Integer sortIndex = 5;
    public String getName() {
        return name;
    }

    @Override
    public void print() {
        logger.info("Starting analysis action: " + getName());

        for (IPipeComponent command : getCommands()) {
            command.print();
        }
    }
}