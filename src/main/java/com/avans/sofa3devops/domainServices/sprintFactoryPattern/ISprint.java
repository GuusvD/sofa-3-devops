package com.avans.sofa3devops.domainServices.sprintFactoryPattern;


import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ISprintState;

public interface ISprint {
    void setState(ISprintState state);
    ISprintState getState();

    void inProgress() throws InvalidStateException;
    void finished() throws InvalidStateException;
    void closed() throws InvalidStateException;

}
