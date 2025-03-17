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
 * Test implementation of ConfigurationType for unit testing.
 */
public enum TestConfigurationType implements ConfigurationType {
    STRING_VALUE(String.class, true, s -> s != null && !s.toString().trim().isEmpty(), "Test string value"),
    INT_VALUE(Integer.class, true, i -> i != null && (int) i >= 0, "Test integer value"),
    FLOAT_VALUE(Float.class, true, f -> f != null && (float) f >= 0, "Test float value"),
    BOOLEAN_VALUE(Boolean.class, false, b -> true, "Test boolean value"),
    UNDEFINED_VALUE(String.class, false, s -> true, "Test undefined value"),
    
    // Add server configuration types
    SERVER_PORT(Integer.class, false, i -> i != null && (int) i > 0, "Server port number"),
    DEBUG_MODE(Boolean.class, false, b -> true, "Debug mode flag"),
    SERVER_NAME(String.class, true, s -> s != null && !s.toString().trim().isEmpty(), "Server name"),
    MAX_PLAYERS(Integer.class, false, i -> i != null && (int) i > 0, "Maximum number of players"),
    TICK_RATE(Float.class, false, f -> f != null && (float) f > 0, "Server tick rate");

    private final Class<?> valueType;
    private final boolean required;
    private final Predicate<Object> validator;
    private final String description;

    TestConfigurationType(Class<?> valueType, boolean required, Predicate<Object> validator, String description) {
        this.valueType = valueType;
        this.required = required;
        this.validator = validator;
        this.description = description;
    }

    @Override
    public Class<?> getValueType() {
        return valueType;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public Predicate<Object> getValidator() {
        return validator;
    }

    @Override
    public Object getDefaultValue() {
        switch (this) {
            case BOOLEAN_VALUE:
                return false;
            case INT_VALUE:
                return 0;
            case FLOAT_VALUE:
                return 0.0f;
            case SERVER_PORT:
                return 8080;
            case DEBUG_MODE:
                return false;
            case MAX_PLAYERS:
                return 100;
            case TICK_RATE:
                return 60.0f;
            default:
                return null;
        }
    }

    @Override
    public String getDescription() {
        return description;
    }
} 