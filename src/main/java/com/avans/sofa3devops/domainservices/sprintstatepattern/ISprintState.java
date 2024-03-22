package com.avans.sofa3devops.domainservices.sprintstatepattern;

import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;

public interface ISprintState {
    void inProgressState() throws InvalidStateException;

    void finishedState() throws InvalidStateException;

    void closedState() throws InvalidStateException;
}
