/*
The MIT License

Copyright (c) 2016-2025 kong <congcoi123@gmail.com>

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

package com.tenio.common.worker;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Worker Pool Runnable Utility")
class WorkerPoolTest {

  @Test
  @DisplayName("Creating a new instance should work")
  void testConstructor() {
    new WorkerPool("Name", 1, 3);
  }

  @Test
  @DisplayName("Executing a runnable worker delivers expected results")
  void testExecute() throws IllegalStateException {
    var workerPool = new WorkerPool("Name", 1, 3);
    workerPool.execute(mock(Runnable.class), "Test Runnable Worker");
    workerPool.stop();
    Assertions.assertThrows(IllegalStateException.class,
        () -> workerPool.execute(mock(Runnable.class), "Test Runnable Worker"));
  }


  @Test
  void testWaitUntilAllTasksFinished() {
    (new WorkerPool("Name", 1, 3)).waitUntilAllTasksFinished();
  }
}
