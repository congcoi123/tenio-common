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

package com.tenio.common.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Immutable Snapshot")
class ImmutableSnapshotTest {

  @Test
  @DisplayName("Throw an exception when the class's instance is attempted creating")
  void createNewInstanceShouldThrowException() throws NoSuchMethodException {
    Constructor<ImmutableSnapshot> constructor =
        ImmutableSnapshot.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    assertThrows(InvocationTargetException.class, () -> {
      constructor.setAccessible(true);
      constructor.newInstance();
    });
  }

  @Test
  @SuppressWarnings("unchecked")
  @DisplayName("ofMap should produce an unmodifiable recursive snapshot")
  void ofMapShouldProduceUnmodifiableRecursiveSnapshot() {
    var inner = new HashMap<String, Object>();
    inner.put("innerValue", 1);
    var source = new HashMap<String, Object>();
    source.put("nested", inner);
    source.put("list", List.of(1, 2));
    source.put("nullValue", null);

    Map<String, Object> snapshot = ImmutableSnapshot.ofMap(source);

    assertAll("ofMapShouldProduceUnmodifiableRecursiveSnapshot",
        () -> assertEquals(1, ((Map<?, ?>) snapshot.get("nested")).get("innerValue")),
        () -> assertEquals(List.of(1, 2), snapshot.get("list")),
        () -> assertNull(snapshot.get("nullValue")),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> snapshot.put("x", 1)),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> ((Map<String, Object>) snapshot.get("nested")).put("x", 1)),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> ((List<Object>) snapshot.get("list")).add(3)));
  }

  @Test
  @SuppressWarnings("unchecked")
  @DisplayName("ofCollection should produce an unmodifiable recursive snapshot")
  void ofCollectionShouldProduceUnmodifiableRecursiveSnapshot() {
    var source = new ArrayList<>();
    source.add(Map.of("key", "value"));
    source.add(new int[] {1, 2, 3});
    source.add(null);

    List<Object> snapshot = ImmutableSnapshot.ofCollection(source);

    assertAll("ofCollectionShouldProduceUnmodifiableRecursiveSnapshot",
        () -> assertEquals("value", ((Map<?, ?>) snapshot.get(0)).get("key")),
        () -> assertEquals(List.of(1, 2, 3), snapshot.get(1)),
        () -> assertNull(snapshot.get(2)),
        () -> assertThrows(UnsupportedOperationException.class, () -> snapshot.add(4)),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> ((Map<String, Object>) snapshot.get(0)).put("x", 1)),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> ((List<Object>) snapshot.get(1)).add(4)));
  }

  @Test
  @DisplayName("Snapshot should be detached from later source mutations")
  void snapshotShouldBeDetachedFromSourceMutations() {
    var inner = new HashMap<String, Object>();
    inner.put("value", 1);
    var source = new HashMap<String, Object>();
    source.put("nested", inner);
    source.put("key", "original");

    Map<String, Object> snapshot = ImmutableSnapshot.ofMap(source);

    inner.put("value", 999);
    source.put("key", "changed");

    assertAll("snapshotShouldBeDetachedFromSourceMutations",
        () -> assertEquals(1, ((Map<?, ?>) snapshot.get("nested")).get("value")),
        () -> assertEquals("original", snapshot.get("key")));
  }

  @Test
  @DisplayName("Cyclic data should throw IllegalArgumentException")
  void cyclicDataShouldThrowException() {
    var cyclic = new HashMap<String, Object>();
    cyclic.put("self", cyclic);

    assertThrows(IllegalArgumentException.class, () -> ImmutableSnapshot.ofMap(cyclic));

    var cyclicList = new ArrayList<>();
    cyclicList.add(cyclicList);
    assertThrows(IllegalArgumentException.class,
        () -> ImmutableSnapshot.ofCollection(cyclicList));
  }

  @Test
  @DisplayName("Shared (non-cyclic) references should be snapshotted independently")
  void sharedReferencesShouldBeHandled() {
    var shared = new HashMap<String, Object>();
    shared.put("v", 1);
    var source = new HashMap<String, Object>();
    source.put("first", shared);
    source.put("second", shared);

    Map<String, Object> snapshot = ImmutableSnapshot.ofMap(source);

    assertAll("sharedReferencesShouldBeHandled",
        () -> assertEquals(1, ((Map<?, ?>) snapshot.get("first")).get("v")),
        () -> assertEquals(1, ((Map<?, ?>) snapshot.get("second")).get("v")));
  }
}
