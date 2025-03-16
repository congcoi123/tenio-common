package com.tenio.common.configuration.format;

import com.tenio.common.configuration.ConfigurationException;
import java.io.File;
import java.util.Map;

/**
 * Interface for handling different configuration file formats.
 */
public interface ConfigurationFormatHandler {

    /**
     * Load configuration from a file.
     *
     * @param file the configuration file
     * @return a map of configuration key-value pairs
     * @throws ConfigurationException if there are any issues loading the configuration
     */
    Map<String, Object> load(File file) throws ConfigurationException;

    /**
     * Save configuration to a file.
     *
     * @param file the configuration file
     * @param configuration the configuration to save
     * @throws ConfigurationException if there are any issues saving the configuration
     */
    void save(File file, Map<String, Object> configuration) throws ConfigurationException;

    /**
     * Check if this handler can handle the given file format.
     *
     * @param file the configuration file
     * @return true if this handler can handle the file format, false otherwise
     */
    boolean canHandle(File file);
} 