package com.avans.sofa3devops.domainservices.pipelinestatepattern;

import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;

public interface IPipelineState {
    void executedState() throws InvalidStateException;

    void finishedState() throws InvalidStateException;

    void failedState() throws InvalidStateException;

    void cancelledState() throws InvalidStateException;
}
