package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

import java.util.Observable;

public class ReadyForTestingState extends Observable implements IBacklogItemState {
    private IItemComponent item;

    public ReadyForTestingState(IItemComponent item) {
        this.item = item;
    }

    @Override
    public void toDoState() {
        item.setState(new ToDoState(item));

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
        item.setState(new TestingState(item));
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
