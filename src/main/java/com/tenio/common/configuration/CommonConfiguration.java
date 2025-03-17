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

import com.tenio.common.logger.SystemLogger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * An abstract base implementation of the {@link Configuration} interface.
 * <p>
 * This class provides a common implementation of the Configuration interface
 * with thread-safe access to configuration values. It uses an immutable map
 * approach to ensure thread safety without the overhead of locks for read
 * operations.
 * <p>
 * The configuration data is stored in an immutable map after loading, which
 * ensures that read operations are lock-free and very fast. This is optimized
 * for the common case where configuration is loaded once at startup and then
 * read frequently during application execution.
 * <p>
 * Subclasses must implement the {@link #loadInternal(String)} method to define
 * how configuration data is loaded from a file. They may also override the
 * {@link #loadInternalToMap(String, Map)} method for better performance.
 * <p>
 * Example usage:
 * <pre>
 * public class MyConfiguration extends CommonConfiguration {
 *     
 *     protected void loadInternal(String file) throws ConfigurationException {
 *         // Load configuration from file
 *         // Call push() to add values to the configuration
 *     }
 *     
 *     protected ConfigurationType[] getConfigurationTypes() {
 *         return MyConfigurationType.values();
 *     }
 * }
 * </pre>
 * 
 * @see Configuration
 * @see ConfigurationType
 */
public abstract class CommonConfiguration extends SystemLogger implements Configuration {

  /**
   * All configuration values will be held in this map. You access values by your
   * defined keys.
   */
  private Map<ConfigurationType, Object> configuration;
  private String lastLoadedFile;
  private String version;

  /**
   * Creates a new instance of CommonConfiguration.
   * <p>
   * Initializes an empty configuration map and sets the default version to "1.0.0".
   */
  public CommonConfiguration() {
    configuration = new HashMap<>();
    version = "1.0.0";
  }

  @Override
  public void load(String file) throws Exception {
    // Create a new map for thread safety
    Map<ConfigurationType, Object> newConfig = new HashMap<>();
    lastLoadedFile = file;
    
    // Load configuration into the new map
    loadInternalToMap(file, newConfig);
    
    // Replace the configuration map atomically
    configuration = Collections.unmodifiableMap(newConfig);
  }

  @Override
  public boolean getBoolean(ConfigurationType key) {
    Object value = configuration.get(key);
    if (value instanceof String) {
      return Boolean.parseBoolean((String) value);
    }
    return (Boolean) value;
  }

  @Override
  public int getInt(ConfigurationType key) {
    Object value = configuration.get(key);
    if (value instanceof String) {
      return Integer.parseInt((String) value);
    }
    return (Integer) value;
  }

  @Override
  public float getFloat(ConfigurationType key) {
    Object value = configuration.get(key);
    if (value instanceof String) {
      return Float.parseFloat((String) value);
    }
    return (Float) value;
  }

  @Override
  public String getString(ConfigurationType key) {
    Object value = configuration.get(key);
    return value != null ? value.toString() : null;
  }

  @Override
  public Object get(ConfigurationType key) {
    return configuration.get(key);
  }

  @Override
  public boolean isDefined(ConfigurationType key) {
    return configuration.containsKey(key);
  }

  @Override
  public void clear() {
    configuration = Collections.emptyMap();
  }

  /**
   * Reloads the configuration from the last loaded file.
   * <p>
   * This method reloads the configuration from the file that was last loaded
   * using the {@link #load(String)} method. If no file has been loaded yet,
   * an exception is thrown.
   *
   * @throws Exception if there is an error reloading the configuration or if
   *         no file has been loaded yet
   */
  public void reload() throws Exception {
    if (lastLoadedFile == null) {
      throw new Exception("No configuration file has been loaded yet");
    }
    load(lastLoadedFile);
  }

  /**
   * Returns the version of the configuration.
   * <p>
   * This method returns the version string of the configuration, which can be
   * used to track configuration changes or compatibility.
   *
   * @return the version string of the configuration
   */
  public String getVersion() {
    return version;
  }

  /**
   * Exports the configuration as a map.
   * <p>
   * This method returns a copy of the internal configuration map, which can be
   * used to access all configuration values at once or to serialize the
   * configuration.
   *
   * @return a map containing all configuration key-value pairs
   */
  public Map<ConfigurationType, Object> export() {
    return new HashMap<>(configuration);
  }

  /**
   * Validates the configuration.
   * <p>
   * This method checks that all required configuration values are present and
   * valid. If any required value is missing, a ConfigurationException is thrown.
   * <p>
   * Subclasses may override this method to add additional validation logic.
   *
   * @throws ConfigurationException if validation fails
   */
  public void validate() throws ConfigurationException {
    // Check for required values
    for (ConfigurationType type : getConfigurationTypes()) {
      if (type.isRequired() && !isDefined(type)) {
        throw new ConfigurationException(
            String.format("Missing required configuration value: %s", type),
            null, type, ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE);
      }
    }
  }

  /**
   * Returns the set of defined configuration keys.
   * <p>
   * This method returns an unmodifiable set of all configuration keys that have
   * values defined in the configuration.
   *
   * @return an unmodifiable set of defined configuration keys
   */
  public Set<ConfigurationType> getDefinedKeys() {
    return Collections.unmodifiableSet(configuration.keySet());
  }

  /**
   * Internal method to load configuration from a file.
   * <p>
   * This method is called by {@link #load(String)} to load configuration data
   * from the specified file. Subclasses must implement this method to define
   * how configuration data is loaded.
   * <p>
   * Implementations should use the {@link #push(ConfigurationType, Object)}
   * method to add values to the configuration.
   *
   * @param file the configuration file path
   * @throws ConfigurationException if there are any issues loading the configuration
   */
  protected abstract void loadInternal(String file) throws ConfigurationException;

  /**
   * Internal method to load configuration from a file into a specified map.
   * <p>
   * This method is called by {@link #load(String)} to load configuration data
   * from the specified file into the specified map. Subclasses may override
   * this method to provide a more efficient implementation than the default,
   * which delegates to {@link #loadInternal(String)}.
   * <p>
   * The default implementation delegates to {@link #loadInternal(String)} and
   * then copies the values from the internal configuration map to the specified
   * map. This is less efficient than directly populating the specified map.
   *
   * @param file the configuration file path
   * @param configMap the map to load configuration into
   * @throws ConfigurationException if there are any issues loading the configuration
   */
  protected void loadInternalToMap(String file, Map<ConfigurationType, Object> configMap) throws ConfigurationException {
    // Default implementation delegates to loadInternal
    // Subclasses should override this method for better performance
    Map<ConfigurationType, Object> tempMap = new HashMap<>(configuration);
    loadInternal(file);
    
    // Copy values from configuration to configMap
    configMap.putAll(configuration);
    
    // Restore original configuration
    configuration = tempMap;
  }

  /**
   * Pushes a new configuration value with validation.
   * <p>
   * This method adds a new value to the configuration for the specified key.
   * The value is validated against the key's validator, and if validation fails,
   * a ConfigurationException is thrown.
   * <p>
   * If the key already has a value in the configuration, the existing value is
   * not replaced, and a log message is generated if logging is enabled.
   * <p>
   * Since the configuration map is immutable, this method creates a new map
   * with the added value and replaces the internal map.
   *
   * @param key the configuration key
   * @param value the configuration value
   * @throws ConfigurationException if the value is invalid
   */
  protected void push(ConfigurationType key, Object value) throws ConfigurationException {
    if (key == null) {
      return;
    }

    if (configuration.containsKey(key)) {
      if (isInfoEnabled()) {
        info("CONFIGURATION",
            buildgen("Configuration key [", key, "] attempted to replace the old value ",
                configuration.get(key), " by the new one ", value));
      }
      return;
    }

    if (value != null && !key.getValueType().isInstance(value)) {
      throw new ConfigurationException(
          String.format("Invalid value type. Expected %s but got %s",
              key.getValueType().getSimpleName(),
              value.getClass().getSimpleName()),
          null, key, ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
    }

    if (value != null && !key.getValidator().test(value)) {
      throw new ConfigurationException("Validation failed for value: " + value,
          null, key, ConfigurationException.ErrorType.VALIDATION_FAILED);
    }

    // Since we're using an immutable map, we need to create a new map
    Map<ConfigurationType, Object> newConfig = new HashMap<>(configuration);
    newConfig.put(key, value);
    configuration = Collections.unmodifiableMap(newConfig);
  }

  /**
   * Returns the array of configuration types supported by this configuration.
   * <p>
   * This method returns an array of all configuration types that are supported
   * by this configuration. This is used by the {@link #validate()} method to
   * check that all required configuration values are present.
   * <p>
   * Subclasses must implement this method to define the set of configuration
   * types they support.
   *
   * @return an array of supported configuration types
   */
  protected abstract ConfigurationType[] getConfigurationTypes();

  /**
   * Returns the configuration type for the specified name.
   * <p>
   * This method returns the configuration type with the specified name, or
   * {@code null} if no such type exists.
   * <p>
   * Subclasses must implement this method to define how configuration types
   * are mapped to names.
   *
   * @param name the name of the configuration type
   * @return the configuration type with the specified name, or {@code null}
   */
  protected abstract ConfigurationType getConfigurationType(String name);

  /**
   * Extends the configuration with additional properties.
   * <p>
   * This method is called by some implementations to add additional properties
   * to the configuration that are not defined as configuration types.
   * <p>
   * Subclasses must implement this method to define how additional properties
   * are handled.
   *
   * @param extProperties a map of additional properties to add to the configuration
   */
  protected abstract void extend(Map<String, String> extProperties);

  /**
   * Load configuration from a map of values.
   *
   * @param values the configuration values to load
   * @throws ConfigurationException if there are any validation issues
   */
  protected void loadConfiguration(Map<String, Object> values) throws ConfigurationException {
    // Resolve environment variables in configuration values
    values = EnvironmentVariableResolver.resolveAll(values);

    // Process each configuration entry
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();
      
      ConfigurationType type = getConfigurationType(key);
      if (type == null) {
        throw new ConfigurationException("Unknown configuration key: " + key, null, 
            null, ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
      }

      try {
        Object convertedValue = convertValue(value, type.getValueType());
        configuration.put(type, convertedValue);
      } catch (Exception e) {
        throw new ConfigurationException("Error processing configuration key '" + key + "'", e, null,
            ConfigurationException.ErrorType.PARSE_ERROR);
      }
    }

    extend(values.entrySet().stream()
        .filter(e -> getConfigurationType(e.getKey()) == null)
        .collect(java.util.stream.Collectors.toMap(
            Map.Entry::getKey,
            e -> String.valueOf(e.getValue())
        )));
  }

  private Object convertValue(Object value, Class<?> targetType) throws ConfigurationException {
    try {
      if (targetType == Boolean.class) {
        if (value instanceof String) {
          return Boolean.parseBoolean((String) value);
        }
      } else if (targetType == Integer.class) {
        if (value instanceof String) {
          return Integer.parseInt((String) value);
        }
      } else if (targetType == Float.class) {
        if (value instanceof String) {
          return Float.parseFloat((String) value);
        }
      }
      return value;
    } catch (NumberFormatException e) {
      throw new ConfigurationException("Failed to convert value: " + value,
          e, null, ConfigurationException.ErrorType.PARSE_ERROR);
    }
  }
}
