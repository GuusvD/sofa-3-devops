package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.Thread;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.backlogStatePattern.DoneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BacklogItemTest {

    private User createdByUser;

    @BeforeEach
    void setup() {
        createdByUser = new User("John Doe", "j.doe@gmail.com", "Password1234");
    }
    @Test
    void givenBacklogItemWithThreadWhenThreadIsAddedThenListHasValueOne() {
        BacklogItem item = new BacklogItem("BacklogItem", createdByUser);
        Thread thread = new Thread("Title","Body",item,createdByUser);

        item.addThread(thread);

        assertThat(item.getThreads()).hasSize(1);
        assertThat(thread.getBacklogItem()).isEqualTo(item);
    }

    @Test
    void givenBacklogItemWhenThreadIsAddedTwiceThenListHasValueOne() {
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