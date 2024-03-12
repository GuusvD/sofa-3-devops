package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

public interface ISprintFactory {
    ISprint createReviewSprint();
    ISprint createRegularSprint();
}
