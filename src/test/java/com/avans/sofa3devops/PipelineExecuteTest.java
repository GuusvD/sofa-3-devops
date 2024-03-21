package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domain.command.*;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import com.avans.sofa3devops.domainServices.sprintStatePattern.FinishedState;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;


@SpringBootTest
public class PipelineExecuteTest {
    private Pipeline pipeline;
    private ISprint sprint;
    @BeforeEach
    void setUp() throws Exception {
        User createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
        ISprintFactory factory = new SprintFactory();
        sprint = factory.createRegularSprint(1, new Date(), new Date(), createdByUser);
        sprint.executePipeline();
        pipeline = sprint.getPipeline();
    }

    @BeforeEach
    void setUpCommands(){
        pipeline.addCommandToAction(new DotnetCleanCommand());
        pipeline.addCommandToAction(new DotnetAnalyseCommand());
        pipeline.addCommandToAction(new DotnetRestoreCommand());
        pipeline.addCommandToAction(new DotnetRestoreCommand());
        pipeline.addCommandToAction(new DotnetPublishCommand());
        pipeline.addCommandToAction(new DotnetTestCommand());
        pipeline.addCommandToAction(new GitCloneCommand());
    }

    @Test
    void givenAnSprintInFinishedStateWithPipelineInProgressStateWhenAnEditIsCalledInSprintThenNoEditShouldBeAllowed() throws InvalidStateException {
        sprint.setState(new FinishedState(sprint,new NotificationService(new NotificationExecutor())));
        pipeline.executedState();

        sprint.removeCommandToAction(new );


    }
}
