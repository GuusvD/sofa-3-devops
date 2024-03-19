package com.avans.sofa3devops.domainServices.sprintStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public interface ISprintState {
    void inProgressState() throws InvalidStateException;

    void finishedState() throws InvalidStateException;

    void closedState() throws InvalidStateException;
}
