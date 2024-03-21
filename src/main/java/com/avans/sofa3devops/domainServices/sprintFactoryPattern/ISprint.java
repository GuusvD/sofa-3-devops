package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

import com.avans.sofa3devops.domain.Activity;
import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ISprintState;
import java.util.Date;

public interface ISprint {
    void setState(ISprintState state);
    ISprintState getState();
    void inProgress() throws InvalidStateException;
    void finished() throws InvalidStateException;
    void closed() throws InvalidStateException;
    boolean containBacklogItem(BacklogItem item);
    boolean backlogContainsActivity(Activity activity);
    void sprintEdit(int number, Date start, Date end);
    void addDeveloper(User user);
    void removeDeveloper(User user);
    void addBacklogItem(BacklogItem item);
    void removeBacklogItem(BacklogItem item);
    Date getEnd();
    void executePipeline() throws InvalidStateException;
    Pipeline getPipeline();
    void addCommandToAction(Command command);
    void removeCommandToAction(Command command);
}
