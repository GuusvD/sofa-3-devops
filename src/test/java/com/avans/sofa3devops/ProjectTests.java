package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domainServices.gitStrategyPattern.GitHub;
import com.avans.sofa3devops.domainServices.gitStrategyPattern.IGitCommands;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Pdf;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Png;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import com.avans.sofa3devops.domainServices.sprintStatePattern.ClosedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@SpringBootTest
public class ProjectTests {
    private List<IReport> reportStrategies;
    private IGitCommands gitStrategy;
    private Pipeline pipeline;
    private Project project;
    private User createdBy;

    @BeforeEach
    void setUp() {
        reportStrategies = new ArrayList<>();
        reportStrategies.add(new Pdf());
        reportStrategies.add(new Png());
        gitStrategy = new GitHub(Logger.getLogger(GitHub.class.getName()));
        pipeline = new Pipeline();
        project = new Project("Project", reportStrategies, gitStrategy, pipeline);
        createdBy = new User("John Doe", "j.doe@gmail.com", "Password1234");
    }

    @Test
    void OneUserIsAddedToProject() {
        User user = new User("John Doe", "j.doe@gmail.com", "Password1234");

        project.addParticipant(user);

        assertThat(project.getParticipants()).hasSize(1);
    }

    @Test
    void TwoUsersAreAddedToProject() {
        User userOne = new User("John Doe", "j.doe@gmail.com", "Password1234");
        User userTwo = new User("John Doe", "j.doe@gmail.com", "Password1234");

        project.addParticipant(userOne);
        project.addParticipant(userTwo);

        assertThat(project.getParticipants()).hasSize(2);
    }

    @Test
    void OneUserIsAddedWhenTheSamePersonIsAddedAgain() {
        User userOne = new User("John Doe", "j.doe@gmail.com", "Password1234");

        project.addParticipant(userOne);
        project.addParticipant(userOne);

        assertThat(project.getParticipants()).hasSize(1);
    }

    @Test
    void BacklogItemIsRemovedWhenNoSprintArrayIsEmpty() {
        BacklogItem item = new BacklogItem("Item", createdBy);
        project.addBacklogItem(item);

        project.removeBacklogItem(item);

        assertThat(project.getProjectBacklog()).hasSize(0);
    }

    @Test
    void BackLogItemIsRemovedWhenNoSprintContainsTheBackLogItemAndSprintIsInCreatedState() {
        BacklogItem item = new BacklogItem("Item", createdBy);
        project.addBacklogItem(item);
        SprintFactory factory = new SprintFactory();
        ISprint sprint = factory.createReviewSprint(0, new Date(), new Date(), createdBy);
        project.addSprint(sprint);

        project.removeBacklogItem(item);

        assertThat(project.getProjectBacklog()).hasSize(0);

    }

    @Test
    void BackLogItemIsRemovedWhenSprintContainsTheBacklogItemAndSprintIsInCreatedState() {
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
    void BackLogItemIsRemovedWhenNoSprintContainsTheBackLogItemAndSprintIsNotInCreatedState() {
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
    void BackLogItemIsNotRemovedWhenSprintContainsBackLogItemAndIsNotInCreatedState() {
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
    void ActivityIsRemovedWhenSprintListIsEmpty() {
        BacklogItem item = new BacklogItem("Item", createdBy);
        Activity activity = new Activity("Activity", createdBy);
        item.addActivity(activity);
        project.addBacklogItem(item);

        project.removeActivity(activity);

        assertThat(item.getActivities()).hasSize(0);
    }

    @Test
    void ActivityIsRemovedWithOneSprintAndBackLogListIsEmpty() {
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
    void ActivityIsRemovedWithTwoSprintsAndBackLogListsAreEmpty() {
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
    void ActivityIsRemovedWithOneSprintAndBacklogDoesNotContainActivityAndSprintStateEqualsCreatedState() {
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
    void ActivityIsRemovedWithOneSprintAndBacklogDoesNotContainActivityAndSprintStateNotEqualsCreatedState() {
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
    void ActivityIsRemovedWithOneSprintAndBacklogDoesContainActivityAndSprintStateEqualsCreatedState() {
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
    void ActivityIsNotRemovedWithOneSprintAndBacklogDoesContainActivityAndSprintStateNotEqualsCreatedState() {
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
        sprintOne.addBacklogItem(itemTwo);

        project.removeActivity(activityRemove);

        assertThat(itemTwo.getActivities()).hasSize(1);
    }
}
