package com.tenio.common.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MsgPackOperationExceptionTest {

    @Test
    void whenExceptionThrown_shouldBeRuntimeException() {
        var wrappedException = new IllegalArgumentException("Test message");
        var exception = new MsgPackOperationException(wrappedException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void whenExceptionThrown_shouldWrapOriginalException() {
        var wrappedException = new IllegalArgumentException("Test message");
        var exception = new MsgPackOperationException(wrappedException);
        assertEquals(wrappedException, exception.getCause());
        assertEquals("java.lang.IllegalArgumentException: Test message", exception.getMessage());
    }

    @Test
    void whenExceptionThrown_shouldPreserveOriginalMessage() {
        String message = "Original error message";
        var wrappedException = new IllegalArgumentException(message);
        var exception = new MsgPackOperationException(wrappedException);
        assertTrue(exception.getMessage().contains(message));
    }
} 