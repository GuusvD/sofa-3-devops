package com.avans.sofa3devops.domain;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String email;
    private final String password;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
