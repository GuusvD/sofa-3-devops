package com.avans.sofa3devops.domainservices.sprintstatepattern;

import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;

import java.util.Date;
import java.util.Observable;

public class InProgressState extends Observable implements ISprintState {
    private final ISprint sprint;
    private final NotificationService service;

    public InProgressState(ISprint sprint, NotificationService service) {
        this.sprint = sprint;
        this.service = service;

        this.addObserver(service);
    }

    @Override
    public void inProgressState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'in progress' state!");
    }

    @Override
    public void finishedState() throws InvalidStateException {
        Date currentDate = new Date();
        Date sprintEndDate = sprint.getEnd();

        if (currentDate.after(sprintEndDate)) {
            sprint.setState(new FinishedState(sprint, service));

            setChanged();
            notifyObservers();
        } else {
            throw new InvalidStateException("Cannot transition to 'finished' state! Sprint hasn't reached its end date!");
        }
    }

    @Override
    public void closedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'closed' state!");
    }
}
