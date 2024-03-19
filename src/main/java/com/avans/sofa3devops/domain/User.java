package com.avans.sofa3devops.domain;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
