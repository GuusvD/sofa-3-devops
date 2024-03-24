package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.command.*;
import com.avans.sofa3devops.domainservices.backlogstatepattern.DoneState;
import com.avans.sofa3devops.domainservices.gitstrategypattern.GitHub;
import com.avans.sofa3devops.domainservices.gitstrategypattern.IGitCommands;
import com.avans.sofa3devops.domainservices.reportstrategypattern.IReport;
import com.avans.sofa3devops.domainservices.reportstrategypattern.Pdf;
import com.avans.sofa3devops.domainservices.reportstrategypattern.Png;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprint;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ISprintFactory;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.ReviewSprint;
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
        // Setup
        Logger logger = Logger.getLogger("MAIN LOGGER");

        // Strategies
        List<IReport> reportStrategies = new ArrayList<>();
            reportStrategies.add(new Pdf());
            reportStrategies.add(new Png());
        IGitCommands gitStrategy = new GitHub(Logger.getLogger(GitHub.class.getName()));

        //Users
        User creator = new User("John Doe", "j.doe@gmail.com", "Password1234");
        User devOne = new User("Mickey Mouse", "m.mouse@gmail.com", "Disney1234");
        User devTwo = new User("Donald Duck", "d.duck@gmail.com", "Disney6789");

        //Project
        Project project = new Project("LANGUAGESCHOOL", reportStrategies, gitStrategy);

        //Sprints
        ISprintFactory factory = new SprintFactory();
        ReviewSprint reviewSprint =(ReviewSprint) factory.createReviewSprint(1, new Date(), new Date(), creator);
        ISprint regularSprint =  factory.createRegularSprint(2, new Date(), new Date(), creator);
            regularSprint.addDeveloper(devOne);
            reviewSprint.addDeveloper(devTwo);
            reviewSprint.addDeveloper(devTwo);

        // Backlog Items and activities
        BacklogItem itemWithoutActivities = new BacklogItem("US-1",creator);
        BacklogItem itemWithActivities = new BacklogItem("US-2", creator);
            regularSprint.addBacklogItem(itemWithActivities);
            reviewSprint.addBacklogItem(itemWithActivities);
            Activity activityOne = new Activity("AC-2-One", creator);
            Activity activityTwo = new Activity("AC-2-Two", creator);
                itemWithActivities.addActivity(activityOne);
                itemWithActivities.addActivity(activityTwo);
        BacklogItem itemUnplanned = new BacklogItem("US-3",creator);

        // Thread & Message
        Thread thread = new Thread("Password management?","What is the best hashing algorithm?",devOne);
        Message message = new Message("How about Bcrypt?",devTwo);
        Message response = new Message("Thank you for your answer!",devOne);
            itemWithoutActivities.addThread(thread);
            thread.addMessage(message);
            message.addMessage(response);

        // Document
        Document reviewDocument = new Document();

        // Add to project
            project.addBacklogItem(itemWithActivities);
            project.addBacklogItem(itemWithActivities);
            project.addParticipant(devOne);
            project.addParticipant(devTwo);
            project.addParticipant(devTwo);
            project.addSprint(reviewSprint);
            project.addSprint(regularSprint);

        // Pipelines
        Pipeline regularSprintPipeline = regularSprint.getPipeline();
        Pipeline reviewSprintPipeline = regularSprint.getPipeline();

        // Commands
        Command commandAnalysis = new DotnetAnalyseCommand();
        Command commandBuild = new DotnetBuildCommand();
        Command commandDeploy = new DotnetPublishCommand();
        Command commandPackage = new DotnetRestoreCommand();
        Command commandSource = new GitCloneCommand();
        Command commandTest = new DotnetTestCommand();
        Command commandUtility = new DotnetCleanCommand();
            reviewSprint.addCommandToAction(commandAnalysis);
            reviewSprint.addCommandToAction(commandBuild);
            reviewSprint.addCommandToAction(commandDeploy);
            reviewSprint.addCommandToAction(commandPackage);
            reviewSprint.addCommandToAction(commandSource);
            reviewSprint.addCommandToAction(commandTest);
            reviewSprint.addCommandToAction(commandUtility);


        // -------------------------------------------------------------------------------------------------------------

        // Scenario
        project.getBacklog();
        reviewSprint.inProgress();
        project.removeBacklogItem(itemWithActivities);
        project.removeBacklogItem(itemUnplanned);

        itemWithActivities.doingState();
        itemWithActivities.readyForTestingState();
        itemWithActivities.testingState();
        itemWithActivities.testedState();
        itemWithActivities.doneState();
        itemWithActivities.setFinished();
        logger.info(String.valueOf(itemWithActivities.getFinished()));
            activityOne.doingState();
            activityOne.readyForTestingState();
            activityOne.testingState();
            activityOne.testedState();
            activityOne.doneState();
                activityOne.setFinished();
        itemWithActivities.setFinished();
        logger.info(String.valueOf(itemWithActivities.getFinished()));
            activityTwo.doingState();
            activityTwo.readyForTestingState();
            activityTwo.testingState();
            activityTwo.testedState();
            activityTwo.doneState();
                activityTwo.setFinished();
        itemWithActivities.setFinished();
        logger.info(String.valueOf(itemWithActivities.getFinished()));

        reviewSprint.finished();
        reviewSprint.executePipeline();
        reviewSprint.setDocument(reviewDocument);
        reviewSprint.setReviewed();
        reviewSprint.closed();

        // --------------------------
        project.printReports();

        itemWithActivities.getAllStories();

        regularSprintPipeline.print();

        Command newCommand = new GitCloneCommand();
        regularSprintPipeline.addCommandToAction(newCommand);
        logger.info(String.valueOf(regularSprintPipeline.execute()));

    }
}
