package com.avans.sofa3devops.domainServices.sprintFactoryPattern;


import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ISprintState;

public interface ISprint {
    void setState(ISprintState state);
    ISprintState getState();

    void inProgress() throws InvalidStateException;
    void finished() throws InvalidStateException;
    void closed() throws InvalidStateException;


    void addDeveloper(User user);
    void removeDeveloper(User user);
    void addBacklogItem(BacklogItem item);
    void removeBacklogItem(BacklogItem item);
}
