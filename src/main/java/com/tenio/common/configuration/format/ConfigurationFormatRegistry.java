package com.tenio.common.configuration.format;

import com.tenio.common.configuration.ConfigurationException;
import com.tenio.common.configuration.ConfigurationException.ErrorType;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Registry for configuration format handlers.
 */
public class ConfigurationFormatRegistry {

    private final List<ConfigurationFormatHandler> handlers;

    /**
     * Creates a new configuration format registry with default handlers.
     */
    public ConfigurationFormatRegistry() {
        handlers = new ArrayList<>();
        // Register default handlers
        handlers.add(new PropertiesConfigurationHandler());
        handlers.add(new JsonConfigurationHandler());
        handlers.add(new YamlConfigurationHandler());
    }

    /**
     * Register a new configuration format handler.
     *
     * @param handler the handler to register
     */
    public void registerHandler(ConfigurationFormatHandler handler) {
        handlers.add(handler);
    }

    /**
     * Load configuration from a file using the appropriate handler.
     *
     * @param file the configuration file
     * @return a map of configuration key-value pairs
     * @throws ConfigurationException if no handler can handle the file or if there are loading issues
     */
    public Map<String, Object> load(File file) throws ConfigurationException {
        for (ConfigurationFormatHandler handler : handlers) {
            if (handler.canHandle(file)) {
                return handler.load(file);
            }
        }
        throw new ConfigurationException(
            "No handler found for configuration file: " + file.getName(),
            null, ErrorType.LOAD_ERROR);
    }

    /**
     * Save configuration to a file using the appropriate handler.
     *
     * @param file the configuration file
     * @param configuration the configuration to save
     * @throws ConfigurationException if no handler can handle the file or if there are saving issues
     */
    public void save(File file, Map<String, Object> configuration) throws ConfigurationException {
        for (ConfigurationFormatHandler handler : handlers) {
            if (handler.canHandle(file)) {
                handler.save(file, configuration);
                return;
            }
        }
        throw new ConfigurationException(
            "No handler found for configuration file: " + file.getName(),
            null, ErrorType.LOAD_ERROR);
    }
} 