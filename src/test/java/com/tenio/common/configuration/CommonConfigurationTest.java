package com.tenio.common.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CommonConfigurationTest {

    private TestConfiguration configuration;
    @TempDir
    File tempDir;
    private Map<String, Object> testValues;

    @BeforeEach
    void setUp() {
        configuration = new TestConfiguration();
        testValues = new HashMap<>();
        testValues.put(TestConfigurationType.STRING_VALUE.name(), "test");
        testValues.put(TestConfigurationType.INT_VALUE.name(), "42");
        testValues.put(TestConfigurationType.FLOAT_VALUE.name(), "3.14");
        testValues.put(TestConfigurationType.BOOLEAN_VALUE.name(), "true");
    }

    @Test
    void testLoadValidConfiguration() throws Exception {
        File configFile = createTestConfig(Map.of(
            "SERVER_PORT", "8080",
            "DEBUG_MODE", "true",
            "SERVER_NAME", "Test Server",
            "MAX_PLAYERS", "100",
            "TICK_RATE", "30.5",
            "STRING_VALUE", "test string",
            "INT_VALUE", "42",
            "FLOAT_VALUE", "3.14"
        ));

        configuration.load(configFile.getAbsolutePath());

        assertEquals(8080, configuration.getInt(TestConfigurationType.SERVER_PORT));
        assertTrue(configuration.getBoolean(TestConfigurationType.DEBUG_MODE));
        assertEquals("Test Server", configuration.getString(TestConfigurationType.SERVER_NAME));
        assertEquals(100, configuration.getInt(TestConfigurationType.MAX_PLAYERS));
        assertEquals(30.5f, configuration.getFloat(TestConfigurationType.TICK_RATE));
        assertEquals("test string", configuration.getString(TestConfigurationType.STRING_VALUE));
        assertEquals(42, configuration.getInt(TestConfigurationType.INT_VALUE));
        assertEquals(3.14f, configuration.getFloat(TestConfigurationType.FLOAT_VALUE));
    }

    @Test
    void testLoadInvalidConfiguration() {
        File configFile = createTestConfig(Map.of(
            "SERVER_PORT", "-1",  // Invalid port
            "SERVER_NAME", "Test Server",
            "STRING_VALUE", "test string",
            "INT_VALUE", "-1",    // Invalid value (should be >= 0)
            "FLOAT_VALUE", "-3.14" // Invalid value (should be >= 0)
        ));

        ConfigurationException exception = assertThrows(ConfigurationException.class,
            () -> {
                configuration.load(configFile.getAbsolutePath());
                // Force validation to run
                configuration.validate();
            });
        assertEquals(ConfigurationException.ErrorType.VALIDATION_FAILED, exception.getErrorType());
    }

    @Test
    void testDefaultValues() throws Exception {
        File configFile = createTestConfig(Map.of(
            "SERVER_NAME", "Test Server",
            "STRING_VALUE", "test string",
            "INT_VALUE", "42",
            "FLOAT_VALUE", "3.14"
        ));

        configuration.load(configFile.getAbsolutePath());

        // Check default values for optional configuration
        assertEquals(8080, configuration.getInt(TestConfigurationType.SERVER_PORT));
        assertFalse(configuration.getBoolean(TestConfigurationType.DEBUG_MODE));
        assertEquals(100, configuration.getInt(TestConfigurationType.MAX_PLAYERS));
        assertEquals(60.0f, configuration.getFloat(TestConfigurationType.TICK_RATE));
    }

    @Test
    void testMissingRequiredValue() {
        File configFile = createTestConfig(Map.of(
            "DEBUG_MODE", "true"  // Only provide optional value
        ));

        ConfigurationException exception = assertThrows(ConfigurationException.class,
            () -> configuration.load(configFile.getAbsolutePath()));
        assertEquals(ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE, exception.getErrorType());
    }

    @Test
    void testInvalidValueType() {
        File configFile = createTestConfig(Map.of(
            "SERVER_PORT", "invalid",
            "SERVER_NAME", "Test Server"
        ));

        ConfigurationException exception = assertThrows(ConfigurationException.class,
            () -> configuration.load(configFile.getAbsolutePath()));
        assertEquals(ConfigurationException.ErrorType.PARSE_ERROR, exception.getErrorType());
    }

    @Test
    void testReload() throws Exception {
        File configFile = createTestConfig(Map.of(
            "SERVER_PORT", "8080",
            "SERVER_NAME", "Test Server",
            "STRING_VALUE", "test string",
            "INT_VALUE", "42",
            "FLOAT_VALUE", "3.14"
        ));

        configuration.load(configFile.getAbsolutePath());
        assertEquals(8080, configuration.getInt(TestConfigurationType.SERVER_PORT));

        // Update config file
        createTestConfig(configFile, Map.of(
            "SERVER_PORT", "9090",
            "SERVER_NAME", "Updated Server",
            "STRING_VALUE", "updated string",
            "INT_VALUE", "99",
            "FLOAT_VALUE", "6.28"
        ));

        configuration.reload();
        assertEquals(9090, configuration.getInt(TestConfigurationType.SERVER_PORT));
        assertEquals("Updated Server", configuration.getString(TestConfigurationType.SERVER_NAME));
        assertEquals("updated string", configuration.getString(TestConfigurationType.STRING_VALUE));
        assertEquals(99, configuration.getInt(TestConfigurationType.INT_VALUE));
        assertEquals(6.28f, configuration.getFloat(TestConfigurationType.FLOAT_VALUE));
    }

    @Test
    void testClear() throws Exception {
        File configFile = createTestConfig(Map.of(
            "SERVER_PORT", "8080",
            "SERVER_NAME", "Test Server",
            "STRING_VALUE", "test string",
            "INT_VALUE", "42",
            "FLOAT_VALUE", "3.14"
        ));

        configuration.load(configFile.getAbsolutePath());
        assertTrue(configuration.isDefined(TestConfigurationType.SERVER_PORT));

        configuration.clear();
        assertFalse(configuration.isDefined(TestConfigurationType.SERVER_PORT));
    }

    @Test
    void testExport() throws Exception {
        File configFile = createTestConfig(Map.of(
            "SERVER_PORT", "8080",
            "SERVER_NAME", "Test Server",
            "STRING_VALUE", "test string",
            "INT_VALUE", "42",
            "FLOAT_VALUE", "3.14"
        ));

        configuration.load(configFile.getAbsolutePath());
        Map<ConfigurationType, Object> exported = configuration.export();

        assertEquals(8080, exported.get(TestConfigurationType.SERVER_PORT));
        assertEquals("Test Server", exported.get(TestConfigurationType.SERVER_NAME));
        assertEquals("test string", exported.get(TestConfigurationType.STRING_VALUE));
        assertEquals(42, exported.get(TestConfigurationType.INT_VALUE));
        assertEquals(3.14f, exported.get(TestConfigurationType.FLOAT_VALUE));
    }

    @Test
    void testVersion() {
        assertEquals("1.0.0", configuration.getVersion());
    }

    private File createTestConfig(Map<String, String> properties) {
        try {
            File configFile = new File(tempDir, "test.properties");
            createTestConfig(configFile, properties);
            return configFile;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create test config", e);
        }
    }

    private void createTestConfig(File file, Map<String, String> properties) throws IOException {
        Properties props = new Properties();
        props.putAll(properties);
        try (FileWriter writer = new FileWriter(file)) {
            props.store(writer, "Test Configuration");
        }
    }
} 