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

import java.io.Serial;

/**
 * Exception thrown when there is an error in the configuration system.
 * <p>
 * This exception provides detailed information about configuration errors,
 * including the specific configuration type that caused the error, the
 * underlying cause, and the type of error that occurred.
 * <p>
 * The error type is specified using the {@link ErrorType} enum, which
 * categorizes different types of configuration errors.
 * <p>
 * Example usage:
 * <pre>
 * try {
 *     configuration.load("config.xml");
 * } catch (ConfigurationException e) {
 *     if (e.getErrorType() == ConfigurationException.ErrorType.LOAD_ERROR) {
 *         System.err.println("Failed to load configuration file: " + e.getMessage());
 *     } else if (e.getErrorType() == ConfigurationException.ErrorType.MISSING_REQUIRED_VALUE) {
 *         System.err.println("Missing required configuration value: " + e.getConfigurationType());
 *     }
 * }
 * </pre>
 */
public class ConfigurationException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private final ConfigurationType configurationType;
    private final ErrorType errorType;

    /**
     * Enum defining the types of configuration errors that can occur.
     * <p>
     * Each error type represents a specific category of configuration error,
     * which can be used to determine the appropriate response to the error.
     */
    public enum ErrorType {
        /**
         * Indicates an error occurred while loading a configuration file.
         * <p>
         * This error type is used when a configuration file cannot be found,
         * read, or parsed.
         */
        LOAD_ERROR,
        
        /**
         * Indicates an error occurred while parsing a configuration value.
         * <p>
         * This error type is used when a configuration value cannot be parsed
         * into the expected type, such as when a string cannot be parsed as
         * an integer.
         */
        PARSE_ERROR,
        
        /**
         * Indicates a required configuration value is missing.
         * <p>
         * This error type is used when a configuration value marked as required
         * is not present in the configuration.
         */
        MISSING_REQUIRED_VALUE,
        
        /**
         * Indicates a configuration value has an invalid type.
         * <p>
         * This error type is used when a configuration value has a type that
         * does not match the expected type for the configuration key.
         */
        INVALID_VALUE_TYPE,
        
        /**
         * Indicates a configuration value failed validation.
         * <p>
         * This error type is used when a configuration value does not satisfy
         * the validation rules for the configuration key.
         */
        VALIDATION_FAILED
    }

    /**
     * Constructs a new ConfigurationException with the specified detail message,
     * cause, configuration type, and error type.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     * @param configurationType the configuration type that caused the error, or null if not applicable
     * @param errorType the type of error that occurred
     */
    public ConfigurationException(String message, Throwable cause, ConfigurationType configurationType,
                                ErrorType errorType) {
        super(message, cause);
        this.configurationType = configurationType;
        this.errorType = errorType;
    }

    /**
     * Returns the configuration type that caused the error.
     *
     * @return the configuration type that caused the error, or null if not applicable
     */
    public ConfigurationType getConfigurationType() {
        return configurationType;
    }

    /**
     * Returns the type of error that occurred.
     *
     * @return the type of error that occurred
     */
    public ErrorType getErrorType() {
        return errorType;
    }
} 