package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.Date;
import java.util.Observable;

public class InProgressState extends Observable implements ISprintState {
    private final ISprint sprint;

    public InProgressState(ISprint sprint) {
        this.sprint = sprint;
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
            sprint.setState(new FinishedState(sprint));

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
