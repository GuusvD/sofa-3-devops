package com.avans.sofa3devops.domainServices.backlogStatePattern;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class DoingState implements IBacklogItemState {
    private BacklogItem item;

    public DoingState(BacklogItem item) {
        this.item = item;
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
