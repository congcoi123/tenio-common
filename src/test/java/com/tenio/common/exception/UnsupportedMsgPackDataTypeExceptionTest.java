package com.tenio.common.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UnsupportedMsgPackDataTypeExceptionTest {

    @Test
    void whenExceptionThrown_shouldHaveCorrectMessage() {
        var exception = new UnsupportedMsgPackDataTypeException();
        String expectedMessage = "The data type is not supported. Please check out this available list: null, boolean, " +
                "byte, short, int, float, long, double, String, boolean[], byte[], short[], int[], float[], long[], " +
                "double[], String[].";
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void whenExceptionThrown_shouldBeRuntimeException() {
        var exception = new UnsupportedMsgPackDataTypeException();
        assertTrue(exception instanceof RuntimeException);
    }
} 