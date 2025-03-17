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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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

    @BeforeEach
    void setUp() throws Exception {
        configuration = new TestConfiguration();
        configFile = new File("src/test/resources/test-config.xml");
        largeConfig = createLargeConfig();
    }

    private Map<String, Object> createLargeConfig() {
        Map<String, Object> config = new HashMap<>();
        
        // Add all the TestConfigurationType values
        for (TestConfigurationType type : TestConfigurationType.values()) {
            if (type.getValueType() == String.class) {
                config.put(type.name(), "test_" + type.name().toLowerCase());
            } else if (type.getValueType() == Integer.class) {
                config.put(type.name(), 42);
            } else if (type.getValueType() == Float.class) {
                config.put(type.name(), 3.14f);
            } else if (type.getValueType() == Boolean.class) {
                config.put(type.name(), true);
            }
        }
        
        return config;
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
                configuration.getString(TestConfigurationType.STRING_VALUE);
                configuration.getInt(TestConfigurationType.INT_VALUE);
                configuration.getFloat(TestConfigurationType.FLOAT_VALUE);
                configuration.getBoolean(TestConfigurationType.BOOLEAN_VALUE);
            }, executor));
        }
        CompletableFuture.allOf(warmupFutures.toArray(new CompletableFuture[0])).join();

        // Measure
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < TEST_ITERATIONS; i++) {
            final int iteration = i;
            futures.add(CompletableFuture.runAsync(() -> {
                long startTime = System.nanoTime();
                configuration.getString(TestConfigurationType.STRING_VALUE);
                configuration.getInt(TestConfigurationType.INT_VALUE);
                configuration.getFloat(TestConfigurationType.FLOAT_VALUE);
                configuration.getBoolean(TestConfigurationType.BOOLEAN_VALUE);
                long endTime = System.nanoTime();
                responseTimes.put(iteration, endTime - startTime);
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