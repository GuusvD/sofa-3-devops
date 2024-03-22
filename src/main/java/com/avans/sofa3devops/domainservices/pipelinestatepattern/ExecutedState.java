package com.avans.sofa3devops.domainservices.pipelinestatepattern;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domainservices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainservices.threadobserverpattern.NotificationService;

import java.util.Observable;

public class ExecutedState extends Observable implements IPipelineState {
    private final Pipeline pipeline;
    private final NotificationService service;

    public ExecutedState(Pipeline pipeline, NotificationService service) {
        this.pipeline = pipeline;
        this.service = service;

        this.addObserver(service);
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
        pipeline.setState(new FailedState(pipeline, service));

        setChanged();
        notifyObservers();
    }

    @Override
    public void cancelledState() throws InvalidStateException {
        throw new InvalidStateException("Cannot transition to 'cancelled' state!");
    }
}
