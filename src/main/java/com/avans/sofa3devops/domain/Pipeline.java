package com.avans.sofa3devops.domain;

import java.util.List;

public class Pipeline {
    private String name;
    private List<Action> actions;
    private List<Release> releases;
}
