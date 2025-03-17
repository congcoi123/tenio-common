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

package com.tenio.common.logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.base.Throwables;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit Test Cases For AbstractLogger")
class AbstractLoggerTest {

  private Logger mockLogger;
  private TestLogger logger;

  private static class TestLogger extends AbstractLogger {
    // No need to override anything - we'll use reflection to set the logger field
  }

  @BeforeEach
  void setUp() throws Exception {
    mockLogger = mock(Logger.class);
    logger = new TestLogger();
    
    // Use reflection to set the logger field
    var loggerField = AbstractLogger.class.getDeclaredField("logger");
    loggerField.setAccessible(true);
    loggerField.set(logger, mockLogger);
  }

  @Test
  @DisplayName("error with throwable should log when enabled")
  void error_withThrowable_shouldLogWhenEnabled() {
    when(mockLogger.isErrorEnabled()).thenReturn(true);
    Throwable throwable = new RuntimeException("Test exception");
    logger.error(throwable);
    verify(mockLogger).error(Throwables.getStackTraceAsString(throwable));
  }

  @Test
  @DisplayName("info with string tag and object should log when enabled")
  void info_withStringTagAndObject_shouldLogWhenEnabled() {
    when(mockLogger.isInfoEnabled()).thenReturn(true);
    logger.info("TAG", "message");
    verify(mockLogger).info("[TAG] message");
  }

  @Test
  @DisplayName("info with string tag and object should not log when disabled")
  void info_withStringTagAndObject_shouldNotLogWhenDisabled() {
    when(mockLogger.isInfoEnabled()).thenReturn(false);
    logger.info("TAG", "message");
    verify(mockLogger, never()).info(anyString());
  }

  @Test
  @DisplayName("buildgen should create StringBuilder with concatenated content")
  void buildgen_shouldCreateStringBuilderWithConcatenatedContent() {
    StringBuilder result = logger.buildgen("Hello", " ", "World");
    assertEquals("Hello World", result.toString());
  }

  @Test
  @DisplayName("buildgen with mixed types should concatenate properly")
  void buildgen_withMixedTypes_shouldConcatenateProperly() {
    StringBuilder result = logger.buildgen("Count: ", 42, " and ", true);
    assertEquals("Count: 42 and true", result.toString());
  }

  @Test
  @DisplayName("buildgen with no objects should return empty StringBuilder")
  void buildgen_withNoObjects_shouldReturnEmptyStringBuilder() {
    StringBuilder result = logger.buildgen();
    assertEquals("", result.toString());
  }
} 