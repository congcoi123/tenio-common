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

@DisplayName("Unit Test Cases For RunningScheduledTaskException")
class RunningScheduledTaskExceptionTest {

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
} 