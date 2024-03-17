package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class DoneState implements IBacklogItemState {
    private BacklogItem item;

    public DoneState(BacklogItem item) {
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
        throw new InvalidStateException("Already in 'done' state!");
    }
}
