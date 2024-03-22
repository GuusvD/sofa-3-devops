package com.avans.sofa3devops.domainservices.backlogstatepattern;

import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;

public interface IBacklogItemState {
    void toDoState() throws InvalidStateException;

    void doingState() throws InvalidStateException;

    void readyForTestingState() throws InvalidStateException;

    void testingState() throws InvalidStateException;

    void testedState() throws InvalidStateException;

    void doneState() throws InvalidStateException;
}
