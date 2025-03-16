package com.tenio.common.logger;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AbstractLoggerTest {

    private static class TestLogger extends AbstractLogger {
        // Concrete implementation for testing
    }

    private TestLogger logger;

    @BeforeEach
    void setUp() {
        logger = new TestLogger();
    }

    @Test
    void buildgen_shouldCreateStringBuilder() {
        var result = logger.buildgen("test", 123, true);
        assertEquals("test123true", result.toString());
    }

    @Test
    void info_withStringTagAndObject_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.info("TestTag", "Test message"));
    }

    @Test
    void info_withStringBuilderTagAndMsg_shouldNotThrowException() {
        var tag = logger.buildgen("TestTag");
        var msg = logger.buildgen("Test message");
        assertDoesNotThrow(() -> logger.info(tag, msg));
    }

    @Test
    void error_withThrowable_shouldNotThrowException() {
        var exception = new RuntimeException("Test exception");
        assertDoesNotThrow(() -> logger.error(exception));
    }

    @Test
    void error_withVarargs_shouldNotThrowException() {
        assertDoesNotThrow(() -> logger.error("Error 1", "Error 2", "Error 3"));
    }

    @Test
    void isErrorEnabled_shouldReturnBoolean() {
        boolean result = logger.isErrorEnabled();
        assertNotNull(result);
    }

    @Test
    void isInfoEnabled_shouldReturnBoolean() {
        boolean result = logger.isInfoEnabled();
        assertNotNull(result);
    }

    @Test
    void info_withMultipleParameters_shouldNotThrowException() {
        var where = logger.buildgen("TestClass");
        var tag = logger.buildgen("TestTag");
        var msg = logger.buildgen("Test message");
        assertDoesNotThrow(() -> logger.info(where, tag, msg));
    }

    @Test
    void error_withThrowableAndExtra_shouldNotThrowException() {
        var exception = new RuntimeException("Test exception");
        var extra = logger.buildgen("Extra info");
        assertDoesNotThrow(() -> logger.error(exception, extra));
    }
} 