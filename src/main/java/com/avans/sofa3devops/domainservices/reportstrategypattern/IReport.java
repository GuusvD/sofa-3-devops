package com.avans.sofa3devops.domainservices.reportstrategypattern;

import com.avans.sofa3devops.domain.Project;

public interface IReport {
    void printReport(Project project);
}