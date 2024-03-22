package com.avans.sofa3devops.domainservices.gitstrategypattern;

public interface IGitCommands {
    void pull();

    void push();

    void commit();

    void status();

    void checkout();

    void stash();
}
