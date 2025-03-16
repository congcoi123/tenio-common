package com.tenio.common.configuration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class EnvironmentVariableResolverTest {

    private static final String TEST_VAR_NAME = "TENIO_TEST_VAR";
    private static final String TEST_VAR_VALUE = "test_value";
    private static final String TEST_PROP_NAME = "tenio.test.prop";
    private static final String TEST_PROP_VALUE = "prop_value";

    @BeforeEach
    void setUp() {
        // Set up test environment variable and system property
        setEnv(TEST_VAR_NAME, TEST_VAR_VALUE);
        System.setProperty(TEST_PROP_NAME, TEST_PROP_VALUE);
    }

    @AfterEach
    void tearDown() {
        // Clean up test environment variable and system property
        setEnv(TEST_VAR_NAME, null);
        System.clearProperty(TEST_PROP_NAME);
    }

    @Test
    void testResolveSimpleEnvVar() {
        String value = "${" + TEST_VAR_NAME + "}";
        assertEquals(TEST_VAR_VALUE, EnvironmentVariableResolver.resolve(value));
    }

    @Test
    void testResolveEnvVarWithDefault() {
        String value = "${NONEXISTENT_VAR:default_value}";
        assertEquals("default_value", EnvironmentVariableResolver.resolve(value));
    }

    @Test
    void testResolveSystemProperty() {
        String value = "${" + TEST_PROP_NAME + "}";
        assertEquals(TEST_PROP_VALUE, EnvironmentVariableResolver.resolve(value));
    }

    @Test
    void testResolveMultipleVars() {
        String value = "prefix_${" + TEST_VAR_NAME + "}_middle_${" + TEST_PROP_NAME + "}_suffix";
        String expected = "prefix_" + TEST_VAR_VALUE + "_middle_" + TEST_PROP_VALUE + "_suffix";
        assertEquals(expected, EnvironmentVariableResolver.resolve(value));
    }

    @Test
    void testResolveNonExistentVar() {
        String value = "${NONEXISTENT_VAR}";
        assertEquals("", EnvironmentVariableResolver.resolve(value));
    }

    @Test
    void testResolveNull() {
        assertNull(EnvironmentVariableResolver.resolve(null));
    }

    @Test
    void testResolveNoVars() {
        String value = "plain text without variables";
        assertEquals(value, EnvironmentVariableResolver.resolve(value));
    }

    @Test
    void testResolveAllInMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("string", "${" + TEST_VAR_NAME + "}");
        values.put("number", 42);
        values.put("nested", "${" + TEST_PROP_NAME + "}");

        Map<String, Object> resolved = EnvironmentVariableResolver.resolveAll(values);

        assertEquals(TEST_VAR_VALUE, resolved.get("string"));
        assertEquals(42, resolved.get("number"));
        assertEquals(TEST_PROP_VALUE, resolved.get("nested"));
    }

    // Mock environment variable by setting system property instead
    // This avoids the security issues with modifying environment variables
    private void setEnv(String name, String value) {
        if (value == null) {
            System.clearProperty(name);
        } else {
            System.setProperty(name, value);
        }
    }
} 