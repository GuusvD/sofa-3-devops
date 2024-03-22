package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Release;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.*;
import com.avans.sofa3devops.domainServices.sprintStatePattern.FinishedState;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;
import com.avans.sofa3devops.domainServices.threadVisitorPattern.NotificationExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

@SpringBootTest
public class SprintTest {
    private ReviewSprint reviewSprint;
    private RegularSprint regularSprint;
    private Date start;
    private Date end;
    private User user;
    private NotificationService service;

    @BeforeEach
    void setup() throws Exception {
        start = new Date();
        end = new Date();
        user = new User("John Doe", "j.doe@gmail.com", "Password1234");
        reviewSprint = new ReviewSprint(0, start, end, user);
        regularSprint = new RegularSprint(0, start, end, user);
        service = new NotificationService(new NotificationExecutor());
    }

    @Test
    void givenSprintNotInCreatedStateWhenSprintEditIsCalledThenSprintIsNotEdited() {
        reviewSprint.setState(new InProgressState(reviewSprint, service));
        regularSprint.setState(new InProgressState(regularSprint, service));
        Date newStart = new Date();
        Date newEnd = new Date();

        reviewSprint.sprintEdit(10, newStart, newEnd);
        regularSprint.sprintEdit(10, newStart, newEnd);

        assertThat(reviewSprint.getStart()).isEqualTo(start);
        assertThat(reviewSprint.getEnd()).isEqualTo(end);
        assertThat(reviewSprint.getNumber()).isEqualTo(0);
        assertThat(regularSprint.getStart()).isEqualTo(start);
        assertThat(regularSprint.getEnd()).isEqualTo(end);
        assertThat(regularSprint.getNumber()).isEqualTo(0);
    }

    @Test
    void givenSprintInCreatedStateWhenSprintEditIsCalledThenSprintIsEdited() {
        Date newStart = new Date();
        Date newEnd = new Date();

        reviewSprint.sprintEdit(10, newStart, newEnd);
        regularSprint.sprintEdit(10, newStart, newEnd);

        assertThat(reviewSprint.getStart()).isEqualTo(newStart);
        assertThat(reviewSprint.getEnd()).isEqualTo(newEnd);
        assertThat(reviewSprint.getNumber()).isEqualTo(10);
        assertThat(regularSprint.getStart()).isEqualTo(newStart);
        assertThat(regularSprint.getEnd()).isEqualTo(newEnd);
        assertThat(regularSprint.getNumber()).isEqualTo(10);
    }

    @Test
    void givenSprintNotInCreatedStateWhenAddBacklogItemIsCalledThenItemIsNotAdded() {
        regularSprint.setState(new InProgressState(regularSprint, service));
        reviewSprint.setState(new InProgressState(reviewSprint, service));
        BacklogItem item = new BacklogItem("Item", user);

        regularSprint.addBacklogItem(item);
        reviewSprint.addBacklogItem(item);

        assertThat(regularSprint.getBacklog()).hasSize(0);
        assertThat(reviewSprint.getBacklog()).hasSize(0);
    }

    @Test
    void givenSprintInCreatedStateWhenAddBacklogItemIsCalledThenItemIsAdded() {
        BacklogItem item = new BacklogItem("Item", user);

        regularSprint.addBacklogItem(item);
        reviewSprint.addBacklogItem(item);

        assertThat(regularSprint.getBacklog()).hasSize(1);
        assertThat(reviewSprint.getBacklog()).hasSize(1);
    }

    @Test
    void givenSprintInCreatedStateWhenAddBacklogItemIsCalledTwiceThenItemIsNotAddedTwice() {
        BacklogItem item = new BacklogItem("Item", user);

        regularSprint.addBacklogItem(item);
        reviewSprint.addBacklogItem(item);
        regularSprint.addBacklogItem(item);
        reviewSprint.addBacklogItem(item);

        assertThat(regularSprint.getBacklog()).hasSize(1);
        assertThat(reviewSprint.getBacklog()).hasSize(1);
    }

    @Test
    void givenSprintNotInCreatedStateWithBacklogItemWhenRemoveBacklogItemIsCalledThenItemIsNotRemoved() {
        BacklogItem item = new BacklogItem("Item", user);
        regularSprint.addBacklogItem(item);
        reviewSprint.addBacklogItem(item);
        reviewSprint.setState(new InProgressState(reviewSprint, service));
        regularSprint.setState(new InProgressState(regularSprint, service));

        reviewSprint.removeBacklogItem(item);
        regularSprint.removeBacklogItem(item);

        assertThat(regularSprint.getBacklog()).hasSize(1);
        assertThat(reviewSprint.getBacklog()).hasSize(1);
    }

    @Test
    void givenSprintInCreatedStateWithBacklogItemWhenRemoveBacklogItemIsCalledThenItemIsRemoved() {
        BacklogItem item = new BacklogItem("Item", user);
        regularSprint.addBacklogItem(item);
        reviewSprint.addBacklogItem(item);

        reviewSprint.removeBacklogItem(item);
        regularSprint.removeBacklogItem(item);

        assertThat(regularSprint.getBacklog()).hasSize(0);
        assertThat(reviewSprint.getBacklog()).hasSize(0);
    }

    @Test
    void givenSprintNotInCreatedStateWhenAddDeveloperIsCalledThenNoDeveloperIsNotAdded() {
        User addedUser = new User("Addy Doe", "A.doe@gmail.com", "Password1234");
        reviewSprint.setState(new InProgressState(reviewSprint, service));
        regularSprint.setState(new InProgressState(regularSprint, service));

        reviewSprint.addDeveloper(addedUser);
        regularSprint.addDeveloper(addedUser);

        assertThat(reviewSprint.getDevelopers()).hasSize(1);
        assertThat(regularSprint.getDevelopers()).hasSize(1);
    }

    @Test
    void givenSprintInCreatedStateWhenAddDeveloperIsCalledThenNoDeveloperIsAdded() {
        User addedUser = new User("Addy Doe", "A.doe@gmail.com", "Password1234");

        reviewSprint.addDeveloper(addedUser);
        regularSprint.addDeveloper(addedUser);

        assertThat(reviewSprint.getDevelopers()).hasSize(2);
        assertThat(regularSprint.getDevelopers()).hasSize(2);
    }

    @Test
    void givenSprintInCreatedStateWhenAddDeveloperIsCalledTwiceThenUserIsNotAddedTwice() {
        User addedUser = new User("Addy Doe", "A.doe@gmail.com", "Password1234");

        reviewSprint.addDeveloper(addedUser);
        regularSprint.addDeveloper(addedUser);
        reviewSprint.addDeveloper(addedUser);
        regularSprint.addDeveloper(addedUser);

        assertThat(reviewSprint.getDevelopers()).hasSize(2);
        assertThat(regularSprint.getDevelopers()).hasSize(2);
    }

    @Test
    void givenSprintNotInCreatedStateWithUserWhenRemoveDeveloperThenUserIsNotRemoved() {
        User addedUser = new User("Addy Doe", "A.doe@gmail.com", "Password1234");
        reviewSprint.setState(new InProgressState(reviewSprint, service));
        regularSprint.setState(new InProgressState(regularSprint, service));
        reviewSprint.addDeveloper(addedUser);
        regularSprint.addDeveloper(addedUser);

        reviewSprint.removeDeveloper(addedUser);
        regularSprint.removeDeveloper(addedUser);

        assertThat(reviewSprint.getDevelopers()).hasSize(1);
        assertThat(regularSprint.getDevelopers()).hasSize(1);
    }

    @Test
    void givenSprintInCreatedStateWithUserWhenRemoveDeveloperThenUserIsRemoved() {
        User addedUser = new User("Addy Doe", "A.doe@gmail.com", "Password1234");
        reviewSprint.addDeveloper(addedUser);
        regularSprint.addDeveloper(addedUser);

        reviewSprint.removeDeveloper(addedUser);
        regularSprint.removeDeveloper(addedUser);

        assertThat(reviewSprint.getDevelopers()).hasSize(1);
        assertThat(regularSprint.getDevelopers()).hasSize(1);
    }

    // addRelease
    @Test
    void givenSprintInFinishedStateWithPipelineInFinishedStateWhenAddReleaseIsCalledThenReleaseIsSet() {
        regularSprint.setState(new FinishedState(regularSprint, new NotificationService(new NotificationExecutor())));
        regularSprint.getPipeline().setState(new com.avans.sofa3devops.domainServices.pipelineStatePattern.FinishedState());
        reviewSprint.setState(new FinishedState(reviewSprint, new NotificationService(new NotificationExecutor())));
        reviewSprint.getPipeline().setState(new com.avans.sofa3devops.domainServices.pipelineStatePattern.FinishedState());
        
        regularSprint.addRelease(new Release(regularSprint,regularSprint.getPipeline()));
        reviewSprint.addRelease(new Release(reviewSprint,reviewSprint.getPipeline()));

        assertThat(regularSprint.getReleases()).hasSize(1);
        assertThat(reviewSprint.getReleases()).hasSize(1);
    }

    @Test
    void givenSprintNotInFinishedStateWithPipelineInFinishedStateWhenAddReleaseIsCalledThenReleaseIsSet() {
        regularSprint.getPipeline().setState(new com.avans.sofa3devops.domainServices.pipelineStatePattern.FinishedState());
        reviewSprint.getPipeline().setState(new com.avans.sofa3devops.domainServices.pipelineStatePattern.FinishedState());

        regularSprint.addRelease(new Release(regularSprint,regularSprint.getPipeline()));
        reviewSprint.addRelease(new Release(reviewSprint,reviewSprint.getPipeline()));

        assertThat(regularSprint.getReleases()).hasSize(0);
        assertThat(reviewSprint.getReleases()).hasSize(0);
    }

    @Test
    void givenSprintInFinishedStateWithPipelineNotInFinishedStateWhenAddReleaseIsCalledThenReleaseIsSet() {
        regularSprint.setState(new FinishedState(regularSprint, new NotificationService(new NotificationExecutor())));
        reviewSprint.setState(new FinishedState(reviewSprint, new NotificationService(new NotificationExecutor())));

        regularSprint.addRelease(new Release(regularSprint,regularSprint.getPipeline()));
        reviewSprint.addRelease(new Release(reviewSprint,reviewSprint.getPipeline()));

        assertThat(regularSprint.getReleases()).hasSize(0);
        assertThat(reviewSprint.getReleases()).hasSize(0);
    }
}
