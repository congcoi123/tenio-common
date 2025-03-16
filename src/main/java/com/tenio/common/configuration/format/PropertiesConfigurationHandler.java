package com.tenio.common.configuration.format;

import com.tenio.common.configuration.ConfigurationException;
import com.tenio.common.configuration.ConfigurationException.ErrorType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Handler for Properties format configuration files.
 */
public class PropertiesConfigurationHandler implements ConfigurationFormatHandler {

    @Override
    public Map<String, Object> load(File file) throws ConfigurationException {
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(fis);

            Map<String, Object> config = new HashMap<>();
            for (String key : properties.stringPropertyNames()) {
                config.put(key, properties.getProperty(key));
            }
            return config;
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load properties file: " + file,
                e, null, ErrorType.LOAD_ERROR);
        }
    }

    @Override
    public void save(File file, Map<String, Object> configuration) throws ConfigurationException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            Properties properties = new Properties();
            for (Map.Entry<String, Object> entry : configuration.entrySet()) {
                properties.setProperty(entry.getKey(), String.valueOf(entry.getValue()));
            }
            properties.store(fos, "TenIO Configuration");
        } catch (IOException e) {
            throw new ConfigurationException("Failed to save properties file: " + file,
                e, null, ErrorType.LOAD_ERROR);
        }
    }

    @Override
    public boolean canHandle(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".properties") || name.endsWith(".props");
    }
} 