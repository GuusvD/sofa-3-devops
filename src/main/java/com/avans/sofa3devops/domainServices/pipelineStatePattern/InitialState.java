package com.avans.sofa3devops.domainServices.pipelineStatePattern;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

public class InitialState implements IPipelineState {
    private final Pipeline pipeline;

    public InitialState(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void executedState() {
        pipeline.setState(new ExecutedState(pipeline));
    }

    @Override
    public void finishedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'finished' state!");
    }

    @Override
    public void failedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'failed' state!");
    }

    @Override
    public void cancelledState() {
        pipeline.setState(new CancelledState());
    }
}
