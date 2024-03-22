package com.avans.sofa3devops.domain;

import java.util.UUID;

public class Document {
    private final UUID id;
    private final String name;

    public Document() {
        this.id = UUID.randomUUID();
        this.name = "ReviewDocument";
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
