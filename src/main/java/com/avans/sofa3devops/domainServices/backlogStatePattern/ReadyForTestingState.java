package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;

import java.util.Observable;

public class ReadyForTestingState extends Observable implements IBacklogItemState {
    private IItemComponent item;
    private NotificationService service;

    public ReadyForTestingState(IItemComponent item, NotificationService service) {
        this.item = item;
        this.service = service;

        this.addObserver(service);
    }

    @Override
    public void toDoState() {
        item.setState(new ToDoState(item, service));

        setChanged();
        notifyObservers();
    }

    @Override
    public void doingState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'doing' state!");
    }

    @Override
    public void readyForTestingState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'ready for testing' state!");
    }

    @Override
    public void testingState() {
        item.setState(new TestingState(item, service));
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
