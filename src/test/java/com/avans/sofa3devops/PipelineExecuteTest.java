package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Command;
import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domain.command.*;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class PipelineExecuteTest {
    private Pipeline pipeline;

    @BeforeEach
    void setUp() throws Exception {
        User createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
        ISprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, new Date(), new Date(), createdByUser);
        pipeline = new Pipeline("Pipe", sprint);
    }

    @BeforeEach
    void setUpCommands(){
        pipeline.addCommandToAction(new DotnetCleanCommand());
        pipeline.addCommandToAction(new DotnetAnalyseCommand());
        pipeline.addCommandToAction(new DotnetRestoreCommand());
        pipeline.addCommandToAction(new DotnetRestoreCommand());
        pipeline.addCommandToAction(new DotnetPublishCommand());
        pipeline.addCommandToAction(new DotnetTestCommand());
//        pipeline.addCommandToAction(new GitCloneCommand());
    }
}
