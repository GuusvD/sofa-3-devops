package com.avans.sofa3devops.domainservices.gitstrategypattern;

import java.util.logging.Logger;

public class GitHub implements IGitCommands {
    private final Logger logger;

    public GitHub(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void pull() {
        logger.info("GitHub: Pulling code!");
    }

    @Override
    public void push() {
        logger.info("GitHub: Pushing code!");
    }

    @Override
    public void commit() {
        logger.info("GitHub: Committing code!");
    }

    @Override
    public void status() {
        logger.info("GitHub: Retrieving status!");
    }

    @Override
    public void checkout() {
        logger.info("GitHub: Checking out!");
    }

    @Override
    public void stash() {
        logger.info("GitHub: Stashing code!");
    }
}
