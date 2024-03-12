package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

public class SprintFactory implements ISprintFactory {
    public ISprint createRegularSprint() {
        return new RegularSprint(0, null, null, null, null, null, null, null);
    }

    public ISprint createReviewSprint() {
        return new ReviewSprint(0, null, null, null, null, null, null, null, false);
    }
}
