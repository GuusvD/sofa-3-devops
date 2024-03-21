package com.avans.sofa3devops.domain;

import com.avans.sofa3devops.domainServices.sprintFactoryPattern.ISprint;

import java.time.LocalDateTime;
import java.util.UUID;

public class Release {
    private final UUID id;
    private final ISprint sprint;
    private final Pipeline pipeline;
    private final LocalDateTime released;

    public Release(ISprint sprint, Pipeline pipeline) {
        this.id = UUID.randomUUID();
        this.sprint = sprint;
        this.pipeline = pipeline;
        this.released = LocalDateTime.now();
    }
}
