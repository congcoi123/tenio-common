package com.tenio.common.configuration.format;

import com.tenio.common.configuration.ConfigurationException;
import com.tenio.common.configuration.ConfigurationException.ErrorType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * Handler for YAML format configuration files.
 */
public class YamlConfigurationHandler implements ConfigurationFormatHandler {

    private final Yaml yaml;

    public YamlConfigurationHandler() {
        yaml = new Yaml();
    }

    @Override
    public boolean canHandle(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

    @Override
    public Map<String, Object> load(File file) throws ConfigurationException {
        if (file == null) {
            throw new ConfigurationException("File cannot be null",
                null, null, ErrorType.LOAD_ERROR);
        }
        try {
            if (!file.exists()) {
                throw new ConfigurationException("Configuration file not found: " + file.getName(),
                    null, null, ErrorType.LOAD_ERROR);
            }
            if (file.length() == 0) {
                throw new ConfigurationException("Configuration file is empty: " + file.getName(),
                    null, null, ErrorType.LOAD_ERROR);
            }
            Map<String, Object> result = yaml.load(new FileInputStream(file));
            if (result == null) {
                throw new ConfigurationException("Invalid YAML configuration: " + file.getName(),
                    null, null, ErrorType.LOAD_ERROR);
            }
            return result;
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load configuration: " + file.getName(),
                e, null, ErrorType.LOAD_ERROR);
        } catch (YAMLException e) {
            throw new ConfigurationException("Invalid YAML configuration: " + file.getName(),
                e, null, ErrorType.LOAD_ERROR);
        }
    }

    @Override
    public void save(File file, Map<String, Object> configuration) throws ConfigurationException {
        if (file == null || configuration == null) {
            throw new NullPointerException("File and configuration cannot be null");
        }
        try {
            yaml.dump(configuration, new FileWriter(file));
        } catch (IOException e) {
            throw new ConfigurationException("Failed to save configuration: " + file.getName(),
                e, null, ErrorType.LOAD_ERROR);
        }
    }
} 