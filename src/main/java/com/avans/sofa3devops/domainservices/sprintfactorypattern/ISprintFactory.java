package com.avans.sofa3devops.domainservices.sprintfactorypattern;

import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.exceptions.AssemblyException;
import com.avans.sofa3devops.domainservices.exceptions.PipelineException;
import com.avans.sofa3devops.domainservices.exceptions.SprintBuildException;

import java.util.Date;

public interface ISprintFactory {
    ISprint createRegularSprint(int number, Date start, Date end, User user) throws SprintBuildException, PipelineException, AssemblyException;

    ISprint createReviewSprint(int number, Date start, Date end, User user) throws SprintBuildException, PipelineException, AssemblyException;
}
