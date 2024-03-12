package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.logging.Logger;

public class CreatedState implements ISprintState {
    private ISprint sprint;
    final Logger logger = Logger.getLogger(this.getClass().getName());

    public CreatedState(ISprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public void createdState() {
        logger.info("Already in 'created' state!");
    }

    @Override
    public void inProgressState() {
        sprint.setState(new InProgressState(sprint));
    }

    @Override
    public void finishedState() {
        logger.info("Can't go to 'finished' state!");
    }

    @Override
    public void closedState() {
        logger.info("Can't go to 'closed' state!");
    }
}
