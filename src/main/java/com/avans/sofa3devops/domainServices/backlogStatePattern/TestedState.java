package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class TestedState implements IBacklogItemState {
    private IItemComponent item;

    public TestedState(IItemComponent item) {
        this.item = item;
    }

    @Override
    public void toDoState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'to do' state!");
    }

    @Override
    public void doingState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'doing' state!");
    }

    @Override
    public void readyForTestingState() {
        item.setState(new ReadyForTestingState(item));
    }

    @Override
    public void testingState() {
        item.setState(new TestingState(item));
    }

    @Override
    public void testedState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'tested' state!");
    }

    @Override
    public void doneState() {
        item.setState(new DoneState(item));
    }
}
