package com.avans.sofa3devops.domainServices.pipelineStatePattern;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.threadObserverPattern.NotificationService;

import java.util.Observable;

public class InitialState extends Observable implements IPipelineState {
    private final Pipeline pipeline;
    private final NotificationService service;

    public InitialState(Pipeline pipeline, NotificationService service) {
        this.pipeline = pipeline;
        this.service = service;

        this.addObserver(service);
    }

    @Override
    public void executedState() {
        pipeline.setState(new ExecutedState(pipeline, service));
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

        setChanged();
        notifyObservers();
    }
}
