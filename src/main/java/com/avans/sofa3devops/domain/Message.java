package com.avans.sofa3devops.domain;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Message {
    private UUID id;
    private String body;
    private User creator;
    private List<Message> responses;
    private Date sent;
}
