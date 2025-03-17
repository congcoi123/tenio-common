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
import java.util.Map;

/**
 * Interface for handlers that load and save configuration data in specific formats.
 * <p>
 * This interface defines the contract for classes that can read and write
 * configuration data in specific file formats, such as XML, JSON, YAML, or
 * properties files. Each implementation handles a specific file format.
 * <p>
 * Implementations of this interface are used by the configuration system to
 * load configuration data from files and save configuration data to files.
 * The {@link ConfigurationFormatRegistry} maintains a registry of handlers
 * for different file formats.
 * <p>
 * Example implementation for a properties file handler:
 * <pre>
 * public class PropertiesConfigurationHandler implements ConfigurationFormatHandler {
 *     
 *     @Override
 *     public boolean canHandle(File file) {
 *         return file.getName().endsWith(".properties");
 *     }
 *     
 *     @Override
 *     public Map<String, Object> load(File file) throws ConfigurationException {
 *         Map<String, Object> config = new HashMap<>();
 *         try (FileInputStream fis = new FileInputStream(file)) {
 *             Properties props = new Properties();
 *             props.load(fis);
 *             for (String key : props.stringPropertyNames()) {
 *                 config.put(key, props.getProperty(key));
 *             }
 *             return config;
 *         } catch (IOException e) {
 *             throw new ConfigurationException("Failed to load properties file", e, null, 
 *                 ConfigurationException.ErrorType.LOAD_ERROR);
 *         }
 *     }
 *     
 *     @Override
 *     public void save(File file, Map<ConfigurationType, Object> config) throws ConfigurationException {
 *         // Implementation for saving to properties file
 *     }
 * }
 * </pre>
 * 
 * @see ConfigurationFormatRegistry
 */
public interface ConfigurationFormatHandler {

    /**
     * Determines if this handler can handle the specified file.
     * <p>
     * This method checks if the file has a format that this handler can process,
     * typically by examining the file extension or content.
     * <p>
     * Implementations should return {@code true} only if they are confident they
     * can correctly load and save the file format.
     *
     * @param file the file to check
     * @return {@code true} if this handler can handle the file, {@code false} otherwise
     */
    boolean canHandle(File file);

    /**
     * Loads configuration data from the specified file.
     * <p>
     * This method reads the file and parses its content into a map of configuration
     * values. The keys in the map are the configuration property names as strings,
     * and the values are the corresponding configuration values.
     * <p>
     * If the file cannot be read or parsed, a {@link ConfigurationException} is thrown.
     *
     * @param file the file to load configuration data from
     * @return a map of configuration values
     * @throws ConfigurationException if there is an error loading the file
     */
    Map<String, Object> load(File file) throws ConfigurationException;

    /**
     * Saves configuration data to the specified file.
     * <p>
     * This method writes the configuration data to the file in the format handled
     * by this handler. The configuration data is provided as a map where the keys
     * are configuration types and the values are the corresponding configuration
     * values.
     * <p>
     * If the file cannot be written, a {@link ConfigurationException} is thrown.
     *
     * @param file the file to save configuration data to
     * @param config the configuration data to save
     * @throws ConfigurationException if there is an error saving the file
     */
    void save(File file, Map<String, Object> config) throws ConfigurationException;
} 