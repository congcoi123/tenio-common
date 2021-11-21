package com.tenio.common.worker;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.concurrent.ArrayBlockingQueue;
import org.junit.jupiter.api.Test;

class WorkerPoolRunnableTest {
  @Test
  void testConstructor() {
    assertFalse(
        (new WorkerPoolRunnable("Name", 1, new ArrayBlockingQueue<Runnable>(3))).isStopped());
    assertFalse(
        (new WorkerPoolRunnable("Name", 1, new ArrayBlockingQueue<Runnable>(3))).isStopped());
  }
}

