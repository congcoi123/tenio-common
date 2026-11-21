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

package com.tenio.common.logger.pool;

import com.tenio.common.constant.CommonConstant;
import com.tenio.common.exception.NullElementPoolException;
import com.tenio.common.pool.ElementPool;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The object pool mechanism for {@link StringBuilder}.
 */
public final class StringBuilderPool implements ElementPool<StringBuilder> {

  private static volatile StringBuilderPool instance;
  private static final int MAX_RETAINED_CAPACITY = 8 * 1024;

  private final ConcurrentLinkedQueue<StringBuilder> available;
  private final Set<StringBuilder> borrowed;
  private final Object expansionLock;

  private StringBuilderPool() {
    available = new ConcurrentLinkedQueue<>();
    borrowed = ConcurrentHashMap.newKeySet();
    expansionLock = new Object();
    refill(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL);
  }

  /**
   * Preventing Singleton object instantiation from outside creates multiple instance if two
   * thread access this method simultaneously.
   *
   * @return the instance of this class
   */
  public static StringBuilderPool getInstance() {
    var reference = instance;
    if (reference == null) {
      synchronized (StringBuilderPool.class) {
        reference = instance;
        if (reference == null) {
          instance = reference = new StringBuilderPool();
        }
      }
    }
    return reference;
  }

  private void refill(int count) {
    for (int i = 0; i < count; i++) {
      available.offer(new StringBuilder());
    }
  }

  @Override
  public StringBuilder get() {
    var element = available.poll();
    if (element == null) {
      synchronized (expansionLock) {
        element = available.poll();
        if (element == null) {
          refill(CommonConstant.ADDITIONAL_NUMBER_ELEMENTS_POOL);
          element = available.poll();
        }
      }
    }
    if (!borrowed.add(element)) {
      throw new IllegalStateException("StringBuilder was borrowed more than once");
    }
    return element;
  }

  @Override
  public void repay(StringBuilder element) {
    if (element == null || !borrowed.remove(element)) {
      throw new NullElementPoolException(String.valueOf(element));
    }
    if (element.capacity() > MAX_RETAINED_CAPACITY) {
      available.offer(new StringBuilder());
    } else {
      element.setLength(0);
      available.offer(element);
    }
  }

  @Override
  public void cleanup() {
    synchronized (expansionLock) {
      available.clear();
      borrowed.clear();
      refill(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL);
    }
  }

  @Override
  public int getPoolSize() {
    return available.size() + borrowed.size();
  }

  @Override
  public int getAvailableSlot() {
    return available.size();
  }
}
