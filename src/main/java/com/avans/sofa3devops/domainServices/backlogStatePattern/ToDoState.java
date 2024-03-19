package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class ToDoState implements IBacklogItemState {
    private IItemComponent item;

    public ToDoState(IItemComponent item) {
        this.item = item;
    }

    @Override
    public void toDoState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'to do' state!");
    }

    @Override
    public void doingState() {
        item.setState(new DoingState(item));
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
