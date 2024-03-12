package com.avans.sofa3devops.domainServices.sprintStatePattern;

public interface ISprintState {
    void createdState();
    void inProgressState();
    void finishedState();
    void closedState();
}
