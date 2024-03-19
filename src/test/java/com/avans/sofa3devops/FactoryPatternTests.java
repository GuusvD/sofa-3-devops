package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.User;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
public class FactoryPatternTests {
    @Test
    void createRegularSprintWithFactory() {
        // Arrange
        ISprintFactory factory = new SprintFactory();
        // Act
        ISprint rSprint = factory.createRegularSprint(1, new Date(), new Date(), new User(UUID.randomUUID(), "John Doe", "j.doe@gmail.com", "1234"));
        // Assert
        assertInstanceOf(RegularSprint.class, rSprint);
    }

    @Test
    void createReviewSprintWithFactory() {
        // Arrange
        ISprintFactory factory = new SprintFactory();
        // Act
        ISprint rSprint = factory.createReviewSprint(1, new Date(), new Date(), new User(UUID.randomUUID(), "John Doe", "j.doe@gmail.com", "1234"));
        // Assert
        assertInstanceOf(ReviewSprint.class, rSprint);
    }

}
