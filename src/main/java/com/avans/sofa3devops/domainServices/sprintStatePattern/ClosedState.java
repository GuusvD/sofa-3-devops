package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.logging.Logger;

public class ClosedState implements ISprintState {
    private ISprint sprint;
    final Logger logger = Logger.getLogger(this.getClass().getName());

    public ClosedState(ISprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public void createdState() {
        logger.info("Can't go to 'created' state!");
    }

    @Override
    public void inProgressState() {
        logger.info("Can't go to 'in progress' state!");
    }

    @Override
    public void finishedState() {
        logger.info("Can't go to 'finished' state!");
    }

    @Override
    public void closedState() {
        logger.info("Already in 'closed' state!");
    }
}
