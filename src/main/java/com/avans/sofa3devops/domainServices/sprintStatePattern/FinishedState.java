package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.Observable;

public class FinishedState extends Observable implements ISprintState {
    private final ISprint sprint;

    public FinishedState(ISprint sprint) {
        this.sprint = sprint;
    }

    @Override
    public void inProgressState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'in progress' state!");
    }

    @Override
    public void finishedState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'finished' state!");
    }

    @Override
    public void closedState() {
        sprint.setState(new ClosedState());

        setChanged();
        notifyObservers();
    }
}
