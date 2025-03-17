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

import com.tenio.common.pool.ElementPool;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemLoggerTest {

  private static class TestSystemLogger extends SystemLogger {
    // Test implementation
  }

  @Mock
  private Logger logger;

  @Mock
  private ElementPool<StringBuilder> mockStringPool;

  private TestSystemLogger systemLogger;

  @BeforeEach
  void setUp() throws Exception {
    systemLogger = new TestSystemLogger();

    // Set logger field
    Field loggerField = AbstractLogger.class.getDeclaredField("logger");
    loggerField.setAccessible(true);
    loggerField.set(systemLogger, logger);

    // Set stringPool field using Unsafe
    Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
    theUnsafeField.setAccessible(true);
    Unsafe unsafe = (Unsafe) theUnsafeField.get(null);

    Field stringPoolField = AbstractLogger.class.getDeclaredField("stringPool");
    unsafe.putObject(
        AbstractLogger.class,
        unsafe.staticFieldOffset(stringPoolField),
        mockStringPool
    );

    // Mock stringPool behavior with lenient stubs
    lenient().when(mockStringPool.get()).thenAnswer(invocation -> new StringBuilder());
    lenient().doNothing().when(mockStringPool).repay(any(StringBuilder.class));
  }

  @Test
  void info_withStringTagAndObject_shouldLogWhenEnabled() {
    when(logger.isInfoEnabled()).thenReturn(true);

    systemLogger.info("tag", "message");

    verify(logger).info(anyString());
  }

  @Test
  void info_withStringTagAndObject_shouldNotLogWhenDisabled() {
    when(logger.isInfoEnabled()).thenReturn(false);

    systemLogger.info("tag", "message");

    verify(logger, never()).info(anyString());
  }

  @Test
  void error_withThrowable_shouldLogWhenEnabled() {
    when(logger.isErrorEnabled()).thenReturn(true);
    Throwable throwable = new RuntimeException("test");

    systemLogger.error(throwable);

    verify(logger).error(anyString());
  }

  @Test
  void error_withThrowableAndExtra_shouldLogWhenEnabled() {
    when(logger.isErrorEnabled()).thenReturn(true);
    Throwable throwable = new RuntimeException("test");

    systemLogger.error(throwable, "extra1", "extra2");

    verify(logger).error(anyString());
  }

  @Test
  void buildgen_shouldCreateStringBuilderWithConcatenatedContent() {
    StringBuilder result = systemLogger.buildgen("Hello", " ", "World");

    verify(mockStringPool).get();
    assert result != null;
    assert result.toString().equals("Hello World");
  }

  @Test
  void buildgen_withMixedTypes_shouldConcatenateProperly() {
    StringBuilder result = systemLogger.buildgen("Count: ", 42, " ", true);

    verify(mockStringPool).get();
    assert result != null;
    assert result.toString().equals("Count: 42 true");
  }
} 