package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Activity;
import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.avans.sofa3devops.domain.Thread;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;



@SpringBootTest
public class BacklogCompositeTest {
    private User createdByUser;

    @BeforeEach
    void setup() {
        createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
    }

    @Test
    void FinishedEqualsFalseWhenWhenNotInDoneState() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);

        item.setFinished();

        assertFalse(item.getFinished());

    }

    @Test
    void FinishedEqualsTrueAfterSetFinishedIsCalledWithNoActivities() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        item.setState(new DoneState(item));

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void FinishedEqualsTrueWithOneActivityThatIsFinished() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activity = new Activity("ActivityItem", createdByUser);
        item.addActivity(activity);
        activity.setFinished();
        item.setState(new DoneState(item));

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void FinishedEqualsFalseWithOneActivityThatIsNotFinished() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activity = new Activity("ActivityItem", createdByUser);
        item.addActivity(activity);
        item.setState(new DoneState(item));

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void FinishedEqualsTrueWithTwoActivitiesAndBothAreFinished() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityOne.setFinished();
        activityTwo.setFinished();
        item.setState(new DoneState(item));

        item.setFinished();

        assertTrue(item.getFinished());
    }

    @Test
    void FinishedEqualsFalseWithTwoActivitiesAndOnlyFirstIsFinished() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityOne.setFinished();
        item.setState(new DoneState(item));

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void FinishedEqualsFalseWithTwoActivitiesAndOnlySecondIsFinished() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Activity activityOne = new Activity("ActivityItemOne", createdByUser);
        Activity activityTwo = new Activity("ActivityItemTwo", createdByUser);
        item.addActivity(activityOne);
        item.addActivity(activityTwo);
        activityTwo.setFinished();
        item.setState(new DoneState(item));

        item.setFinished();

        assertFalse(item.getFinished());
    }

    @Test
    void GivenBacklogItemWhenThreadIsAddedThenListHasValueOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title","Body",item,createdByUser);

        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(1);
        assertThat(thread.getBacklogItem()).isEqualTo(item);
    }

    @Test
    void GivenBacklogItemWhenThreadIsAddedTwiceThenListHasValueOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title","Body",item,createdByUser);

        item.addThread(thread);
        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(1);
        assertThat(thread.getBacklogItem()).isEqualTo(item);
    }
    
    @Test
    void givenFinishedBackLogItemWhenThreadIsAddedThenItemIsNotAdded() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        item.setState(new DoneState(item));
        item.setFinished();
        Thread thread = new Thread("Title","Body",item,createdByUser);

        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(0);

    }



}
