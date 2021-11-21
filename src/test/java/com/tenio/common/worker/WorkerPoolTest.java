package com.tenio.common.worker;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class WorkerPoolTest {
  @Test
  void testConstructor() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     WorkerPool.taskQueue
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.name
    //     WorkerPool.isStopped
    //     AbstractLogger.logger

    new WorkerPool("Name", 1, 3);

  }

  @Test
  void testConstructor2() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     WorkerPool.taskQueue
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.name
    //     WorkerPool.isStopped
    //     AbstractLogger.logger

    new WorkerPool("CREATED NEW WORKERS", 1, 3);

  }

  @Test
  void testConstructor3() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     WorkerPool.taskQueue
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.name
    //     WorkerPool.isStopped
    //     AbstractLogger.logger

    new WorkerPool("Name", Double.SIZE, 3);

  }

  @Test
  void testExecute() throws IllegalStateException {
    // TODO: This test is incomplete.
    //   Reason: R004 No meaningful assertions found.
    //   Diffblue Cover was unable to create an assertion.
    //   Make sure that fields modified by execute(Runnable, String)
    //   have package-private, protected, or public getters.
    //   See https://diff.blue/R004 to resolve this issue.

    (new WorkerPool("Name", 1, 3)).execute(mock(Runnable.class), "Debug Text");
  }

  @Test
  void testExecute2() throws IllegalStateException {
    // TODO: This test is incomplete.
    //   Reason: R004 No meaningful assertions found.
    //   Diffblue Cover was unable to create an assertion.
    //   Make sure that fields modified by execute(Runnable, String)
    //   have package-private, protected, or public getters.
    //   See https://diff.blue/R004 to resolve this issue.

    (new WorkerPool("com.tenio.common.worker.WorkerPoolRunnable", 1, 3)).execute(
        mock(Runnable.class),
        "EXECUTED A TASK");
  }

  @Test
  void testStop() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("Name", 1, 3)).stop();
  }

  @Test
  void testStop2() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("42", 1, 3)).stop();
  }

  @Test
  void testStop3() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("com.tenio.common.worker.WorkerPoolRunnable", 1, 1)).stop();
  }

  @Test
  void testStop4() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("com.tenio.common.worker.WorkerPoolRunnable", 2, 1)).stop();
  }

  @Test
  void testStop5() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("com.tenio.common.worker.WorkerPoolRunnable", 3, 1)).stop();
  }

  @Test
  void testStop6() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("Name", 3, 1)).stop();
  }

  @Test
  void testWaitUntilAllTasksFinished() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("Name", 1, 3)).waitUntilAllTasksFinished();
  }

  @Test
  void testWaitUntilAllTasksFinished2() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("com.tenio.common.worker.WorkerPoolRunnable", 1,
        3)).waitUntilAllTasksFinished();
  }

  @Test
  void testWaitUntilAllTasksFinished3() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("Name", 1, 1)).waitUntilAllTasksFinished();
  }

  @Test
  void testWaitUntilAllTasksFinished4() {
    // TODO: This test is incomplete.
    //   Reason: R002 Missing observers.
    //   Diffblue Cover was unable to create an assertion.
    //   Add getters for the following fields or make them package-private:
    //     AbstractLogger.logger
    //     WorkerPool.isStopped
    //     WorkerPool.name
    //     WorkerPool.runnableWorkerPools
    //     WorkerPool.taskQueue

    (new WorkerPool("Name", 2, 1)).waitUntilAllTasksFinished();
  }
}

