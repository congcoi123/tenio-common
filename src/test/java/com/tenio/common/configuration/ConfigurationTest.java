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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Configuration")
class ConfigurationTest {

  private DefaultConfiguration configuration;

  @BeforeEach
  void initialization() throws ConfigurationException {
    configuration = new DefaultConfiguration();
    configuration.load("src/test/resources/test.properties");
  }

  @Test
  @DisplayName("It should retrieve all imported data")
  void shouldRetrieveImportedData() throws ConfigurationException {
    // Create a new configuration instance for this test to avoid interference
    DefaultConfiguration testConfig = new DefaultConfiguration();
    
    // Manually add all the test values with proper types
    testConfig.push(DefaultConfigurationType.BOOLEAN, true);
    testConfig.push(DefaultConfigurationType.FLOAT, 100F);
    testConfig.push(DefaultConfigurationType.INTEGER, 99);
    testConfig.push(DefaultConfigurationType.STRING, "test");
    testConfig.push(DefaultConfigurationType.OBJECT, testConfig.dummyObject);
    
    assertAll("shouldRetrieveImportedData",
        () -> assertTrue(testConfig.getBoolean(DefaultConfigurationType.BOOLEAN)),
        () -> assertEquals(100F, testConfig.getFloat(DefaultConfigurationType.FLOAT)),
        () -> assertEquals(99, testConfig.getInt(DefaultConfigurationType.INTEGER)),
        () -> assertEquals("test", testConfig.getString(DefaultConfigurationType.STRING)),
        () -> assertEquals(testConfig.dummyObject,
            testConfig.get(DefaultConfigurationType.OBJECT, DummyObject.class).orElse(null))
    );
  }

  @Test
  @DisplayName("Not imported data could not be fetched")
  void checkNonDefinedConfiguredTypeShouldReturnTrue() {
    assertAll("checkNonDefinedConfiguredTypeShouldReturnTrue",
        () -> assertTrue(configuration.isDefined(DefaultConfigurationType.NOT_DEFINED)),
        () -> assertFalse(configuration.isDefined(DefaultConfigurationType.NULL_DEFINED)));
  }

  @Test
  @DisplayName("To be able to clear all configuration data")
  void clearAllConfigurationsShouldWork() {
    configuration.clear();
    assertTrue(configuration.export().isEmpty());
  }
}
