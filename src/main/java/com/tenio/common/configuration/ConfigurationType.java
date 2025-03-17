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

import java.util.function.Predicate;

/**
 * The ConfigurationType interface defines the contract for configuration keys.
 * <p>
 * This interface is typically implemented by enums to provide a type-safe way
 * to access configuration values. Each configuration type represents a specific
 * configuration key and provides metadata about the expected value type,
 * validation rules, default values, and whether the value is required.
 * <p>
 * Example implementation:
 * <pre>
 * public enum ServerConfigType implements ConfigurationType {
 *     PORT(Integer.class, true, port -> (int)port > 0 && (int)port < 65536, "Server port number"),
 *     HOST(String.class, true, host -> host != null && !((String)host).isEmpty(), "Server hostname"),
 *     DEBUG(Boolean.class, false, value -> true, "Debug mode flag");
 *     
 *     private final Class<?> valueType;
 *     private final boolean required;
 *     private final Predicate<Object> validator;
 *     private final String description;
 *     
 *     ServerConfigType(Class<?> valueType, boolean required, Predicate<Object> validator, String description) {
 *         this.valueType = valueType;
 *         this.required = required;
 *         this.validator = validator;
 *         this.description = description;
 *     }
 *     
 *     // Implement interface methods...
 * }
 * </pre>
 * 
 * @see Configuration
 */
public interface ConfigurationType {

  /**
   * Returns the expected type of the configuration value.
   * <p>
   * This method returns the Java class that represents the expected type of the
   * configuration value. This is used for type checking and conversion when
   * retrieving values from the configuration.
   * <p>
   * Common return values include:
   * <ul>
   *   <li>{@link String}.class for text values</li>
   *   <li>{@link Integer}.class for integer values</li>
   *   <li>{@link Float}.class for floating-point values</li>
   *   <li>{@link Boolean}.class for boolean values</li>
   * </ul>
   *
   * @return the Java class representing the expected type of the configuration value
   */
  Class<?> getValueType();

  /**
   * Returns the default value for this configuration type.
   * <p>
   * This method returns the default value to use when the configuration does not
   * contain a value for this key. If there is no sensible default, this method
   * may return {@code null}.
   * <p>
   * The returned value should be of the type specified by {@link #getValueType()}.
   *
   * @return the default value, or {@code null} if there is no default
   */
  Object getDefaultValue();

  /**
   * Returns a predicate that validates configuration values.
   * <p>
   * This method returns a predicate that tests whether a configuration value
   * is valid for this configuration type. The predicate should return {@code true}
   * if the value is valid, and {@code false} otherwise.
   * <p>
   * The predicate is used to validate values when they are added to the configuration.
   *
   * @return a predicate that validates configuration values
   */
  Predicate<Object> getValidator();

  /**
   * Returns a description of this configuration type.
   * <p>
   * This method returns a human-readable description of this configuration type,
   * which can be used for documentation or error messages.
   *
   * @return a description of this configuration type
   */
  String getDescription();

  /**
   * Returns whether this configuration type is required.
   * <p>
   * This method returns {@code true} if a value for this configuration type
   * must be present in the configuration, and {@code false} if it is optional.
   * <p>
   * If a required configuration value is missing, validation will fail.
   *
   * @return {@code true} if this configuration type is required, {@code false} otherwise
   */
  boolean isRequired();
}
