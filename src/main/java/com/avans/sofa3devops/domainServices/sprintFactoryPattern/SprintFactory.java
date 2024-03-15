package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domain.User;

import java.util.Date;
import java.util.List;

public class SprintFactory implements ISprintFactory {
    public ISprint createRegularSprint(int number, Date start, Date end, User user) {
        return new RegularSprint(number, start, end, user);
    }

    public ISprint createReviewSprint(int number, Date start, Date end, User user) {
        return new ReviewSprint(number, start, end, user);
    }
}
