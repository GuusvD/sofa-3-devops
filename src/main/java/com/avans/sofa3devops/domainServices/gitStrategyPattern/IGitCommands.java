package com.avans.sofa3devops.domainServices.gitStrategyPattern;

public interface IGitCommands {
    void pull();
    void push();
    void commit();
    void status();
    void checkout();
    void stash();
}
