package com.tenio.common.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NullElementPoolExceptionTest {

    @Test
    void whenExceptionThrown_shouldHaveCorrectMessage() {
        String message = "Test error message";
        var exception = new NullElementPoolException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void whenExceptionThrown_shouldBeRuntimeException() {
        var exception = new NullElementPoolException("Test");
        assertTrue(exception instanceof RuntimeException);
    }
} 