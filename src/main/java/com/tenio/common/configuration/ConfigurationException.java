package com.tenio.common.configuration;

import java.io.Serial;

/**
 * Exception thrown when there are issues with configuration loading, validation, or access.
 */
public class ConfigurationException extends Exception {

    @Serial
    private static final long serialVersionUID = 1936137870101920490L;

    private final ConfigurationType configurationType;
    private final ErrorType errorType;

    /**
     * Error types for configuration exceptions.
     */
    public enum ErrorType {
        MISSING_REQUIRED_VALUE,
        INVALID_VALUE_TYPE,
        VALIDATION_FAILED,
        LOAD_ERROR,
        PARSE_ERROR
    }

    /**
     * Creates a new configuration exception.
     *
     * @param message the error message
     * @param configurationType the configuration type that caused the error
     * @param errorType the type of error that occurred
     */
    public ConfigurationException(String message, ConfigurationType configurationType, ErrorType errorType) {
        super(message);
        this.configurationType = configurationType;
        this.errorType = errorType;
    }

    /**
     * Creates a new configuration exception with a cause.
     *
     * @param message the error message
     * @param cause the cause of the error
     * @param configurationType the configuration type that caused the error
     * @param errorType the type of error that occurred
     */
    public ConfigurationException(String message, Throwable cause, ConfigurationType configurationType,
                                ErrorType errorType) {
        super(message, cause);
        this.configurationType = configurationType;
        this.errorType = errorType;
    }

    /**
     * Gets the configuration type that caused the error.
     *
     * @return the configuration type
     */
    public ConfigurationType getConfigurationType() {
        return configurationType;
    }

    /**
     * Gets the type of error that occurred.
     *
     * @return the error type
     */
    public ErrorType getErrorType() {
        return errorType;
    }
} 