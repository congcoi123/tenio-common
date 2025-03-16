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

import com.tenio.common.configuration.format.ConfigurationFormatRegistry;
import com.tenio.common.logger.SystemLogger;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This server needs some basic configuration to start running. The configuration file can be
 * defined as an XML file. See an example in <code>configuration.example.xml</code>. You can also
 * extend this file to create your own configuration settings.
 */
public abstract class CommonConfiguration extends SystemLogger implements Configuration {

  /**
   * All configuration values will be held in this map. You access values by your
   * defined keys.
   */
  private final Map<ConfigurationType, Object> configuration;
  private final ReadWriteLock lock;
  private final ConfigurationFormatRegistry formatRegistry;
  private String lastLoadedFile;
  private String version;

  /**
   * Creates a new instance.
   */
  public CommonConfiguration() {
    configuration = new HashMap<>();
    lock = new ReentrantReadWriteLock();
    formatRegistry = new ConfigurationFormatRegistry();
    version = "1.0.0";
  }

  @Override
  public void load(String file) throws ConfigurationException {
    lock.writeLock().lock();
    try {
      clear();
      lastLoadedFile = file;
      Map<String, Object> loadedConfig = formatRegistry.load(new File(file));
      loadConfiguration(loadedConfig);
      validate();
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void reload() throws ConfigurationException {
    if (lastLoadedFile == null) {
      throw new ConfigurationException("No configuration file has been loaded yet", null,
          ConfigurationException.ErrorType.LOAD_ERROR);
    }
    load(lastLoadedFile);
  }

  @Override
  public boolean getBoolean(ConfigurationType key) throws ConfigurationException {
    lock.readLock().lock();
    try {
      Object value = getAndValidate(key, Boolean.class);
      if (value instanceof String) {
        return Boolean.parseBoolean((String) value);
      }
      return (Boolean) value;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public int getInt(ConfigurationType key) throws ConfigurationException {
    lock.readLock().lock();
    try {
      Object value = getAndValidate(key, Integer.class);
      if (value instanceof String) {
        return Integer.parseInt((String) value);
      }
      return (Integer) value;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public float getFloat(ConfigurationType key) throws ConfigurationException {
    lock.readLock().lock();
    try {
      Object value = getAndValidate(key, Float.class);
      if (value instanceof String) {
        return Float.parseFloat((String) value);
      }
      return (Float) value;
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public String getString(ConfigurationType key) throws ConfigurationException {
    lock.readLock().lock();
    try {
      Object value = getAndValidate(key, String.class);
      return value.toString();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public <T> Optional<T> get(ConfigurationType key, Class<T> type) {
    lock.readLock().lock();
    try {
      Object value = configuration.get(key);
      if (value == null) {
        return Optional.empty();
      }
      if (type.isInstance(value)) {
        return Optional.of(type.cast(value));
      }
      return Optional.empty();
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public boolean isDefined(ConfigurationType key) {
    lock.readLock().lock();
    try {
      return configuration.containsKey(key);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public Set<ConfigurationType> getDefinedKeys() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableSet(configuration.keySet());
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public Map<ConfigurationType, Object> export() {
    lock.readLock().lock();
    try {
      return new HashMap<>(configuration);
    } finally {
      lock.readLock().unlock();
    }
  }

  @Override
  public void validate() throws ConfigurationException {
    // Check for required values
    for (ConfigurationType type : getConfigurationTypes()) {
      if (type.isRequired() && !isDefined(type)) {
        throw new ConfigurationException(
            String.format("Missing required configuration value: %s", type),
            type, ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE);
      }
    }
  }

  @Override
  public void clear() {
    lock.writeLock().lock();
    try {
      configuration.clear();
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Internal method to load configuration from a file.
   *
   * @param file the configuration file path
   * @throws ConfigurationException if there are any issues loading the configuration
   */
  protected abstract void loadInternal(String file) throws ConfigurationException;

  /**
   * Push a new configuration value with validation.
   *
   * @param key the configuration key
   * @param value the configuration value
   * @throws ConfigurationException if the value is invalid
   */
  protected void push(ConfigurationType key, Object value) throws ConfigurationException {
    if (key == null) {
      return;
    }

    lock.writeLock().lock();
    try {
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
            key, ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
      }

      if (value != null && !key.getValidator().test(value)) {
        throw new ConfigurationException("Validation failed for value: " + value,
            key, ConfigurationException.ErrorType.VALIDATION_FAILED);
      }

      configuration.put(key, value);
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Set the configuration version.
   *
   * @param version the version string
   */
  protected void setVersion(String version) {
    this.version = version;
  }

  private Object getAndValidate(ConfigurationType key, Class<?> expectedType)
      throws ConfigurationException {
    Object value = configuration.get(key);
    
    if (value == null) {
      if (key.isRequired()) {
        throw new ConfigurationException("Missing required value", key,
            ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE);
      }
      value = key.getDefaultValue();
    }

    if (value != null && !expectedType.isInstance(value) &&
        !(value instanceof String && canConvertFromString(value.toString(), expectedType))) {
      throw new ConfigurationException(
          String.format("Invalid value type. Expected %s but got %s",
              expectedType.getSimpleName(),
              value.getClass().getSimpleName()),
          key, ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
    }

    return value;
  }

  private boolean canConvertFromString(String value, Class<?> targetType) {
    try {
      if (targetType == Boolean.class) {
        Boolean.parseBoolean(value);
      } else if (targetType == Integer.class) {
        Integer.parseInt(value);
      } else if (targetType == Float.class) {
        Float.parseFloat(value);
      } else {
        return false;
      }
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /**
   * Your extension part can be handled here. Check the examples for more details
   * about how to use it.
   *
   * @param extProperties the extension data in key-value format
   * @see Map
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
            ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
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

  /**
   * Convert a configuration type name to its enum value.
   *
   * @param name the name of the configuration type
   * @return the configuration type enum value, or null if not found
   */
  protected abstract ConfigurationType getConfigurationType(String name);

  private Object convertValue(Object value, Class<?> targetType) throws ConfigurationException {
    if (value == null) {
      return null;
    }

    try {
      if (targetType.isInstance(value)) {
        return value;
      }

      String stringValue = String.valueOf(value);

      if (targetType == Boolean.class) {
        return Boolean.parseBoolean(stringValue);
      } else if (targetType == Integer.class) {
        return Integer.parseInt(stringValue);
      } else if (targetType == Float.class) {
        return Float.parseFloat(stringValue);
      } else if (targetType == String.class) {
        return stringValue;
      } else {
        throw new ConfigurationException("Unsupported type: " + targetType.getSimpleName(),
            null, ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
      }
    } catch (NumberFormatException e) {
      throw new ConfigurationException("Failed to convert value: " + value,
          e, null, ConfigurationException.ErrorType.PARSE_ERROR);
    }
  }

  protected abstract ConfigurationType[] getConfigurationTypes();
}
