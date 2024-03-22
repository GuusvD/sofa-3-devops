package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainservices.sprintfactorypattern.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
public class FactoryPatternTest {
    @Test
    void givenSprintFactoryWhenCreateRegularSprintIsCalledThenRegularSprintClassIsReturned() throws Exception {

        ISprintFactory factory = new SprintFactory();

        ISprint rSprint = factory.createRegularSprint(1, new Date(), new Date(), new User("John Doe", "j.doe@gmail.com", "Password1234"));

        assertInstanceOf(RegularSprint.class, rSprint);
    }

    @Test
    void givenSprintFactoryWhenCreateReviewSprintIsCalledThenReviewSprintClassIsReturned() throws Exception {
        ISprintFactory factory = new SprintFactory();

        ISprint rSprint = factory.createReviewSprint(1, new Date(), new Date(), new User("John Doe", "j.doe@gmail.com", "Password1234"));

        assertInstanceOf(ReviewSprint.class, rSprint);
    }
}
