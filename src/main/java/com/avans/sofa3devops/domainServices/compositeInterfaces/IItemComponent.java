package com.avans.sofa3devops.domainServices.compositeInterfaces;

import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domain.Thread;

import com.avans.sofa3devops.domainServices.backlogStatePattern.IBacklogItemState;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.List;


public interface IItemComponent {
    String getStory();
    IBacklogItemState getState();

    void setState(IBacklogItemState state);
    User getAssignedTo();
    void setAssignedTo(User assignedTo);
    boolean getFinished();
    void setFinished();

    void removeFromSprints(List<ISprint> sprints);


}
