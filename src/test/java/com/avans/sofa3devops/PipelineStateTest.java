package com.avans.sofa3devops;

import com.avans.sofa3devops.domain.Pipeline;
import com.avans.sofa3devops.domainServices.exceptions.InvalidStateException;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.CancelledState;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.ExecutedState;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.FailedState;
import com.avans.sofa3devops.domainServices.pipelineStatePattern.FinishedState;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PipelineStateTest {
    // Correct state switching
    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateThenSwitchToExecutedState() throws InvalidStateException {
        Pipeline pipeline = new Pipeline("Test");

        pipeline.executedState();

        assertThat(pipeline.getState()).isInstanceOf(ExecutedState.class);
    }

    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateThenSwitchToCancelledState() throws InvalidStateException {
        Pipeline pipeline = new Pipeline("Test");

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateThenSwitchToFinishedState() throws InvalidStateException {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();

        pipeline.finishedState();

        assertThat(pipeline.getState()).isInstanceOf(FinishedState.class);
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateThenSwitchToFailedState() throws InvalidStateException {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();

        pipeline.failedState();

        assertThat(pipeline.getState()).isInstanceOf(FailedState.class);
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateThenSwitchToExecutedState() throws InvalidStateException {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();

        pipeline.executedState();

        assertThat(pipeline.getState()).isInstanceOf(ExecutedState.class);
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateThenSwitchToCancelledState() throws InvalidStateException {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
    }
}
