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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.util.Map;
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
        configuration.load("src/test/resources/test-config.xml");
        
        assertEquals("test string", configuration.getString(TestConfigurationType.STRING_VALUE));
        assertEquals(42, configuration.getInt(TestConfigurationType.INT_VALUE));
        assertEquals(3.14f, configuration.getFloat(TestConfigurationType.FLOAT_VALUE));
        assertTrue(configuration.getBoolean(TestConfigurationType.BOOLEAN_VALUE));
        assertEquals(8080, configuration.getInt(TestConfigurationType.SERVER_PORT));
        assertTrue(configuration.getBoolean(TestConfigurationType.DEBUG_MODE));
        assertEquals("Test Server", configuration.getString(TestConfigurationType.SERVER_NAME));
        assertEquals(100, configuration.getInt(TestConfigurationType.MAX_PLAYERS));
        assertEquals(60.0f, configuration.getFloat(TestConfigurationType.TICK_RATE));
    }

    @Test
    void testLoadInvalidConfiguration() {
        // Create a file that doesn't exist to trigger a load error
        File nonExistentFile = new File(tempDir, "non-existent-file.properties");
        
        ConfigurationException exception = assertThrows(ConfigurationException.class,
            () -> configuration.load(nonExistentFile.getAbsolutePath()));
        
        assertEquals(ConfigurationException.ErrorType.LOAD_ERROR, exception.getErrorType());
    }

    @Test
    void testDefaultValues() throws Exception {
        // Check default values for optional configuration
        assertEquals(8080, (int)TestConfigurationType.SERVER_PORT.getDefaultValue());
        assertEquals(false, (boolean)TestConfigurationType.DEBUG_MODE.getDefaultValue());
        assertEquals(100, (int)TestConfigurationType.MAX_PLAYERS.getDefaultValue());
        assertEquals(60.0f, (float)TestConfigurationType.TICK_RATE.getDefaultValue());
    }

    @Test
    void testMissingRequiredValue() throws Exception {
        // Create a test XML file with missing required values
        File configFile = new File(tempDir, "missing-required.xml");
        try (FileWriter writer = new FileWriter(configFile)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<configuration>\n");
            writer.write("  <DEBUG_MODE>true</DEBUG_MODE>\n");
            writer.write("</configuration>\n");
        }
        
        // Load the configuration and validate it
        TestConfiguration testConfig = new TestConfiguration();
        testConfig.load(configFile.getAbsolutePath());
        
        // Manually call validate to check for missing required values
        Exception exception = assertThrows(ConfigurationException.class, () -> testConfig.validate());
        assertTrue(exception.getMessage().contains("Missing required configuration value"));
    }

    @Test
    void testInvalidValueType() {
        // Create a test XML file with invalid value type
        File configFile = new File(tempDir, "invalid-type.xml");
        try {
            try (FileWriter writer = new FileWriter(configFile)) {
                writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                writer.write("<configuration>\n");
                writer.write("  <SERVER_PORT>invalid</SERVER_PORT>\n");
                writer.write("  <SERVER_NAME>Test Server</SERVER_NAME>\n");
                writer.write("</configuration>\n");
            }
            
            // Load the configuration
            TestConfiguration testConfig = new TestConfiguration();
            Exception exception = assertThrows(ConfigurationException.class, 
                () -> testConfig.load(configFile.getAbsolutePath()));
            assertTrue(exception.getMessage().contains("Failed to load configuration file"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test file", e);
        }
    }

    @Test
    void testReload() throws Exception {
        configuration.load("src/test/resources/test-config.xml");
        
        assertEquals(8080, configuration.getInt(TestConfigurationType.SERVER_PORT));
        
        // Clear the configuration
        configuration.clear();
        assertFalse(configuration.isDefined(TestConfigurationType.SERVER_PORT));
        
        // Reload
        configuration.reload();
        
        // Verify values are reloaded
        assertEquals(8080, configuration.getInt(TestConfigurationType.SERVER_PORT));
    }

    @Test
    void testClear() throws Exception {
        configuration.load("src/test/resources/test-config.xml");
        
        // Verify configuration is loaded
        assertTrue(configuration.isDefined(TestConfigurationType.SERVER_PORT));
        
        // Clear the configuration
        configuration.clear();
        
        // Verify configuration is cleared
        assertFalse(configuration.isDefined(TestConfigurationType.SERVER_PORT));
        assertTrue(configuration.export().isEmpty());
    }

    @Test
    void testExport() throws Exception {
        configuration.load("src/test/resources/test-config.xml");
        
        Map<ConfigurationType, Object> exported = configuration.export();
        
        // Verify exported values
        assertEquals(8080, exported.get(TestConfigurationType.SERVER_PORT));
        assertEquals("Test Server", exported.get(TestConfigurationType.SERVER_NAME));
        assertEquals(42, exported.get(TestConfigurationType.INT_VALUE));
        assertEquals(3.14f, exported.get(TestConfigurationType.FLOAT_VALUE));
        assertEquals(true, exported.get(TestConfigurationType.BOOLEAN_VALUE));
    }

    @Test
    void testVersion() {
        assertEquals("1.0.0", configuration.getVersion());
    }
}
