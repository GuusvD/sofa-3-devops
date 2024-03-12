package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.logging.Logger;

public class InProgressState implements ISprintState {
    private ISprint sprint;
    final Logger logger = Logger.getLogger(this.getClass().getName());

    public InProgressState(ISprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public void createdState() {
        logger.info("Can't go to 'created' state!");
    }

    @Override
    public void inProgressState() {
        logger.info("Already in 'in progress' state!");
    }

    @Override
    public void finishedState() {
        sprint.setState(new FinishedState(sprint));
    }

    @Override
    public void closedState() {
        logger.info("Can't go to 'closed' state!");
    }
}
