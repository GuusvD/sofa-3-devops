package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.BacklogItem;
import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IItemComponent;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class BacklogCompositeTest {

    // Backlog Tests
    //setFinished Tests
    @Test
    void lala() {}

    @Test
    void completedWithNoActivities() {
        User createdByUser = new User();
        User assignedTo = new User();
        IItemComponent item = new BacklogItem("BacklogItem", createdByUser,assignedTo);

        item.setFinished();

        assertTrue(item.getFinished());

    }

}
