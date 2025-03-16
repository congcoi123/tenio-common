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
 * Enhanced configuration type interface that includes type information and validation.
 * This interface defines the contract for configuration keys and their associated metadata.
 */
public interface ConfigurationType {
    /**
     * Gets the expected type of the configuration value.
     *
     * @return The Class representing the expected type
     */
    Class<?> getValueType();

    /**
     * Gets the default value for this configuration type.
     *
     * @return The default value, may be null if no default is specified
     */
    Object getDefaultValue();

    /**
     * Gets the validator for this configuration type.
     *
     * @return A predicate that validates values for this configuration type
     */
    Predicate<Object> getValidator();

    /**
     * Gets the description of this configuration type.
     *
     * @return A human-readable description of what this configuration represents
     */
    String getDescription();

    /**
     * Checks if this configuration is required.
     *
     * @return true if this configuration must be provided, false otherwise
     */
    boolean isRequired();
}
