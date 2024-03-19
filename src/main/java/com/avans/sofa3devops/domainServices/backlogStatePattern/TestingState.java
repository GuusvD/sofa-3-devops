package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class TestingState implements IBacklogItemState {
    private IItemComponent item;

    public TestingState(IItemComponent item) {
        this.item = item;
    }

    @Override
    public void toDoState() {
        item.setState(new ToDoState(item));
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
        throw new InvalidStateException("Already in 'testing' state!");
    }

    @Override
    public void testedState() {
        item.setState(new TestedState(item));
    }

    @Override
    public void doneState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'done' state!");
    }
}
