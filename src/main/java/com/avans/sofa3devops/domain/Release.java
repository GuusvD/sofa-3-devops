package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.util.UUID;

public class Release {
    private UUID id;
    private ISprint sprint;
    private Pipeline pipeline;
}
