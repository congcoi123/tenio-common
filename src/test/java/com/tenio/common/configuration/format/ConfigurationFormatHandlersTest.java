package com.tenio.common.configuration.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tenio.common.configuration.ConfigurationException;
import java.io.File;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfigurationFormatHandlersTest {

    @TempDir
    File tempDir;

    private PropertiesConfigurationHandler propertiesHandler;
    private JsonConfigurationHandler jsonHandler;
    private YamlConfigurationHandler yamlHandler;
    private Map<String, Object> testConfig;

    @BeforeEach
    void setUp() {
        propertiesHandler = new PropertiesConfigurationHandler();
        jsonHandler = new JsonConfigurationHandler();
        yamlHandler = new YamlConfigurationHandler();
        testConfig = Map.of(
            "server.port", 8080,
            "server.name", "Test Server",
            "debug.enabled", true,
            "game.maxPlayers", 1000,
            "game.tickRate", 60.5f
        );
    }

    @Test
    void testPropertiesHandler() throws Exception {
        File file = new File(tempDir, "test.properties");
        propertiesHandler.save(file, testConfig);
        assertTrue(file.exists());

        Map<String, Object> loaded = propertiesHandler.load(file);
        assertConfigEquals(testConfig, loaded);
        assertTrue(propertiesHandler.canHandle(new File("test.properties")));
        assertTrue(propertiesHandler.canHandle(new File("test.props")));
        assertFalse(propertiesHandler.canHandle(new File("test.json")));
    }

    @Test
    void testJsonHandler() throws Exception {
        File file = new File(tempDir, "test.json");
        jsonHandler.save(file, testConfig);
        assertTrue(file.exists());

        Map<String, Object> loaded = jsonHandler.load(file);
        assertConfigEquals(testConfig, loaded);
        assertTrue(jsonHandler.canHandle(new File("test.json")));
        assertFalse(jsonHandler.canHandle(new File("test.yaml")));
    }

    @Test
    void testYamlHandler() throws Exception {
        File file = new File(tempDir, "test.yml");
        yamlHandler.save(file, testConfig);
        assertTrue(file.exists());

        Map<String, Object> loaded = yamlHandler.load(file);
        assertConfigEquals(testConfig, loaded);
        assertTrue(yamlHandler.canHandle(new File("test.yml")));
        assertTrue(yamlHandler.canHandle(new File("test.yaml")));
        assertFalse(yamlHandler.canHandle(new File("test.json")));
    }

    @Test
    void testConfigurationFormatRegistry() throws Exception {
        ConfigurationFormatRegistry registry = new ConfigurationFormatRegistry();

        // Test properties format
        File propsFile = new File(tempDir, "test.properties");
        registry.save(propsFile, testConfig);
        Map<String, Object> propsLoaded = registry.load(propsFile);
        assertConfigEquals(testConfig, propsLoaded);

        // Test JSON format
        File jsonFile = new File(tempDir, "test.json");
        registry.save(jsonFile, testConfig);
        Map<String, Object> jsonLoaded = registry.load(jsonFile);
        assertConfigEquals(testConfig, jsonLoaded);

        // Test YAML format
        File yamlFile = new File(tempDir, "test.yml");
        registry.save(yamlFile, testConfig);
        Map<String, Object> yamlLoaded = registry.load(yamlFile);
        assertConfigEquals(testConfig, yamlLoaded);

        // Test unsupported format
        File unsupportedFile = new File(tempDir, "test.xyz");
        assertThrows(ConfigurationException.class, () -> registry.load(unsupportedFile));
    }

    private void assertConfigEquals(Map<String, Object> expected, Map<String, Object> actual) {
        assertEquals(expected.size(), actual.size());
        for (Map.Entry<String, Object> entry : expected.entrySet()) {
            assertTrue(actual.containsKey(entry.getKey()));
            Object expectedValue = entry.getValue();
            Object actualValue = actual.get(entry.getKey());

            if (expectedValue instanceof Number && actualValue instanceof Number) {
                // Compare numeric values by converting to double
                double expectedDouble = ((Number) expectedValue).doubleValue();
                double actualDouble = ((Number) actualValue).doubleValue();
                assertEquals(expectedDouble, actualDouble, 0.001, "Value mismatch for key: " + entry.getKey());
            } else {
                assertEquals(expectedValue.toString(), actualValue.toString(), "Value mismatch for key: " + entry.getKey());
            }
        }
    }
} 