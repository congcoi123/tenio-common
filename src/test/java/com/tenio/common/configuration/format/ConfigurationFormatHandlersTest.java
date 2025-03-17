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

/**
 * Test class for configuration format handlers and the registry.
 * <p>
 * This class tests the functionality of the {@link XmlConfigurationHandler} and
 * {@link ConfigurationFormatRegistry} classes. It verifies that:
 * <ul>
 *   <li>The XML handler can correctly save and load configuration data</li>
 *   <li>The registry can find the appropriate handler for a file</li>
 *   <li>The registry throws appropriate exceptions when no handler is found</li>
 * </ul>
 * <p>
 * The tests create temporary files for testing in a temporary directory.
 */
class ConfigurationFormatHandlersTest {

    @TempDir
    File tempDir;

    private XmlConfigurationHandler xmlHandler;
    private Map<String, Object> testConfig;

    /**
     * Sets up the test environment before each test.
     * <p>
     * This method initializes the XML handler and sets up a test configuration
     * map with sample values.
     */
    @BeforeEach
    void setUp() {
        xmlHandler = new XmlConfigurationHandler();
        testConfig = Map.of(
            "SERVER_PORT", 8080,
            "SERVER_NAME", "TenIO Server",
            "MAX_CONNECTIONS", 1000
        );
    }

    /**
     * Tests the XML configuration handler.
     * <p>
     * This test verifies that the XML handler can:
     * <ul>
     *   <li>Determine if it can handle XML files</li>
     *   <li>Save configuration data to an XML file</li>
     *   <li>Load configuration data from an XML file</li>
     * </ul>
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testXmlHandler() throws Exception {
        File file = new File(tempDir, "test.xml");
        xmlHandler.save(file, testConfig);
        assertTrue(file.exists());

        Map<String, Object> loaded = xmlHandler.load(file);
        assertConfigEquals(testConfig, loaded);
        assertTrue(xmlHandler.canHandle(new File("test.xml")));
        assertFalse(xmlHandler.canHandle(new File("test.json")));
    }

    /**
     * Tests the configuration format registry.
     * <p>
     * This test verifies that the registry can:
     * <ul>
     *   <li>Find the appropriate handler for an XML file</li>
     *   <li>Load configuration data using the appropriate handler</li>
     *   <li>Save configuration data using the appropriate handler</li>
     *   <li>Throw an exception when no handler is found for a file</li>
     * </ul>
     *
     * @throws Exception if there is an error during the test
     */
    @Test
    void testConfigurationFormatRegistry() throws Exception {
        ConfigurationFormatRegistry registry = new ConfigurationFormatRegistry();

        // Test XML format
        File xmlFile = new File(tempDir, "test.xml");
        registry.save(xmlFile, testConfig);
        Map<String, Object> xmlLoaded = registry.load(xmlFile);
        assertConfigEquals(testConfig, xmlLoaded);

        // Test unsupported format
        File unsupportedFile = new File(tempDir, "test.xyz");
        assertThrows(ConfigurationException.class, () -> registry.load(unsupportedFile));
        assertThrows(ConfigurationException.class, () -> registry.save(unsupportedFile, testConfig));
    }

    /**
     * Helper method to assert that two configuration maps are equal.
     * <p>
     * This method compares the values in the two maps, converting them to strings
     * for comparison since the loaded values may be strings.
     *
     * @param expected the expected configuration map
     * @param actual the actual configuration map
     */
    private void assertConfigEquals(Map<String, Object> expected, Map<String, Object> actual) {
        assertEquals(expected.size(), actual.size());
        for (String key : expected.keySet()) {
            assertEquals(String.valueOf(expected.get(key)), String.valueOf(actual.get(key)));
        }
    }
} 