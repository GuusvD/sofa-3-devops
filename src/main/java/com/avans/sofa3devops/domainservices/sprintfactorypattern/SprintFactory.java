package com.avans.sofa3devops.domainservices.sprintfactorypattern;

import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.exceptions.AssemblyException;
import com.avans.sofa3devops.domainservices.exceptions.PipelineException;

import java.util.Date;

public class SprintFactory implements ISprintFactory {
    public ISprint createRegularSprint(int number, Date start, Date end, User user) throws PipelineException, AssemblyException {
        return new RegularSprint(number, start, end, user);
    }

    public ISprint createReviewSprint(int number, Date start, Date end, User user) throws PipelineException, AssemblyException {
        return new ReviewSprint(number, start, end, user);
    }
}
