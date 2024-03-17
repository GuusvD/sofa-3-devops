package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domain.Project;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Pdf;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Png;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class ReportStrategyTests {
    private final String directoryPath = "./src/main/java/com/avans/sofa3devops/domainServices/reportStrategyPattern";
    private final String name = "TestFile";
    private final String startsWith = name + "_report";

    @AfterEach
    void tearDown() {
        // Iterate through each directory and remove files starting with "TestFile_Report"
        for (String directory : Arrays.asList(directoryPath + "/reports/pdf/", directoryPath + "/reports/png/")) {
            File dir = new File(directory);
            if (dir.exists()) {
                File[] files = dir.listFiles((dir1, name) -> name.startsWith(startsWith));
                if (files != null) {
                    for (File file : files) {
                        file.delete();
                    }
                }
            }
        }
    }

    @Test
    void checkIfReportsAreCreatedFromProjectInPdfAndPng() {
        // Arrange
        List<IReport> reportStrategies = Arrays.asList(new Pdf(), new Png());
        Pipeline pipeline = new Pipeline();
        Project project = new Project(name, reportStrategies, pipeline);

        // Act
        project.printReports();

        // Assert
        // Check if any files matching the patterns exist
        assertTrue(Arrays.stream(Objects.requireNonNull(new File(directoryPath + "/reports/pdf/").listFiles()))
                .anyMatch(file -> file.getName().startsWith(startsWith)));
        assertTrue(Arrays.stream(Objects.requireNonNull(new File(directoryPath + "/reports/png/").listFiles()))
                .anyMatch(file -> file.getName().startsWith(startsWith)));
    }
}
