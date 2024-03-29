package com.avans.sofa3devops.domainservices.sprintstatepattern;

import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;

public class ClosedState implements ISprintState {
    @Override
    public void inProgressState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'in progress' state!");
    }

    @Override
    public void finishedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'finished' state!");
    }

    @Override
    public void closedState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'closed' state!");
    }
}
