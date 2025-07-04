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

package com.tenio.common.data.common;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Common Array")
class CommonArrayTest {

  private final CommonArray dummyArray = CommonArray.newInstance();
  private final CommonArray commonArray = CommonArray.newInstance();

  @BeforeEach
  void initialization() {
    commonArray.put(100.0).put(100.0F).put(100L).put(100).put(true).put("test")
        .put(dummyArray).put(dummyArray);
  }

  @Test
  @DisplayName("Allow fetching all inserted data from the array")
  void itShouldFetchAllInsertedData() {
    assertAll("itShouldFetchAllInsertedData",
        () -> assertEquals(100.0, commonArray.getDouble(0)),
        () -> assertEquals(100.0f, commonArray.getFloat(1)),
        () -> assertEquals(100L, commonArray.getLong(2)),
        () -> assertEquals(100, commonArray.getInt(3)),
        () -> assertTrue(commonArray.getBoolean(4)),
        () -> assertEquals("test", commonArray.getString(5)),
        () -> assertEquals(dummyArray, commonArray.getCommonObjectArray(6)),
        () -> assertInstanceOf(CommonArray.class, commonArray.getObject(7))
    );
  }

  @Test
  @DisplayName("An exception should be thrown when a readonly array is tried to modify")
  void itShouldThrowExceptionWhenTryToModifyReadonlyArray() {
    var readonlyList = commonArray.getReadonlyList();
    Assertions.assertThrows(UnsupportedOperationException.class,
        () -> readonlyList.add("new value"));
  }
}
