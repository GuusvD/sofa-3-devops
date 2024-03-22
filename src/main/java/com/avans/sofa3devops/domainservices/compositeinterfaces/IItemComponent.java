package com.avans.sofa3devops.domainservices.compositeinterfaces;

import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.backlogstatepattern.IBacklogItemState;

public interface IItemComponent {
    String getStory();

    IBacklogItemState getState();

    void setState(IBacklogItemState state);

    User getAssignedTo();

    void setAssignedTo(User assignedTo);

    boolean getFinished();

    void setFinished();
}
