package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ISprintState;

public interface ISprint {
    void setState(ISprintState state);
    ISprintState getState();
}
