package com.avans.sofa3devops.domainServices.compositeInterfaces;

import com.avans.sofa3devops.domain.Release;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public interface IPipeComponent {
    boolean execute() throws InvalidStateException;
    void print();
}
