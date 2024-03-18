package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.*;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Pdf;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Png;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class Sofa3DevopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sofa3DevopsApplication.class, args);

		List<IReport> reportStrategies = new ArrayList<>();
		reportStrategies.add(new Pdf());
		reportStrategies.add(new Png());

		Pipeline pipeline = new Pipeline();


		Project project = new Project("LANGUAGESCHOOL", reportStrategies, pipeline);
		project.printReports();

		User creator= new User();
		User developer = new User();

		BacklogItem item = new BacklogItem("US-1",creator );
		Activity activityOne = new Activity("AC-1-One",creator);
		Activity activityTwo = new Activity("AC-1-Two",creator);

		item.addActivity(activityOne);
		item.addActivity(activityTwo);

		item.getAllStories();

	}

}
