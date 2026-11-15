/*
The MIT License

Copyright (c) 2016-2026 kong <congcoi123@gmail.com>

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

package com.tenio.common.task.implement;

import com.tenio.common.exception.RunningScheduledTaskException;
import com.tenio.common.logger.SystemLogger;
import com.tenio.common.task.Task;
import com.tenio.common.task.TaskManager;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class uses Java scheduler ({@link ScheduledFuture}) to manage your
 * tasks. The scheduler is used to schedule a thread or task that executes at a
 * certain period of time or periodically at a fixed interval. It's useful when
 * you want to create a time counter before starting a match or send messages
 * periodically for one player.
 *
 * @see TaskManager
 */
public final class TaskManagerImpl extends SystemLogger implements TaskManager {

  /**
   * A list of tasks in the server.
   */
  private final Map<String, Task> tasks;
  private final Object lifecycleLock;
  private long lifecycleVersion;

  private TaskManagerImpl() {
    tasks = new ConcurrentHashMap<>();
    lifecycleLock = new Object();
  }

  /**
   * Creates a new instance for task manager.
   *
   * @return an instance in {@link TaskManager} type
   */
  public static TaskManager newInstance() {
    return new TaskManagerImpl();
  }

  @Override
  public void create(String id, Task task) {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(task, "task");

    final long observedVersion;
    synchronized (lifecycleLock) {
      var existingTask = tasks.get(id);
      if (isActive(existingTask)) {
        if (isErrorEnabled()) {
          error(new RunningScheduledTaskException(), "task id: ", id);
        }
        return;
      }
      observedVersion = lifecycleVersion;
    }

    task.run();
    var scheduler = Objects.requireNonNull(task.getScheduler(), "A task must expose its scheduler after run()");

    boolean installed = false;
    synchronized (lifecycleLock) {
      var existingTask = tasks.get(id);
      if (lifecycleVersion == observedVersion && !isActive(existingTask)) {
        tasks.put(id, task);
        installed = true;
      }
    }
    if (!installed) {
      task.shutdown();
      return;
    }
    if (isInfoEnabled()) {
      info("RUN TASK", buildgen(id, " >Time left> ", scheduler.getDelay(TimeUnit.SECONDS), " seconds"));
    }
  }

  @Override
  public void kill(String id) {
    Objects.requireNonNull(id, "id");
    final Task task;
    synchronized (lifecycleLock) {
      lifecycleVersion++;
      task = tasks.remove(id);
    }
    if (task != null) {
      task.shutdown();
      if (isInfoEnabled()) {
        info("KILLED TASK", id);
      }
    }
  }

  @Override
  public void clear() {
    final Map<String, Task> tasksToStop;
    synchronized (lifecycleLock) {
      lifecycleVersion++;
      tasksToStop = new java.util.HashMap<>(tasks);
      tasks.clear();
    }
    for (var entry : tasksToStop.entrySet()) {
      if (isInfoEnabled()) {
        info("KILLED TASK", entry.getKey());
      }
      entry.getValue().shutdown();
    }
  }

  @Override
  public int getRemainTime(String id) {
    Objects.requireNonNull(id, "id");
    var task = tasks.get(id);
    if (task != null) {
      return (int) task.getScheduler().getDelay(TimeUnit.SECONDS);
    }
    return -1;
  }

  private static boolean isActive(Task task) {
    if (task == null) {
      return false;
    }
    var scheduler = task.getScheduler();
    return scheduler != null && !scheduler.isDone();
  }
}
