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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for resolving environment variables in configuration values.
 * <p>
 * This class provides methods to replace environment variable references in
 * string values with their actual values from the system environment. It
 * supports a syntax similar to shell variable expansion, with default values.
 * <p>
 * Environment variable references have the format {@code ${VAR_NAME:default}},
 * where {@code VAR_NAME} is the name of the environment variable and
 * {@code default} is an optional default value to use if the environment
 * variable is not defined.
 * <p>
 * Example:
 * <pre>
 * // Resolves to the value of the PORT environment variable, or "8080" if not defined
 * String port = EnvironmentVariableResolver.resolve("${PORT:8080}");
 * 
 * // Resolves all environment variable references in a map of configuration values
 * Map<String, Object> config = new HashMap<>();
 * config.put("port", "${PORT:8080}");
 * config.put("host", "${HOST:localhost}");
 * EnvironmentVariableResolver.resolveAll(config);
 * </pre>
 */
public final class EnvironmentVariableResolver {

    private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\$\\{([^:}]+)(?::([^}]*))?}");

    private EnvironmentVariableResolver() {
        throw new UnsupportedOperationException("This utility class cannot be instantiated");
    }

    /**
     * Resolves environment variable references in a string value.
     * <p>
     * This method replaces all occurrences of environment variable references in
     * the input string with their actual values from the system environment or
     * system properties. If a variable is not found in the environment, it will
     * check system properties. If the variable is not defined in either place and
     * a default value is provided in the reference, the default value is used instead.
     * <p>
     * If the input string does not contain any environment variable references,
     * it is returned unchanged.
     *
     * @param value the string value that may contain environment variable references
     * @return the string with all environment variable references resolved
     */
    public static String resolve(String value) {
        if (value == null || !value.contains("${")) {
            return value;
        }

        Matcher matcher = ENV_VAR_PATTERN.matcher(value);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String varName = matcher.group(1);
            String defaultValue = matcher.group(2);
            
            // First check environment variables
            String varValue = System.getenv(varName);
            
            // If not found in environment, check system properties
            if (varValue == null) {
                varValue = System.getProperty(varName);
            }

            // Use default value if provided and variable not found
            if (varValue == null && defaultValue != null) {
                varValue = defaultValue;
            } else if (varValue == null) {
                varValue = "";
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(varValue));
        }

        matcher.appendTail(result);
        return result.toString();
    }

    /**
     * Resolves environment variable references in all string values in a map.
     * <p>
     * This method iterates through all entries in the input map and resolves
     * environment variable references in any string values. The map is modified
     * in place, with string values containing environment variable references
     * replaced by their resolved values.
     * <p>
     * Non-string values in the map are left unchanged.
     *
     * @param map the map containing values that may include environment variable references
     * @return the same map with all environment variable references resolved
     */
    public static Map<String, Object> resolveAll(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                entry.setValue(resolve((String) entry.getValue()));
            }
        }
        return map;
    }
} 