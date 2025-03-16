package com.tenio.common.configuration.format;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tenio.common.configuration.ConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class YamlConfigurationHandlerTest {

    @TempDir
    File tempDir;

    private YamlConfigurationHandler handler;
    private File yamlFile;
    private Map<String, Object> testConfig;

    @BeforeEach
    void setUp() {
        handler = new YamlConfigurationHandler();
        yamlFile = new File(tempDir, "test.yml");
        testConfig = new HashMap<>();
        testConfig.put("string", "value");
        testConfig.put("number", 42);
        testConfig.put("boolean", true);
        testConfig.put("nested", new HashMap<String, Object>() {{
            put("key", "value");
        }});
    }

    @Test
    void testCanHandle() {
        assertTrue(handler.canHandle(new File("test.yml")));
        assertTrue(handler.canHandle(new File("test.yaml")));
        assertFalse(handler.canHandle(new File("test.properties")));
        assertFalse(handler.canHandle(new File("test.json")));
    }

    @Test
    void testSaveAndLoad() throws ConfigurationException {
        handler.save(yamlFile, testConfig);
        assertTrue(yamlFile.exists());

        Map<String, Object> loaded = handler.load(yamlFile);
        assertEquals(testConfig.size(), loaded.size());
        assertEquals("value", loaded.get("string"));
        assertEquals(42, loaded.get("number"));
        assertEquals(true, loaded.get("boolean"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> nested = (Map<String, Object>) loaded.get("nested");
        assertNotNull(nested);
        assertEquals("value", nested.get("key"));
    }

    @Test
    void testLoadNonexistentFile() {
        assertThrows(ConfigurationException.class, () -> handler.load(new File("nonexistent.yml")));
    }

    @Test
    void testLoadInvalidYaml() throws IOException {
        try (FileWriter writer = new FileWriter(yamlFile)) {
            writer.write("invalid: yaml: content: {unclosed");
        }

        assertThrows(ConfigurationException.class, () -> handler.load(yamlFile));
    }

    @Test
    void testSaveToReadOnlyDirectory() {
        tempDir.setReadOnly();
        File readOnlyFile = new File(tempDir, "readonly.yml");
        
        assertThrows(ConfigurationException.class, () -> handler.save(readOnlyFile, testConfig));
    }

    @Test
    void testLoadEmptyFile() throws IOException {
        yamlFile.createNewFile();
        
        assertThrows(ConfigurationException.class, () -> handler.load(yamlFile));
    }

    @Test
    void testSaveNull() {
        assertThrows(NullPointerException.class, () -> handler.save(null, testConfig));
        assertThrows(NullPointerException.class, () -> handler.save(yamlFile, null));
    }

    @Test
    void testLoadComplexStructure() throws ConfigurationException {
        Map<String, Object> complexConfig = new HashMap<>();
        complexConfig.put("array", new String[] {"one", "two", "three"});
        complexConfig.put("list", java.util.Arrays.asList(1, 2, 3));
        complexConfig.put("deep", new HashMap<String, Object>() {{
            put("nested", new HashMap<String, Object>() {{
                put("value", 42);
            }});
        }});

        handler.save(yamlFile, complexConfig);
        Map<String, Object> loaded = handler.load(yamlFile);

        assertNotNull(loaded.get("array"));
        assertNotNull(loaded.get("list"));
        
        @SuppressWarnings("unchecked")
        Map<String, Object> deep = (Map<String, Object>) loaded.get("deep");
        assertNotNull(deep);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> nested = (Map<String, Object>) deep.get("nested");
        assertNotNull(nested);
        assertEquals(42, nested.get("value"));
    }

    @Test
    void testSaveAndLoadWithSpecialCharacters() throws ConfigurationException {
        Map<String, Object> specialConfig = new HashMap<>();
        specialConfig.put("special", "!@#$%^&*()_+");
        specialConfig.put("unicode", "こんにちは");
        specialConfig.put("multiline", "line1\nline2\nline3");

        handler.save(yamlFile, specialConfig);
        Map<String, Object> loaded = handler.load(yamlFile);

        assertEquals(specialConfig.get("special"), loaded.get("special"));
        assertEquals(specialConfig.get("unicode"), loaded.get("unicode"));
        assertEquals(specialConfig.get("multiline"), loaded.get("multiline"));
    }
} 