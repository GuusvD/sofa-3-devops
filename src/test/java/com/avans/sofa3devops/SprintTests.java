package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.*;
import com.avans.sofa3devops.domainServices.sprintStatePattern.InProgressState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

import java.util.Date;

@SpringBootTest
public class SprintTests {
    private ReviewSprint reviewSprint;
    private RegularSprint regularSprint;
    private Date start;
    private Date end;
    private User user;

    @BeforeEach
    void setup() throws Exception {
        start = new Date();
        end = new Date();
        user = new User("John Doe", "j.doe@gmail.com", "Password1234");
        reviewSprint = new ReviewSprint(0, start, end, user);
        regularSprint = new RegularSprint(0, start, end, user);
    }

    @Test
    void givenSprintNotInCreatedStateWhenSprintEditIsCalledThenSprintIsNotEdited() {
        reviewSprint.setState(new InProgressState(reviewSprint));
        regularSprint.setState(new InProgressState(regularSprint));
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
        regularSprint.setState(new InProgressState(regularSprint));
        reviewSprint.setState(new InProgressState(reviewSprint));
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
        reviewSprint.setState(new InProgressState(reviewSprint));
        regularSprint.setState(new InProgressState(regularSprint));

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
        reviewSprint.setState(new InProgressState(reviewSprint));
        regularSprint.setState(new InProgressState(regularSprint));

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
        reviewSprint.setState(new InProgressState(reviewSprint));
        regularSprint.setState(new InProgressState(regularSprint));
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
}
