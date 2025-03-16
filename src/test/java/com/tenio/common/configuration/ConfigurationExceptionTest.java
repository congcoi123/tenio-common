package com.tenio.common.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConfigurationExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String message = "Test error message";
        ConfigurationType type = TestConfigurationType.STRING_VALUE;
        ConfigurationException.ErrorType errorType = ConfigurationException.ErrorType.VALIDATION_FAILED;

        ConfigurationException exception = new ConfigurationException(message, type, errorType);

        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getConfigurationType());
        assertEquals(errorType, exception.getErrorType());
        assertNull(exception.getCause());
    }

    @Test
    void testConstructorWithCause() {
        String message = "Test error message";
        Throwable cause = new RuntimeException("Test cause");
        ConfigurationType type = TestConfigurationType.INT_VALUE;
        ConfigurationException.ErrorType errorType = ConfigurationException.ErrorType.PARSE_ERROR;

        ConfigurationException exception = new ConfigurationException(message, cause, type, errorType);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(type, exception.getConfigurationType());
        assertEquals(errorType, exception.getErrorType());
    }

    @Test
    void testConstructorWithNullValues() {
        ConfigurationException exception = new ConfigurationException(null, null, null);

        assertNull(exception.getMessage());
        assertNull(exception.getConfigurationType());
        assertNull(exception.getErrorType());
        assertNull(exception.getCause());
    }

    @Test
    void testErrorTypes() {
        // Test all error types are accessible and unique
        ConfigurationException.ErrorType[] errorTypes = ConfigurationException.ErrorType.values();
        assertEquals(5, errorTypes.length);
        
        assertTrue(java.util.Arrays.asList(errorTypes)
            .contains(ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE));
        assertTrue(java.util.Arrays.asList(errorTypes)
            .contains(ConfigurationException.ErrorType.INVALID_VALUE_TYPE));
        assertTrue(java.util.Arrays.asList(errorTypes)
            .contains(ConfigurationException.ErrorType.VALIDATION_FAILED));
        assertTrue(java.util.Arrays.asList(errorTypes)
            .contains(ConfigurationException.ErrorType.LOAD_ERROR));
        assertTrue(java.util.Arrays.asList(errorTypes)
            .contains(ConfigurationException.ErrorType.PARSE_ERROR));
    }

    @Test
    void testExceptionChaining() {
        Exception rootCause = new IllegalArgumentException("Root cause");
        Exception intermediateCause = new RuntimeException("Intermediate cause", rootCause);
        
        ConfigurationException exception = new ConfigurationException(
            "Top level message",
            intermediateCause,
            TestConfigurationType.FLOAT_VALUE,
            ConfigurationException.ErrorType.LOAD_ERROR
        );

        assertEquals(intermediateCause, exception.getCause());
        assertEquals(rootCause, exception.getCause().getCause());
    }

    @Test
    void testToString() {
        ConfigurationException exception = new ConfigurationException(
            "Test message",
            TestConfigurationType.BOOLEAN_VALUE,
            ConfigurationException.ErrorType.VALIDATION_FAILED
        );

        String toString = exception.toString();
        assertTrue(toString.contains("ConfigurationException"), "toString should contain class name");
        assertTrue(toString.contains("Test message"), "toString should contain message");
        
        // Print the actual toString value for debugging
        System.out.println("Exception toString: " + toString);
        
        // Just check that toString returns something reasonable
        assertTrue(toString.length() > 20, "toString should return a reasonable string");
    }
} 