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

package com.tenio.common.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Exception Classes")
class ExceptionTest {

  @Test
  @DisplayName("MsgPackOperationException should be created with exception")
  void msgPackOperationException_shouldBeCreatedWithException() {
    var originalException = new Exception("Original exception");
    var exception = new MsgPackOperationException(originalException);
    
    assertNotNull(exception);
    assertEquals(originalException, exception.getCause());
    assertEquals("java.lang.Exception: Original exception", exception.getMessage());
    
    // Actually throw and catch the exception to ensure it's used
    try {
      throw exception;
    } catch (MsgPackOperationException e) {
      assertEquals(originalException, e.getCause());
    }
  }

  @Test
  @DisplayName("NullElementPoolException should be created with message")
  void nullElementPoolException_shouldBeCreatedWithMessage() {
    String message = "Test message";
    var exception = new NullElementPoolException(message);
    
    assertNotNull(exception);
    assertEquals(message, exception.getMessage());
    
    // Actually throw and catch the exception to ensure it's used
    try {
      throw exception;
    } catch (NullElementPoolException e) {
      assertEquals(message, e.getMessage());
    }
  }

  @Test
  @DisplayName("RunningScheduledTaskException should be created with default constructor")
  void runningScheduledTaskException_shouldBeCreatedWithDefaultConstructor() throws Exception {
    // Use reflection to create an instance since the class has no public constructor
    Constructor<RunningScheduledTaskException> constructor = 
        RunningScheduledTaskException.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    var exception = constructor.newInstance();
    
    assertNotNull(exception);
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
    
    // Actually throw and catch the exception to ensure it's used
    try {
      throw exception;
    } catch (RunningScheduledTaskException e) {
      assertNull(e.getMessage());
    }
  }

  @Test
  @DisplayName("UnsupportedMsgPackDataTypeException should be created with default constructor")
  void unsupportedMsgPackDataTypeException_shouldBeCreatedWithDefaultConstructor() {
    var exception = new UnsupportedMsgPackDataTypeException();
    
    assertNotNull(exception);
    assertNotNull(exception.getMessage());
    assertTrue(exception.getMessage().contains("The data type is not supported"));
    assertTrue(exception.getMessage().contains("null, boolean, byte, short, int"));
    assertNull(exception.getCause());
    
    // Actually throw and catch the exception to ensure it's used
    try {
      throw exception;
    } catch (UnsupportedMsgPackDataTypeException e) {
      assertTrue(e.getMessage().contains("The data type is not supported"));
    }
  }
} 