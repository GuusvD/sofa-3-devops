package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;

import java.util.Observable;

public class DoingState extends Observable implements IBacklogItemState {
    private IItemComponent item;

    public DoingState(IItemComponent item) {
        this.item = item;

        this.addObserver(new NotificationService(new NotificationExecutor()));
    }

    @Override
    public void toDoState() {
        item.setState(new ToDoState(item));
    }

    @Override
    public void doingState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'doing' state!");
    }

    @Override
    public void readyForTestingState() {
        item.setState(new ReadyForTestingState(item));

        setChanged();
        notifyObservers();
    }

    @Override
    public void testingState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'testing' state!");
    }

    @Override
    public void testedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'tested' state!");
    }

    @Override
    public void doneState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'done' state!");
    }
}
