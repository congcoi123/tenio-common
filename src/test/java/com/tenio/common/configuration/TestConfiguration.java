package com.tenio.common.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * Test implementation of CommonConfiguration for unit testing.
 */
public class TestConfiguration extends CommonConfiguration {

    @Override
    protected void loadInternal(String file) throws ConfigurationException {
        try {
            Properties properties = new Properties();
            File configFile = new File(file);
            
            if (!configFile.exists()) {
                throw new ConfigurationException("Configuration file not found: " + file,
                    null, ConfigurationException.ErrorType.LOAD_ERROR);
            }

            try (FileInputStream fis = new FileInputStream(configFile)) {
                properties.load(fis);
            }

            // Load standard properties
            for (TestConfigurationType type : TestConfigurationType.values()) {
                String value = properties.getProperty(type.name());
                if (value != null) {
                    push(type, convertValue(value, type.getValueType()));
                }
            }

            // Handle extensions
            Map<String, String> extensions = properties.entrySet().stream()
                .filter(e -> !isStandardProperty(e.getKey().toString()))
                .collect(java.util.stream.Collectors.toMap(
                    e -> e.getKey().toString(),
                    e -> e.getValue().toString()
                ));

            extend(extensions);

        } catch (IOException e) {
            throw new ConfigurationException("Failed to load configuration file: " + file,
                e, null, ConfigurationException.ErrorType.LOAD_ERROR);
        }
    }

    @Override
    protected void extend(Map<String, String> extProperties) {
        // Test implementation doesn't need extensions
    }

    @Override
    protected ConfigurationType getConfigurationType(String name) {
        try {
            return TestConfigurationType.valueOf(name);
        } catch (IllegalArgumentException e) {
            // For performance tests, handle dynamic keys
            if (name.startsWith("string_") || name.startsWith("int_") || 
                name.startsWith("float_") || name.startsWith("bool_") ||
                name.startsWith("nested_")) {
                return new DynamicConfigurationType(name);
            }
            return null;
        }
    }

    private boolean isStandardProperty(String key) {
        try {
            TestConfigurationType.valueOf(key);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private Object convertValue(String value, Class<?> targetType) throws ConfigurationException {
        try {
            if (targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == Float.class) {
                return Float.parseFloat(value);
            } else if (targetType == String.class) {
                return value;
            } else {
                throw new ConfigurationException("Unsupported type: " + targetType.getSimpleName(),
                    null, ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
            }
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Failed to convert value: " + value,
                e, null, ConfigurationException.ErrorType.PARSE_ERROR);
        }
    }

    @Override
    protected ConfigurationType[] getConfigurationTypes() {
        return TestConfigurationType.values();
    }

    @Override
    public void validate() throws ConfigurationException {
        super.validate();
        
        // Additional validation logic for TestConfiguration
        for (TestConfigurationType type : TestConfigurationType.values()) {
            if (isDefined(type)) {
                Object value = get(type, Object.class).orElse(null);
                if (!type.getValidator().test(value)) {
                    throw new ConfigurationException(
                        "Validation failed for " + type + ": " + value,
                        type,
                        ConfigurationException.ErrorType.VALIDATION_FAILED
                    );
                }
            }
        }
    }

    // Inner class to handle dynamic configuration types for performance tests
    private static class DynamicConfigurationType implements ConfigurationType {
        private final String name;
        
        public DynamicConfigurationType(String name) {
            this.name = name;
        }
        
        @Override
        public Class<?> getValueType() {
            if (name.startsWith("string_")) {
                return String.class;
            } else if (name.startsWith("int_")) {
                return Integer.class;
            } else if (name.startsWith("float_")) {
                return Float.class;
            } else if (name.startsWith("bool_")) {
                return Boolean.class;
            } else {
                return Object.class;
            }
        }
        
        @Override
        public boolean isRequired() {
            return false;
        }
        
        @Override
        public java.util.function.Predicate<Object> getValidator() {
            return obj -> true;
        }
        
        @Override
        public Object getDefaultValue() {
            return null;
        }
        
        @Override
        public String getDescription() {
            return "Dynamic configuration type for performance testing";
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
} 