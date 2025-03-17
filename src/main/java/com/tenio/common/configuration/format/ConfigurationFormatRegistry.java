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

import com.tenio.common.configuration.ConfigurationException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Registry for configuration format handlers.
 * <p>
 * This class maintains a registry of {@link ConfigurationFormatHandler} instances
 * that can load and save configuration data in different file formats. When a
 * configuration file needs to be loaded or saved, this registry is used to find
 * a handler that can handle the file's format.
 * <p>
 * By default, the registry includes a handler for XML files. Additional handlers
 * can be registered using the {@link #registerHandler(ConfigurationFormatHandler)}
 * method.
 * <p>
 * Example usage:
 * <pre>
 * // Create a registry with the default handlers
 * ConfigurationFormatRegistry registry = new ConfigurationFormatRegistry();
 * 
 * // Register a custom handler
 * registry.registerHandler(new CustomConfigurationHandler());
 * 
 * // Load a configuration file
 * Map<String, Object> config = registry.load(new File("config.xml"));
 * 
 * // Save a configuration file
 * registry.save(new File("config.xml"), config);
 * </pre>
 * 
 * @see ConfigurationFormatHandler
 */
public class ConfigurationFormatRegistry {

    private final List<ConfigurationFormatHandler> handlers;

    /**
     * Creates a new configuration format registry with the default handlers.
     * <p>
     * This constructor initializes the registry with a handler for XML files.
     * Additional handlers can be registered using the
     * {@link #registerHandler(ConfigurationFormatHandler)} method.
     */
    public ConfigurationFormatRegistry() {
        handlers = new ArrayList<>();
        // Register default handlers
        registerHandler(new XmlConfigurationHandler());
    }

    /**
     * Registers a new configuration format handler.
     * <p>
     * This method adds a new handler to the registry. The handler will be used
     * to load and save configuration files in the format it supports.
     * <p>
     * Handlers are tried in the order they were registered, so handlers registered
     * earlier have precedence over handlers registered later.
     *
     * @param handler the handler to register
     */
    public void registerHandler(ConfigurationFormatHandler handler) {
        handlers.add(handler);
    }

    /**
     * Loads configuration data from the specified file.
     * <p>
     * This method finds a handler that can handle the file's format and uses it
     * to load the configuration data. If no handler can handle the file's format,
     * a {@link ConfigurationException} is thrown.
     * <p>
     * The configuration data is returned as a map where the keys are the
     * configuration property names as strings, and the values are the
     * corresponding configuration values.
     *
     * @param file the file to load configuration data from
     * @return a map of configuration values
     * @throws ConfigurationException if no handler can handle the file's format
     *         or if there is an error loading the file
     */
    public Map<String, Object> load(File file) throws ConfigurationException {
        for (ConfigurationFormatHandler handler : handlers) {
            if (handler.canHandle(file)) {
                return handler.load(file);
            }
        }
        throw new ConfigurationException("No handler found for file: " + file.getName(),
            null, null, ConfigurationException.ErrorType.LOAD_ERROR);
    }

    /**
     * Saves configuration data to the specified file.
     * <p>
     * This method finds a handler that can handle the file's format and uses it
     * to save the configuration data. If no handler can handle the file's format,
     * a {@link ConfigurationException} is thrown.
     * <p>
     * The configuration data is provided as a map where the keys are the
     * configuration property names as strings, and the values are the
     * corresponding configuration values.
     *
     * @param file the file to save configuration data to
     * @param config the configuration data to save
     * @throws ConfigurationException if no handler can handle the file's format
     *         or if there is an error saving the file
     */
    public void save(File file, Map<String, Object> config) throws ConfigurationException {
        for (ConfigurationFormatHandler handler : handlers) {
            if (handler.canHandle(file)) {
                handler.save(file, config);
                return;
            }
        }
        throw new ConfigurationException("No handler found for file: " + file.getName(),
            null, null, ConfigurationException.ErrorType.LOAD_ERROR);
    }
} 