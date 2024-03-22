package com.avans.sofa3devops.domainservices.gitstrategypattern;

import java.util.logging.Logger;

public class GitLab implements IGitCommands {
    private final Logger logger;

    public GitLab(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void pull() {
        logger.info("GitLab: Pulling code!");
    }

    @Override
    public void push() {
        logger.info("GitLab: Pushing code!");
    }

    @Override
    public void commit() {
        logger.info("GitLab: Committing code!");
    }

    @Override
    public void status() {
        logger.info("GitLab: Retrieving status!");
    }

    @Override
    public void checkout() {
        logger.info("GitLab: Checking out!");
    }

    @Override
    public void stash() {
        logger.info("GitLab: Stashing code!");
    }
}
