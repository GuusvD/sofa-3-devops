package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domain.command.GitCloneCommand;
import com.avans.sofa3devops.domainservices.gitstrategypattern.GitHub;
import com.avans.sofa3devops.domainservices.gitstrategypattern.IGitCommands;
import com.avans.sofa3devops.domainservices.reportstrategypattern.IReport;
import com.avans.sofa3devops.domainservices.reportstrategypattern.Pdf;
import com.avans.sofa3devops.domainservices.reportstrategypattern.Png;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprintFactory;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.SprintFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
public class Sofa3DevopsApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Sofa3DevopsApplication.class, args);

        List<IReport> reportStrategies = new ArrayList<>();
        reportStrategies.add(new Pdf());
        reportStrategies.add(new Png());
        User creator = new User("John Doe", "j.doe@gmail.com", "Password1234");
        ISprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createRegularSprint(1, new Date(), new Date(), creator);

        Pipeline pipeline = new Pipeline("Pipe", sprint);

        IGitCommands gitStrategy = new GitHub(Logger.getLogger(GitHub.class.getName()));

        Project project = new Project("LANGUAGESCHOOL", reportStrategies, gitStrategy);

        project.printReports();


        BacklogItem item = new BacklogItem("US-1", creator);
        Activity activityOne = new Activity("AC-1-One", creator);
        Activity activityTwo = new Activity("AC-1-Two", creator);

        item.addActivity(activityOne);
        item.addActivity(activityTwo);

        item.getAllStories();

        pipeline.print();
        Command newCommand = new GitCloneCommand();
        pipeline.addCommandToAction(newCommand);
        boolean completed = pipeline.execute();
    }
}
