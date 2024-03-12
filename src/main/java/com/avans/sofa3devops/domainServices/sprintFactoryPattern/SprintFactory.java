package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

public class SprintFactory implements ISprintFactory {
    public ISprint createRegularSprint() {
        return new RegularSprint();
    }

    public ISprint createReviewSprint() {
        return new ReviewSprint();
    }
}
