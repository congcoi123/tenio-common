package com.tenio.common.configuration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.tenio.common.configuration.format.ConfigurationFormatHandler;
import com.tenio.common.configuration.format.YamlConfigurationHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Performance tests for the configuration system.
 * These tests measure the performance of various operations under different conditions.
 */
class ConfigurationPerformanceTest {

    @TempDir
    File tempDir;

    private TestConfiguration configuration;
    private Map<String, Object> largeConfig;
    private File configFile;
    private static final int WARMUP_ITERATIONS = 10;
    private static final int TEST_ITERATIONS = 100;
    private static final int CONCURRENT_THREADS = 4;
    private static final int LARGE_CONFIG_SIZE = 50;

    @BeforeEach
    void setUp() throws IOException, ConfigurationException {
        configuration = new TestConfiguration();
        configFile = new File(tempDir, "performance-test.yml");
        largeConfig = createLargeConfig();
        createConfigFile();
    }

    private Map<String, Object> createLargeConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // Add required configuration values
        config.put("STRING_VALUE", "test string");
        config.put("INT_VALUE", 42);
        config.put("FLOAT_VALUE", 3.14f);
        config.put("SERVER_NAME", "Test Server");
        
        // Add dynamic configuration values
        for (int i = 0; i < LARGE_CONFIG_SIZE; i++) {
            config.put("string_" + i, "value_" + i);
            config.put("int_" + i, i);
            config.put("bool_" + i, i % 2 == 0);
            
            // Add nested structures
            Map<String, Object> nested = new HashMap<>();
            nested.put("nested_string_" + i, "nested_value_" + i);
            nested.put("nested_int_" + i, i * 2);
            config.put("nested_" + i, nested);
        }
        return config;
    }

    private void createConfigFile() throws IOException, ConfigurationException {
        ConfigurationFormatHandler handler = new YamlConfigurationHandler();
        handler.save(configFile, largeConfig);
    }

    @Test
    void testLoadPerformance() throws Exception {
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            configuration.load(configFile.getAbsolutePath());
            configuration.clear();
        }

        // Measure
        long startTime = System.nanoTime();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            configuration.load(configFile.getAbsolutePath());
            configuration.clear();
        }
        long endTime = System.nanoTime();

        double averageTimeMs = (endTime - startTime) / (TEST_ITERATIONS * 1_000_000.0);
        System.out.printf("Average load time: %.3f ms%n", averageTimeMs);
        
        // Assert reasonable performance (adjust threshold based on your requirements)
        assertTrue(averageTimeMs < 100.0, "Load operation took too long: " + averageTimeMs + "ms");
    }

    @Test
    void testConcurrentReadPerformance() throws Exception {
        configuration.load(configFile.getAbsolutePath());
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_THREADS);
        ConcurrentHashMap<Integer, Long> responseTimes = new ConcurrentHashMap<>();

        // Warmup
        List<CompletableFuture<Void>> warmupFutures = new ArrayList<>();
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            warmupFutures.add(CompletableFuture.runAsync(() -> {
                try {
                    configuration.getString(TestConfigurationType.STRING_VALUE);
                    configuration.getInt(TestConfigurationType.INT_VALUE);
                    configuration.getFloat(TestConfigurationType.FLOAT_VALUE);
                    configuration.getBoolean(TestConfigurationType.BOOLEAN_VALUE);
                } catch (ConfigurationException e) {
                    fail("Concurrent read failed", e);
                }
            }, executor));
        }
        CompletableFuture.allOf(warmupFutures.toArray(new CompletableFuture[0])).join();

        // Measure
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            final int iteration = i;
            futures.add(CompletableFuture.runAsync(() -> {
                long startTime = System.nanoTime();
                try {
                    configuration.getString(TestConfigurationType.STRING_VALUE);
                    configuration.getInt(TestConfigurationType.INT_VALUE);
                    configuration.getFloat(TestConfigurationType.FLOAT_VALUE);
                    configuration.getBoolean(TestConfigurationType.BOOLEAN_VALUE);
                    long endTime = System.nanoTime();
                    responseTimes.put(iteration, endTime - startTime);
                } catch (ConfigurationException e) {
                    fail("Concurrent read failed", e);
                }
            }, executor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        double averageTimeMs = responseTimes.values().stream()
            .mapToLong(Long::longValue)
            .average()
            .orElse(0.0) / 1_000_000.0;

        System.out.printf("Average concurrent read time: %.3f ms%n", averageTimeMs);
        assertTrue(averageTimeMs < 1.0, "Concurrent read operation took too long: " + averageTimeMs + "ms");
    }

    @Test
    void testMemoryUsage() throws Exception {
        // Force garbage collection before test
        System.gc();
        Thread.sleep(100);
        
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Create multiple configuration instances with large data
        List<TestConfiguration> configurations = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            TestConfiguration config = new TestConfiguration();
            config.loadConfiguration(largeConfig);
            configurations.add(config);
        }

        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long memoryUsedPerConfig = (endMemory - startMemory) / configurations.size();

        System.out.printf("Memory used per configuration: %d bytes%n", memoryUsedPerConfig);
        assertTrue(memoryUsedPerConfig < 10 * 1024 * 1024, // 10MB
            "Memory usage per configuration too high: " + memoryUsedPerConfig + " bytes");

        // Clean up
        configurations.clear();
        System.gc();
    }

    @Test
    void testValidationPerformance() throws Exception {
        configuration.loadConfiguration(largeConfig);

        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            configuration.validate();
        }

        // Measure
        long startTime = System.nanoTime();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            configuration.validate();
        }
        long endTime = System.nanoTime();

        double averageTimeMs = (endTime - startTime) / (TEST_ITERATIONS * 1_000_000.0);
        System.out.printf("Average validation time: %.3f ms%n", averageTimeMs);
        assertTrue(averageTimeMs < 1.0, "Validation operation took too long: " + averageTimeMs + "ms");
    }

    @Test
    void testEnvironmentVariableResolutionPerformance() throws Exception {
        // Create config with environment variable references
        Map<String, Object> envConfig = new HashMap<>();
        for (int i = 0; i < 20; i++) {
            envConfig.put("env_" + i, "${TEST_VAR_" + i + ":default_" + i + "}");
        }

        // Warmup
        for (int i = 0; i < 5; i++) {
            EnvironmentVariableResolver.resolveAll(envConfig);
        }

        // Measure
        long startTime = System.nanoTime();
        for (int i = 0; i < 50; i++) {
            EnvironmentVariableResolver.resolveAll(envConfig);
        }
        long endTime = System.nanoTime();

        double averageTimeMs = (endTime - startTime) / (TEST_ITERATIONS * 1_000_000.0);
        System.out.printf("Average environment variable resolution time: %.3f ms%n", averageTimeMs);
        assertTrue(averageTimeMs < 10.0, 
            "Environment variable resolution took too long: " + averageTimeMs + "ms");
    }
} 