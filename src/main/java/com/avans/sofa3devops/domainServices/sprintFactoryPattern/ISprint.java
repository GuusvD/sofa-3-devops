package com.avans.sofa3devops.domainServices.sprintFactoryPattern;

public interface ISprint {
    private int number;
    private DateTime start;
    private DateTime end;
    private List<BacklogItem> backlog;
    private List<User> developers;
    private Document document;
    private Release release;
    private Pipeline pipeline;
}
