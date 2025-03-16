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

import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Enhanced configuration interface that provides type-safe access to configuration values
 * with validation and reload capabilities.
 */
public interface Configuration {

  /**
   * Load the configuration from a file.
   *
   * @param file the configuration file path
   * @throws ConfigurationException if there are any issues loading or validating the configuration
   */
  void load(String file) throws ConfigurationException;

  /**
   * Reload the configuration from the last loaded file.
   *
   * @throws ConfigurationException if there are any issues reloading or validating the configuration
   */
  void reload() throws ConfigurationException;

  /**
   * Get a boolean configuration value.
   *
   * @param key the configuration key
   * @return the boolean value
   * @throws ConfigurationException if the value is not present and no default exists
   */
  boolean getBoolean(ConfigurationType key) throws ConfigurationException;

  /**
   * Get an integer configuration value.
   *
   * @param key the configuration key
   * @return the integer value
   * @throws ConfigurationException if the value is not present and no default exists
   */
  int getInt(ConfigurationType key) throws ConfigurationException;

  /**
   * Get a float configuration value.
   *
   * @param key the configuration key
   * @return the float value
   * @throws ConfigurationException if the value is not present and no default exists
   */
  float getFloat(ConfigurationType key) throws ConfigurationException;

  /**
   * Get a string configuration value.
   *
   * @param key the configuration key
   * @return the string value
   * @throws ConfigurationException if the value is not present and no default exists
   */
  String getString(ConfigurationType key) throws ConfigurationException;

  /**
   * Get a configuration value as an Optional.
   *
   * @param key the configuration key
   * @param type the expected type of the value
   * @return an Optional containing the value if present
   * @param <T> the type parameter
   */
  <T> Optional<T> get(ConfigurationType key, Class<T> type);

  /**
   * Check if a configuration value is defined.
   *
   * @param key the configuration key
   * @return true if the value is defined, false otherwise
   */
  boolean isDefined(ConfigurationType key);

  /**
   * Get all defined configuration keys.
   *
   * @return a Set of all defined configuration keys
   */
  Set<ConfigurationType> getDefinedKeys();

  /**
   * Get the current configuration version.
   *
   * @return the configuration version string
   */
  String getVersion();

  /**
   * Export the current configuration as a Map.
   *
   * @return a Map containing all configuration key-value pairs
   */
  Map<ConfigurationType, Object> export();

  /**
   * Validate the entire configuration.
   *
   * @throws ConfigurationException if any validation fails
   */
  void validate() throws ConfigurationException;

  /**
   * Clear all configuration values.
   */
  void clear();
}
