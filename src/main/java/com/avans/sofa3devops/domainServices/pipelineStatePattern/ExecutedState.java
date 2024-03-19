package com.avans.sofa3devops.domainServices.pipelineStatePattern;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class ExecutedState implements IPipelineState {
    private final Pipeline pipeline;

    public ExecutedState(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void executedState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'executed' state!");
    }

    @Override
    public void finishedState() {
        pipeline.setState(new FinishedState());
    }

    @Override
    public void failedState() {
        pipeline.setState(new FailedState(pipeline));
    }

    @Override
    public void cancelledState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'cancelled' state!");
    }
}
