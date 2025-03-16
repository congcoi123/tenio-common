package com.tenio.common.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RunningScheduledTaskExceptionTest {

    @Test
    void whenExceptionThrown_shouldBeRuntimeException() {
        var exception = new RunningScheduledTaskException();
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void whenExceptionThrown_shouldHaveNullMessage() {
        var exception = new RunningScheduledTaskException();
        assertNull(exception.getMessage());
    }
} 