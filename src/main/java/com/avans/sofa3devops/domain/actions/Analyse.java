package com.avans.sofa3devops.domain.actions;
import org.springframework.stereotype.Component;

import com.avans.sofa3devops.domain.Release;
import com.avans.sofa3devops.domainServices.compositeInterfaces.IPipeComponent;

import java.util.logging.Logger;

@Component
public class Analyse implements IPipeComponent {

    private final String name = getClass().getName();
    private final Logger logger = Logger.getLogger(getClass().getName());
    @Override
    public void start(Release release) {

    }

    @Override
    public void run() {

    }

    @Override
    public void build() {

    }

    @Override
    public void stop() {

    }
}