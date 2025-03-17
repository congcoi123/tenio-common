/*
The MIT License

Copyright (c) 2016-2023 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.tenio.common.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConfigurationExceptionTest {

    @Test
    void testConstructorWithAllParameters() {
        String message = "Test message";
        Throwable cause = new RuntimeException("Test cause");
        ConfigurationType type = TestConfigurationType.STRING_VALUE;
        ConfigurationException.ErrorType errorType = ConfigurationException.ErrorType.LOAD_ERROR;

        ConfigurationException exception = new ConfigurationException(message, cause, type, errorType);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(type, exception.getConfigurationType());
        assertEquals(errorType, exception.getErrorType());
    }

    @Test
    void testConstructorWithNullCause() {
        String message = "Test message";
        ConfigurationType type = TestConfigurationType.INT_VALUE;
        ConfigurationException.ErrorType errorType = ConfigurationException.ErrorType.PARSE_ERROR;

        ConfigurationException exception = new ConfigurationException(message, null, type, errorType);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(type, exception.getConfigurationType());
        assertEquals(errorType, exception.getErrorType());
    }

    @Test
    void testConstructorWithNullParameters() {
        ConfigurationException exception = new ConfigurationException(null, null, null, null);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
        assertNull(exception.getConfigurationType());
        assertNull(exception.getErrorType());
    }

    @Test
    void testErrorTypeValues() {
        assertEquals(5, ConfigurationException.ErrorType.values().length);
        assertEquals(ConfigurationException.ErrorType.LOAD_ERROR, ConfigurationException.ErrorType.valueOf("LOAD_ERROR"));
        assertEquals(ConfigurationException.ErrorType.PARSE_ERROR, ConfigurationException.ErrorType.valueOf("PARSE_ERROR"));
        assertEquals(ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE, ConfigurationException.ErrorType.valueOf("MISSING_REQUIRED_VALUE"));
        assertEquals(ConfigurationException.ErrorType.INVALID_VALUE_TYPE, ConfigurationException.ErrorType.valueOf("INVALID_VALUE_TYPE"));
        assertEquals(ConfigurationException.ErrorType.VALIDATION_FAILED, ConfigurationException.ErrorType.valueOf("VALIDATION_FAILED"));
    }

    @Test
    void testWithSpecificErrorType() {
        String message = "Missing required value";
        TestConfigurationType type = TestConfigurationType.STRING_VALUE;
        ConfigurationException.ErrorType errorType = ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE;

        ConfigurationException exception = new ConfigurationException(message, null, type, errorType);

        assertEquals(message, exception.getMessage());
        assertEquals(type, exception.getConfigurationType());
        assertEquals(errorType, exception.getErrorType());
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
            null,
            TestConfigurationType.BOOLEAN_VALUE,
            ConfigurationException.ErrorType.VALIDATION_FAILED
        );

        String toString = exception.toString();
        
        // Just check that toString returns something reasonable
        assertTrue(toString.length() > 0, "toString should not be empty");
        assertTrue(toString.contains("Test message"), "toString should contain the message");
    }
} 