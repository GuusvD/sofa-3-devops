package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domainServices.sprintStatePattern.CreatedState;

public class SprintFactory implements ISprintFactory {
    public ISprint createRegularSprint() {
        return new RegularSprint(null, 0, null, null, null, null, null, null, null);
    }

    public ISprint createReviewSprint() {
        return new ReviewSprint(null, 0, null, null, null, null, null, null, null, false);
    }
}
