package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Activity;
import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.backlogstatepattern.DoneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class BacklogCompositeTest {
    private User createdByUser;

    @BeforeEach
    void setup() {
        createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
    }

    @Test
    void givenBacklogItemNotInDoneStateWhenSetFinishedIsCalledThenFinishedEqualsFalse() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void givenBacklogItemInDoneStateWhenSetFinishedIsCalledThenFinishedEqualsTrue() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        item.setState(new DoneState());

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void givenBacklogItemInDoneStateWithOneActivityThatIsFinishedWhenSetFinishedIsCalledThenFinishedEqualsTrue() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activity = new Activity("ActivityItem", createdByUser);
        item.addActivity(activity);
        activity.setState(new DoneState());
        activity.setFinished();
        item.setState(new DoneState());

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void givenBacklogItemWithOneActivityThatIsNotFinishedWhenSetFinishedISCalledThenFinishedEqualsFalse() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activity = new Activity("ActivityItem", createdByUser);
        item.addActivity(activity);
        item.setState(new DoneState());

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void givenBacklogItemWithTwoActivitiesWhichBothAreFinishedWhenSetFinishedIsCalledThenFinishedEqualsTrue() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityOne.setState(new DoneState());
        activityTwo.setState(new DoneState());
        activityOne.setFinished();
        activityTwo.setFinished();
        item.setState(new DoneState());

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void givenBacklogItemWithTwoActivitiesAndOnlyFirstIsFinishedWhenSetFinishedIsCalledThenFinishedEqualsFalse() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityOne.setFinished();
        item.setState(new DoneState());

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void givenBacklogItemWithTwoActivitiesAndOnlySecondIsFinishedWhenSetFinishedIsCalledThenFinishedEqualsFalse() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityTwo.setFinished();
        item.setState(new DoneState());

        item.setFinished();

        assertFalse(item.getFinished());
    }
}
