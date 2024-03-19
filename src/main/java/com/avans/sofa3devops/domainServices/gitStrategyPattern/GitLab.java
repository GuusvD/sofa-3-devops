package com.avans.sofa3devops.domainServices.gitStrategyPattern;

import java.util.logging.Logger;

public class GitLab implements IGitCommands {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

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
