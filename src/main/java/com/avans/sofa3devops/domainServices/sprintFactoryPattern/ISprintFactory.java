package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domain.User;

import java.util.Date;

public interface ISprintFactory {
    ISprint createRegularSprint(int number, Date start, Date end, User user);

    ISprint createReviewSprint(int number, Date start, Date end, User user);
}
