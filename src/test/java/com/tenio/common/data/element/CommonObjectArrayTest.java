package com.tenio.common.data.element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommonObjectArrayTest {

  @Test
  void testConstructor() {
    assertTrue((new CommonObjectArray()).isEmpty());
  }

  @Test
  void testNewInstance() {
    assertTrue(CommonObjectArray.newInstance().isEmpty());
  }

  @Test
  void testPut() {
    CommonObjectArray newInstanceResult = CommonObjectArray.newInstance();
    newInstanceResult.add("42");
    CommonObjectArray actualPutResult = newInstanceResult.put("Value");
    assertSame(newInstanceResult, actualPutResult);
    assertEquals(2, actualPutResult.size());
    assertEquals("42", actualPutResult.get(0));
    assertEquals("Value", actualPutResult.get(1));
  }

  @Test
  void testGetString() {
    CommonObjectArray newInstanceResult = CommonObjectArray.newInstance();
    newInstanceResult.add("42");
    newInstanceResult.add("42");
    assertEquals("42", newInstanceResult.getString(1));
  }

  @Test
  void testGetString2() {
    CommonObjectArray newInstanceResult = CommonObjectArray.newInstance();
    newInstanceResult.add("42");
    newInstanceResult.add("foo");
    assertEquals("foo", newInstanceResult.getString(1));
  }

  @Test
  void testGetObject() {
    CommonObjectArray newInstanceResult = CommonObjectArray.newInstance();
    newInstanceResult.add("42");
    newInstanceResult.add("42");
    assertEquals("42", newInstanceResult.getObject(1));
  }
}

