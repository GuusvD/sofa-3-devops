package com.avans.sofa3devops.domainservices.sprintstatepattern;

import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;

import java.util.Observable;

public class FinishedState extends Observable implements ISprintState {
    private final ISprint sprint;

    public FinishedState(ISprint sprint, NotificationService service) {
        this.sprint = sprint;

        this.addObserver(service);
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
