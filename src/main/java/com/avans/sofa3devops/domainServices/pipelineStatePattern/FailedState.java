package com.avans.sofa3devops.domainServices.pipelineStatePattern;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

import java.util.Observable;

public class FailedState extends Observable implements IPipelineState {
    private final Pipeline pipeline;

    public FailedState(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void executedState() throws InvalidStateException {
        pipeline.setState(new ExecutedState(pipeline));
    }

    @Override
    public void finishedState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'finished' state!");
    }

    @Override
    public void failedState() throws InvalidStateException {
        throw new InvalidStateException("Already in 'failed' state!");
    }

    @Override
    public void cancelledState() {
        pipeline.setState(new CancelledState());

        setChanged();
        notifyObservers();
    }
}
