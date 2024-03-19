package com.avans.sofa3devops.domainServices.compositeInterfaces;

import com.avans.sofa3devops.domain.Release;

public interface IPipeComponent {

    void start(Release release);
    void run();
    void build();
    void stop();

}
