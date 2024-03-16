package com.avans.sofa3devops.domain;

import java.util.List;
import java.util.UUID;

public class BacklogItem {
    private UUID id;
    private List<Activity> activities;
    private User createdBy;
    private User assignedTo;
    private List<Thread> threads;
}
