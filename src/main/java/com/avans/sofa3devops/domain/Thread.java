package com.avans.sofa3devops.domain;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Thread {
    private UUID id;
    private String title;
    private String body;
    private List<Message> messages;
    private User createdBy;
    private Date created;
}
