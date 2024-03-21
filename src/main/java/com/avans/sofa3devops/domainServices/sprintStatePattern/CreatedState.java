package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;

public class CreatedState implements ISprintState {
    private final ISprint sprint;
    private final NotificationService service;

    public CreatedState(ISprint sprint, NotificationService service) {
        this.sprint = sprint;
        this.service = service;
    }

    @Override
    public void inProgressState() {
        sprint.setState(new InProgressState(sprint, service));
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
