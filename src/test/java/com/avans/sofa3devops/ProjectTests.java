package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domainServices.gitStrategyPattern.GitHub;
import com.avans.sofa3devops.domainServices.gitStrategyPattern.IGitCommands;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Pdf;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Png;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ClosedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest
public class ProjectTests {
    private List<IReport> reportStrategies;
    private IGitCommands gitStrategy;
    private Project project;
    private User createdBy;

    @BeforeEach
    void setUp() {
        reportStrategies = new ArrayList<>();
        reportStrategies.add(new Pdf());
        reportStrategies.add(new Png());
        gitStrategy = new GitHub(Logger.getLogger(GitHub.class.getName()));
        project = new Project("Project", reportStrategies, gitStrategy);
        createdBy = new User("John Doe", "j.doe@gmail.com", "Password1234");
    }

    @Test
    void givenOneProjectWithoutDevelopersWhenOneUserIsAddedToProjectThenParticipantsSizeEqualsOne () {
        User user = new User("John Doe", "j.doe@gmail.com", "Password1234");

        project.addParticipant(user);

        assertThat(project.getParticipants()).hasSize(1);
    }

    @Test
    void givenOneProjectWithoutDevelopersWhenTwoUserAreAddedToProjectThenParticipantsSizeEqualsTwo() {
        User userOne = new User("John Doe", "j.doe@gmail.com", "Password1234");
        User userTwo = new User("John Doe", "j.doe@gmail.com", "Password1234");

        project.addParticipant(userOne);
        project.addParticipant(userTwo);

        assertThat(project.getParticipants()).hasSize(2);
    }

    @Test
    void givenOneProjectWithoutDevelopersWhenOneUserIsAddedTwiceThenParticipantsSizeEqualsOne() {
        User userOne = new User("John Doe", "j.doe@gmail.com", "Password1234");

        project.addParticipant(userOne);
        project.addParticipant(userOne);

        assertThat(project.getParticipants()).hasSize(1);
    }

    @Test
    void givenProjectWithoutSprintsWhenRemoveBacklogItemIsCalledThenProjectBacklogSizeEqualsZero() {
        BacklogItem item = new BacklogItem("Item", createdBy);
        project.addBacklogItem(item);

        project.removeBacklogItem(item);

        assertThat(project.getProjectBacklog()).hasSize(0);
    }

    @Test
    void givenProjectWithOneSprintWithoutBacklogItemsWhenRemoveBacklogItemIsCalledThenProjectBacklogSizeEqualsZero() throws Exception {
        BacklogItem item = new BacklogItem("Item", createdBy);
        project.addBacklogItem(item);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(0, new Date(), new Date(), createdBy);
        project.addSprint(sprint);

        project.removeBacklogItem(item);

        assertThat(project.getProjectBacklog()).hasSize(0);

    }

    @Test
    void givenProjectWithOneSprintInCreatedStateWithBacklogItemWhenRemoveBacklogItemIsCalledThenProjectBacklogSizeEqualsZero() throws Exception {
        BacklogItem item = new BacklogItem("Item", createdBy);
        project.addBacklogItem(item);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(0, new Date(), new Date(), createdBy);
        project.addSprint(sprint);
        sprint.addBacklogItem(item);

        project.removeBacklogItem(item);

        assertThat(project.getProjectBacklog()).hasSize(0);
    }

    @Test
    void givenProjectWithOneSprintNotInCreatedStateWithoutBacklogItemsWhenRemoveBacklogItemIsCalledThenProjectBacklogSizeEqualsZero() throws Exception {
        BacklogItem item = new BacklogItem("Item", createdBy);
        project.addBacklogItem(item);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(0, new Date(), new Date(), createdBy);
        project.addSprint(sprint);
        sprint.setState(new ClosedState());

        project.removeBacklogItem(item);

        assertThat(project.getProjectBacklog()).hasSize(0);
    }

    @Test
    void givenProjectWithOneSprintNotInCreatedStateWithBacklogItemWhenRemoveBacklogItemIsCalledThenProjectBacklogSizeEqualsOne() throws Exception {
        BacklogItem item = new BacklogItem("Item", createdBy);
        project.addBacklogItem(item);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(0, new Date(), new Date(), createdBy);
        project.addSprint(sprint);
        sprint.addBacklogItem(item);
        sprint.setState(new ClosedState());

        project.removeBacklogItem(item);

        assertThat(project.getProjectBacklog()).hasSize(1);
    }

    @Test
    void givenProjectWithoutSprintsAndWithBacklogItemWithActivityWhenRemoveActivityIsCalledThenBacklogItemActivityListEqualsZero() {
        BacklogItem item = new BacklogItem("Item", createdBy);
        Activity activity = new Activity("Activity", createdBy);
        item.addActivity(activity);
        project.addBacklogItem(item);

        project.removeActivity(activity);

        assertThat(item.getActivities()).hasSize(0);
    }

    @Test
    void givenProjectWithOneSprintWithNoBacklogAndOneBacklogItemWithActivityWhenRemoveActivityIsCalledThenBacklogItemActivityListEqualsZero() throws Exception {
        BacklogItem item = new BacklogItem("Item", createdBy);
        Activity activity = new Activity("Activity", createdBy);
        item.addActivity(activity);
        project.addBacklogItem(item);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(1, new Date(), new Date(), createdBy);
        project.addSprint(sprint);

        project.removeActivity(activity);

        assertThat(item.getActivities()).hasSize(0);

    }

    @Test
    void givenProjectWithTwoEmptySprintsAndBacklogContainsItemWithActivityWhenRemoveActivityIsCalledThenItemActivityListEqualsZero() throws Exception {
        BacklogItem item = new BacklogItem("Item", createdBy);
        Activity activity = new Activity("Activity", createdBy);
        item.addActivity(activity);
        project.addBacklogItem(item);
        SprintFactory factory = new SprintFactory();
        ISprint sprintOne = factory.createReviewSprint(1, new Date(), new Date(), createdBy);
        project.addSprint(sprintOne);
        ISprint sprintTwo = factory.createRegularSprint(2, new Date(), new Date(), createdBy);
        project.addSprint(sprintTwo);

        project.removeActivity(activity);

        assertThat(item.getActivities()).hasSize(0);

    }

    @Test
    void givenProjectWithSprintInCreatedStateWhichHasAItemThatDoesNotContainTheActivityWhenRemoveActivityIsCalledThenItemActivityListEqualsZero() throws Exception {
        BacklogItem itemOne = new BacklogItem("ItemOne", createdBy);
        BacklogItem itemTwo = new BacklogItem("ItemTwo", createdBy);
        Activity activityKeep = new Activity("ActivityOne", createdBy);
        Activity activityRemove = new Activity("ActivityTwo", createdBy);
        project.addBacklogItem(itemOne);
        project.addBacklogItem(itemTwo);
        itemOne.addActivity(activityKeep);
        itemTwo.addActivity(activityRemove);
        SprintFactory factory = new SprintFactory();
        ISprint sprintOne = factory.createReviewSprint(1, new Date(), new Date(), createdBy);
        project.addSprint(sprintOne);
        sprintOne.addBacklogItem(itemOne);


        project.removeActivity(activityRemove);

        assertThat(itemTwo.getActivities()).hasSize(0);
    }

    @Test
    void givenProjectWithSprintNotInCreatedStateWhichHasAItemThatDoesNotContainTheActivityWhenRemoveActivityIsCalledThenItemActivityListEqualsZero() throws Exception {
        BacklogItem itemOne = new BacklogItem("ItemOne", createdBy);
        BacklogItem itemTwo = new BacklogItem("ItemTwo", createdBy);
        Activity activityKeep = new Activity("ActivityOne", createdBy);
        Activity activityRemove = new Activity("ActivityTwo", createdBy);
        project.addBacklogItem(itemOne);
        project.addBacklogItem(itemTwo);
        itemOne.addActivity(activityKeep);
        itemTwo.addActivity(activityRemove);
        SprintFactory factory = new SprintFactory();
        ISprint sprintOne = factory.createReviewSprint(1, new Date(), new Date(), createdBy);
        sprintOne.setState(new InProgressState(sprintOne));
        project.addSprint(sprintOne);
        sprintOne.addBacklogItem(itemOne);

        project.removeActivity(activityRemove);

        assertThat(itemTwo.getActivities()).hasSize(0);
    }

    @Test
    void givenProjectWithSprintInCreatedStateWhichHasAItemThatDoesContainTheActivityWhenRemoveActivityIsCalledThenItemActivityListEqualsZero() throws Exception {
        BacklogItem itemOne = new BacklogItem("ItemOne", createdBy);
        BacklogItem itemTwo = new BacklogItem("ItemTwo", createdBy);
        Activity activityKeep = new Activity("ActivityOne", createdBy);
        Activity activityRemove = new Activity("ActivityTwo", createdBy);
        project.addBacklogItem(itemOne);
        project.addBacklogItem(itemTwo);
        itemOne.addActivity(activityKeep);
        itemTwo.addActivity(activityRemove);
        SprintFactory factory = new SprintFactory();
        ISprint sprintOne = factory.createReviewSprint(1, new Date(), new Date(), createdBy);
        project.addSprint(sprintOne);
        sprintOne.addBacklogItem(itemTwo);

        project.removeActivity(activityRemove);

        assertThat(itemTwo.getActivities()).hasSize(0);
    }

    @Test
    void givenProjectWithSprintNotInCreatedStateWhichHasAItemThatDoesContainTheActivityWhenRemoveActivityIsCalledThenItemActivityListEqualsZero() throws Exception {
        BacklogItem itemOne = new BacklogItem("ItemOne", createdBy);
        BacklogItem itemTwo = new BacklogItem("ItemTwo", createdBy);
        Activity activityKeep = new Activity("ActivityKeep", createdBy);
        Activity activityRemove = new Activity("ActivityRemove", createdBy);
        project.addBacklogItem(itemOne);
        project.addBacklogItem(itemTwo);
        itemOne.addActivity(activityKeep);
        itemTwo.addActivity(activityRemove);
        SprintFactory factory = new SprintFactory();
        ISprint sprintOne = factory.createReviewSprint(1, new Date(), new Date(), createdBy);
        sprintOne.addBacklogItem(itemTwo);
        sprintOne.setState(new InProgressState(sprintOne));
        project.addSprint(sprintOne);

        project.removeActivity(activityRemove);

        assertThat(itemTwo.getActivities()).hasSize(1);
    }

    @Test
    void givenProjectWithSprintInCreatedStateWhenRemoveSprintIsCalledThenSprintListEqualsZero() throws Exception {
        ISprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(1,new Date(),new Date(),createdBy);
        project.addSprint(sprint);

        project.removeSprint(sprint);

        assertThat(project.getSprints()).hasSize(0);
    }

    @Test
    void givenProjectWithSprintNotInCreatedStateWhenRemoveSprintIsCalledThenSprintListEqualsOne() throws Exception {
        ISprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(1,new Date(),new Date(),createdBy);
        project.addSprint(sprint);
        sprint.inProgress();

        project.removeSprint(sprint);

        assertThat(project.getSprints()).hasSize(1);
    }

    @Test
    void givenProjectWithTwoBacklogItemsAndBothHaveAThreadWhenGetAllProjectThreadsIsCalledThenAllArePrintedOut() {
        BacklogItem itemOne = new BacklogItem("ItemOne",createdBy);
        Thread threadOne = new Thread("ThreadOne","BodyOne",createdBy);
        itemOne.addThread(threadOne);
        BacklogItem itemTwo = new BacklogItem("ItemTwo",createdBy);
        Thread threadTwo = new Thread("ThreadTwo","BodyTwo",createdBy);
        itemTwo.addThread(threadTwo);
        project.addBacklogItem(itemOne);
        project.addBacklogItem(itemTwo);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream captureStream = new PrintStream(outputStream);
        System.setOut(captureStream);

        project.getAllProjectThreads();

        String capturedLogs = outputStream.toString();

        assertThat(capturedLogs.contains("Title: " + threadOne.getTitle()));
        assertThat(capturedLogs.contains("Title: " +threadOne.getTitle()));
        assertThat(capturedLogs.contains("Body: " +threadTwo.getBody()));
        assertThat(capturedLogs.contains("Body: " +threadTwo.getBody()));
    }
}
