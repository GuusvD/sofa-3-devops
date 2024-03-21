package com.avans.sofa3devops.domainServices.pipelineStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public interface IPipelineState {
    void executedState() throws InvalidStateException;
    void finishedState() throws InvalidStateException;
    void failedState() throws InvalidStateException;
    void cancelledState() throws InvalidStateException;
}
