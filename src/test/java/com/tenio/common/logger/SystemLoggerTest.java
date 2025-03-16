package com.tenio.common.logger;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SystemLoggerTest {

    private static class TestSystemLogger extends SystemLogger {
        // Concrete implementation for testing
    }

    private TestSystemLogger logger;

    @BeforeEach
    void setUp() {
        logger = new TestSystemLogger();
    }

    @Test
    void debugEvent_withNoExtraMessage_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.debugEvent("TEST_EVENT"));
    }

    @Test
    void debugEvent_withMultipleMessages_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.debugEvent("TEST_EVENT", "msg1", "msg2", "msg3"));
    }

    @Test
    void debug_withNoExtraMessage_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.debug("DEBUG_TYPE"));
    }

    @Test
    void debug_withMultipleMessages_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.debug("DEBUG_TYPE", "msg1", "msg2", "msg3"));
    }

    @Test
    void trace_withNoExtraMessage_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.trace("TRACE_TYPE"));
    }

    @Test
    void trace_withMultipleMessages_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.trace("TRACE_TYPE", "msg1", "msg2", "msg3"));
    }

    @Test
    void trace_withFullParameters_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.trace("TestClass", "subWhere", "TAG", "Test message"));
    }

    @Test
    void isTraceEnabled_shouldReturnBoolean() {
        boolean result = logger.isTraceEnabled();
        assertNotNull(result);
    }

    @Test
    void isDebugEnabled_shouldReturnBoolean() {
        boolean result = logger.isDebugEnabled();
        assertNotNull(result);
    }

    @Test
    void debugEvent_withNullType_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.debugEvent(null, "msg"));
    }

    @Test
    void debug_withNullType_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.debug(null, "msg"));
    }

    @Test
    void trace_withNullParameters_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.trace(null, null, null, null));
    }
} 