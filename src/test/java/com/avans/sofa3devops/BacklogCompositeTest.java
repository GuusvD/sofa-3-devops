package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Activity;
import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class BacklogCompositeTest {
    User createdByUser = new User();
    User assignedToUser = new User();

    @Test
    void lala() {}

    @Test
    void FinishedEqualsFalseWhenWhenNotInDoneState() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser,assignedToUser);

        item.setFinished();

        assertFalse(item.getFinished());

    }

    @Test
    void FinishedEqualsTrueAfterSetFinishedIsCalledWithNoActivities() throws InvalidStateException {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser,assignedToUser);
//        item.doingState();
//        item.readyForTestingState();
//        item.testingState();
//        item.testedState();
//        item.doneState();
        // shortcut state for Arrange
        item.setState(new DoneState(item));

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void FinishedEqualsTrueWithOneActivityThatIsFinished() throws InvalidStateException {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser,assignedToUser);
        Activity activity = new Activity("ActivityItem", createdByUser,assignedToUser);
        item.addActivity(activity);
        activity.setFinished();
        // shortcut state for Arrange
        item.setState(new DoneState(item));

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void FinishedEqualsFalseWithOneActivityThatIsNotFinished() throws InvalidStateException {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser,assignedToUser);
        Activity activity = new Activity("ActivityItem", createdByUser,assignedToUser);
        item.addActivity(activity);
        // shortcut state for Arrange
        item.setState(new DoneState(item));

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void FinishedEqualsTrueWithTwoActivitiesAndBothAreFinished() throws InvalidStateException {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser,assignedToUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser,assignedToUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser,assignedToUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityOne.setFinished();
        activityTwo.setFinished();
        // shortcut state for Arrange
        item.setState(new DoneState(item));

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void FinishedEqualsFalseWithTwoActivitiesAndOnlyFirstIsFinished() throws InvalidStateException {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser,assignedToUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser,assignedToUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser,assignedToUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityOne.setFinished();
        // shortcut state for Arrange
        item.setState(new DoneState(item));

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void FinishedEqualsFalseWithTwoActivitiesAndOnlySecondIsFinished() throws InvalidStateException {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser,assignedToUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser,assignedToUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser,assignedToUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityTwo.setFinished();
        // shortcut state for Arrange
        item.setState(new DoneState(item));

        item.setFinished();

        assertFalse(item.getFinished());
    }



}
