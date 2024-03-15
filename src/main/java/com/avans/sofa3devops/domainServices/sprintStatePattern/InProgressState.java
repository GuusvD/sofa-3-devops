package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

public class InProgressState implements ISprintState {
    private ISprint sprint;

    public InProgressState(ISprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public void inProgressState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'in progress' state!");
    }

    @Override
    public void finishedState() {
        sprint.setState(new FinishedState(sprint));
    }

    @Override
    public void closedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'closed' state!");
    }
}
