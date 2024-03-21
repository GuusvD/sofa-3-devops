package com.avans.sofa3devops.domainServices.pipelineStatePattern;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;

import java.util.Observable;

public class ExecutedState extends Observable implements IPipelineState {
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

        setChanged();
        notifyObservers();
    }

    @Override
    public void failedState() {
        pipeline.setState(new FailedState(pipeline));

        setChanged();
        notifyObservers();
    }

    @Override
    public void cancelledState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'cancelled' state!");
    }
}
