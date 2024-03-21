package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;

public class ToDoState implements IBacklogItemState {
    private final IItemComponent item;
    private final NotificationService service;

    public ToDoState(IItemComponent item, NotificationService service) {
        this.item = item;
        this.service = service;
    }

    @Override
    public void toDoState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'to do' state!");
    }

    @Override
    public void doingState() {
        item.setState(new DoingState(item, service));
    }

    @Override
    public void readyForTestingState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'ready for testing' state!");
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
