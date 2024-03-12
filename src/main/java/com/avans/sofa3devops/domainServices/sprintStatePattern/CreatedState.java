package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

public class CreatedState implements ISprintState {
    private ISprint sprint;

    public CreatedState(ISprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public void inProgressState() {
        sprint.setState(new InProgressState(sprint));
    }

    @Override
    public void finishedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'finished' state!");
    }

    @Override
    public void closedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'closed' state!");
    }
}
