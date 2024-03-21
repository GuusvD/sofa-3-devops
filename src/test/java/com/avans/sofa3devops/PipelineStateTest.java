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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PipelineStateTest {
    // Correct state switching
    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateThenSwitchToExecutedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test");

        pipeline.executedState();

        assertThat(pipeline.getState()).isInstanceOf(ExecutedState.class);
    }

    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateThenSwitchToCancelledState() throws Exception {
        Pipeline pipeline = new Pipeline("Test");

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateThenSwitchToFinishedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();

        pipeline.finishedState();

        assertThat(pipeline.getState()).isInstanceOf(FinishedState.class);
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateThenSwitchToFailedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();

        pipeline.failedState();

        assertThat(pipeline.getState()).isInstanceOf(FailedState.class);
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateThenSwitchToExecutedState() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();

        pipeline.executedState();

        assertThat(pipeline.getState()).isInstanceOf(ExecutedState.class);
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateThenSwitchToCancelledState() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();

        pipeline.cancelledState();

        assertThat(pipeline.getState()).isInstanceOf(CancelledState.class);
    }

    // Incorrect state switching
    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithInitialStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Cannot transition to 'failed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateToCancelledStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::cancelledState);
        assertEquals("Cannot transition to 'cancelled' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToExecutedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::executedState);
        assertEquals("Cannot transition to 'executed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Cannot transition to 'failed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToCancelledStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::cancelledState);
        assertEquals("Cannot transition to 'cancelled' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToExecutedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::executedState);
        assertEquals("Cannot transition to 'executed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Cannot transition to 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Cannot transition to 'failed' state!", exception.getMessage());
    }

    // Same state switching
    @Test
    void givenPipelineWithExecutedStateWhenSwitchingStateToExecutedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::executedState);
        assertEquals("Already in 'executed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFinishedStateWhenSwitchingStateToFinishedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.finishedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::finishedState);
        assertEquals("Already in 'finished' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithFailedStateWhenSwitchingStateToFailedStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::failedState);
        assertEquals("Already in 'failed' state!", exception.getMessage());
    }

    @Test
    void givenPipelineWithCancelledStateWhenSwitchingStateToCancelledStateThenThrowException() throws Exception {
        Pipeline pipeline = new Pipeline("Test");
        pipeline.executedState();
        pipeline.failedState();
        pipeline.cancelledState();

        InvalidStateException exception = assertThrows(InvalidStateException.class, pipeline::cancelledState);
        assertEquals("Already in 'cancelled' state!", exception.getMessage());
    }
}
