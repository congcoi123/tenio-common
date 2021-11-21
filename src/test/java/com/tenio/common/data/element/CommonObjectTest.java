package com.tenio.common.data.element;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CommonObjectTest {
  @Test
  void testConstructor() {
    assertTrue((new CommonObject()).isEmpty());
  }

  @Test
  void testNewInstance() {
    assertTrue(CommonObject.newInstance().isEmpty());
  }

  @Test
  void testGetString() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.put("foo", "42");
    assertNull(newInstanceResult.getString("Key"));
  }

  @Test
  void testGetString2() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.add("Key", "42");
    newInstanceResult.put("foo", "42");
    assertEquals("42", newInstanceResult.getString("Key"));
  }

  @Test
  void testGetObject() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.put("foo", "42");
    assertNull(newInstanceResult.getObject("Key"));
  }

  @Test
  void testGetMessageObject() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.put("foo", "42");
    assertNull(newInstanceResult.getMessageObject("Key"));
  }

  @Test
  void testGetMessageObjectArray() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.put("foo", "42");
    assertNull(newInstanceResult.getMessageObjectArray("Key"));
  }

  @Test
  void testContains() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.put("foo", "42");
    assertFalse(newInstanceResult.contains("Key"));
  }

  @Test
  void testContains2() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.add("Key", "Value");
    newInstanceResult.put("foo", "42");
    assertTrue(newInstanceResult.contains("Key"));
  }

  @Test
  void testAdd() {
    CommonObject newInstanceResult = CommonObject.newInstance();
    newInstanceResult.put("foo", "42");
    CommonObject actualAddResult = newInstanceResult.add("Key", "Value");
    assertSame(newInstanceResult, actualAddResult);
    assertEquals(2, actualAddResult.size());
  }
}

