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

package com.tenio.common.logger.pool;

import static org.junit.jupiter.api.Assertions.*;

import com.tenio.common.constant.CommonConstant;
import com.tenio.common.exception.NullElementPoolException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For StringBuilderPool")
class StringBuilderPoolTest {

    private StringBuilderPool pool;

    @BeforeEach
    void setUp() {
        pool = StringBuilderPool.getInstance();
    }

    @AfterEach
    void tearDown() {
        pool.cleanup();
    }

    @Test
    @DisplayName("getInstance should return the same instance")
    void getInstance_shouldReturnSameInstance() {
        var instance1 = StringBuilderPool.getInstance();
        var instance2 = StringBuilderPool.getInstance();
        
        assertSame(instance1, instance2);
    }

    @Test
    @DisplayName("get should return a StringBuilder")
    void get_shouldReturnStringBuilder() {
        var builder = pool.get();
        
        assertNotNull(builder);
        assertEquals(0, builder.length());
    }

    @Test
    @DisplayName("get should return different instances when called multiple times")
    void get_shouldReturnDifferentInstances_whenCalledMultipleTimes() {
        var builder1 = pool.get();
        var builder2 = pool.get();
        
        assertNotSame(builder1, builder2);
    }

    @Test
    @DisplayName("get should increase pool size when all elements are used")
    void get_shouldIncreasePoolSize_whenAllElementsAreUsed() throws Exception {
        // Get the initial pool size
        int initialPoolSize = pool.getPoolSize();
        
        // Get all elements from the pool
        StringBuilder[] builders = new StringBuilder[initialPoolSize];
        for (int i = 0; i < initialPoolSize; i++) {
            builders[i] = pool.get();
        }
        
        // Get one more element to trigger pool expansion
        var extraBuilder = pool.get();
        
        // Check that the pool size has increased
        assertEquals(initialPoolSize + CommonConstant.ADDITIONAL_NUMBER_ELEMENTS_POOL, pool.getPoolSize());
        
        // Repay all elements
        for (var builder : builders) {
            pool.repay(builder);
        }
        pool.repay(extraBuilder);
    }

    @Test
    @DisplayName("repay should make the element available again")
    void repay_shouldMakeElementAvailableAgain() {
        var builder = pool.get();
        builder.append("test");
        
        // Get the available slots before repaying
        int availableBefore = pool.getAvailableSlot();
        
        // Repay the element
        pool.repay(builder);
        
        // Check that the element is cleared
        assertEquals(0, builder.length());
        
        // Check that the available slots have increased
        assertEquals(availableBefore + 1, pool.getAvailableSlot());
    }

    @Test
    @DisplayName("repay should throw NullElementPoolException when element is not from the pool")
    void repay_shouldThrowNullElementPoolException_whenElementIsNotFromPool() {
        var externalBuilder = new StringBuilder();
        
        assertThrows(NullElementPoolException.class, () -> pool.repay(externalBuilder));
    }

    @Test
    @DisplayName("cleanup should reset the pool")
    void cleanup_shouldResetPool() throws Exception {
        // Get some elements from the pool
        pool.get();
        pool.get();
        
        // Cleanup the pool
        pool.cleanup();
        
        // Check that the pool size is reset to default
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getPoolSize());
        
        // Check that all slots are available
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getAvailableSlot());
    }

    @Test
    @DisplayName("getPoolSize should return the correct size")
    void getPoolSize_shouldReturnCorrectSize() {
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, pool.getPoolSize());
    }

    @Test
    @DisplayName("getAvailableSlot should return the correct number of available slots")
    void getAvailableSlot_shouldReturnCorrectNumberOfAvailableSlots() {
        // Initially all slots should be available
        int initialAvailable = pool.getAvailableSlot();
        assertEquals(CommonConstant.DEFAULT_NUMBER_ELEMENTS_POOL, initialAvailable);
        
        // Get some elements
        var builder1 = pool.get();
        pool.get();
        
        // Check that available slots have decreased
        assertEquals(initialAvailable - 2, pool.getAvailableSlot());
        
        // Repay one element
        pool.repay(builder1);
        
        // Check that available slots have increased
        assertEquals(initialAvailable - 1, pool.getAvailableSlot());
    }
} 