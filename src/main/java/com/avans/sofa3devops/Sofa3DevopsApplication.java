package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Project;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.IReport;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Pdf;
import com.avans.sofa3devops.domainServices.reportStrategyPattern.Png;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprintFactory;
import com.avans.sofa3devops.domainServices.sprintFactoryPattern.SprintFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Sofa3DevopsApplication {

	public static void main(String[] args) {
		SpringApplication.run(Sofa3DevopsApplication.class, args);

		List<IReport> reportStrategies = new ArrayList<>();
		reportStrategies.add(new Pdf());
		reportStrategies.add(new Png());


		Project project = new Project("LANGUAGESCHOOL", reportStrategies);
		project.printReports();
	}

}
