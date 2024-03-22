package com.avans.sofa3devops.domainservices.compositeinterfaces;

import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;

public interface IPipeComponent {
    boolean execute() throws InvalidStateException;

    void print();
}
