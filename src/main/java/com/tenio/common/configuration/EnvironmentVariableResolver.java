package com.tenio.common.configuration;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Resolves environment variables in configuration values.
 */
public class EnvironmentVariableResolver {

    private static final Pattern ENV_VAR_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    /**
     * Resolves environment variables in a string value.
     * Environment variables can be specified in the format ${VAR_NAME} or ${VAR_NAME:default_value}.
     *
     * @param value the value to resolve
     * @return the resolved value
     */
    public static String resolve(String value) {
        if (value == null) {
            return null;
        }

        Matcher matcher = ENV_VAR_PATTERN.matcher(value);
        StringBuffer result = new StringBuffer();

        while (matcher.find()) {
            String envVar = matcher.group(1);
            String resolved = resolveEnvVar(envVar);
            matcher.appendReplacement(result, Matcher.quoteReplacement(resolved));
        }
        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Resolves environment variables in a map of values.
     *
     * @param values the map of values to resolve
     * @return a new map with resolved values
     */
    public static Map<String, Object> resolveAll(Map<String, Object> values) {
        return values.entrySet().stream()
            .collect(java.util.stream.Collectors.toMap(
                Map.Entry::getKey,
                entry -> resolveValue(entry.getValue())
            ));
    }

    private static Object resolveValue(Object value) {
        if (value instanceof String) {
            return resolve((String) value);
        }
        return value;
    }

    private static String resolveEnvVar(String envVar) {
        String[] parts = envVar.split(":", 2);
        String varName = parts[0].trim();
        String defaultValue = parts.length > 1 ? parts[1].trim() : "";

        String value = System.getenv(varName);
        if (value == null) {
            value = System.getProperty(varName, defaultValue);
        }

        return value;
    }
} 