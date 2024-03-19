package com.avans.sofa3devops.domainServices.pipelineStatePattern;

import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class FinishedState implements IPipelineState {
    @Override
    public void executedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'executed' state!");
    }

    @Override
    public void finishedState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'finished' state!");
    }

    @Override
    public void failedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'failed' state!");
    }

    @Override
    public void cancelledState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'cancelled' state!");
    }
}
