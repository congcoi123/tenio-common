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

/**
 * The Configuration interface defines the contract for accessing configuration data.
 * <p>
 * This interface provides methods to load configuration from files and retrieve
 * configuration values of different types. The configuration system is designed to
 * support type-safe access to configuration values using enum-based keys.
 * <p>
 * The server requires basic configuration to start running. The configuration file
 * is typically defined as an XML file. See an example in {@code configuration.example.xml}.
 * <p>
 * Implementations of this interface should be thread-safe to allow concurrent access
 * from multiple components of the application.
 * <p>
 * Example usage:
 * <pre>
 * Configuration config = new YourConfigurationImpl();
 * config.load("path/to/config.xml");
 * int port = config.getInt(ServerConfigType.PORT);
 * String name = config.getString(ServerConfigType.NAME);
 * </pre>
 * 
 * @see ConfigurationType
 * @see CommonConfiguration
 */
public interface Configuration {

  /**
   * Loads the configuration from the specified file.
   * <p>
   * This method reads the configuration data from the given file and makes it
   * available through the other methods of this interface. The file format
   * depends on the implementation, but is typically XML.
   * <p>
   * If the file cannot be read or parsed, an exception is thrown.
   *
   * @param file the path to the configuration file
   * @throws Exception if there is an error loading or parsing the configuration file
   */
  void load(String file) throws Exception;

  /**
   * Retrieves a boolean configuration value.
   * <p>
   * This method returns the boolean value associated with the specified key.
   * If the value is stored as a string, it will be converted to a boolean
   * using {@link Boolean#parseBoolean(String)}.
   *
   * @param key the configuration key
   * @return the boolean value associated with the key
   */
  boolean getBoolean(ConfigurationType key);

  /**
   * Retrieves an integer configuration value.
   * <p>
   * This method returns the integer value associated with the specified key.
   * If the value is stored as a string, it will be converted to an integer
   * using {@link Integer#parseInt(String)}.
   *
   * @param key the configuration key
   * @return the integer value associated with the key
   */
  int getInt(ConfigurationType key);

  /**
   * Retrieves a float configuration value.
   * <p>
   * This method returns the float value associated with the specified key.
   * If the value is stored as a string, it will be converted to a float
   * using {@link Float#parseFloat(String)}.
   *
   * @param key the configuration key
   * @return the float value associated with the key
   */
  float getFloat(ConfigurationType key);

  /**
   * Retrieves a string configuration value.
   * <p>
   * This method returns the string value associated with the specified key.
   * If the value is not a string, it will be converted to a string
   * using {@link Object#toString()}.
   *
   * @param key the configuration key
   * @return the string value associated with the key
   */
  String getString(ConfigurationType key);

  /**
   * Retrieves a configuration value as an object.
   * <p>
   * This method returns the raw object associated with the specified key
   * without any type conversion.
   *
   * @param key the configuration key
   * @return the object value associated with the key
   */
  Object get(ConfigurationType key);

  /**
   * Determines if a configuration value is defined for the specified key.
   * <p>
   * This method returns {@code true} if the configuration contains a value
   * for the specified key, and {@code false} otherwise.
   * <p>
   * If you want a configuration value to be treated as "undefined",
   * you can set its value to {@code -1}.
   *
   * @param key the configuration key
   * @return {@code true} if the configuration contains a value for the key,
   *         {@code false} otherwise
   */
  boolean isDefined(ConfigurationType key);

  /**
   * Returns a string representation of the configuration.
   * <p>
   * This method returns a human-readable string representation of the
   * configuration, which can be useful for debugging or logging.
   *
   * @return a string representation of the configuration
   */
  String toString();

  /**
   * Clears all configuration values.
   * <p>
   * This method removes all configuration values, resulting in an empty
   * configuration. After calling this method, {@link #isDefined(ConfigurationType)}
   * will return {@code false} for all keys.
   */
  void clear();
}
