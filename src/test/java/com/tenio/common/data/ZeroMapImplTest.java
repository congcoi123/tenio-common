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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tenio.common.data.zero.ZeroMap;
import com.tenio.common.data.zero.ZeroType;
import com.tenio.common.data.zero.utility.ZeroUtility;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Zero Map Implementation")
class ZeroMapImplTest {

  private ZeroMap zeroMap;

  @BeforeEach
  void initialization() {
    zeroMap = ZeroUtility.newZeroMap();
  }

  @Test
  @DisplayName("Creating a new instance should produce an empty ZERO map")
  void itShouldCreateAnEmptyZeroMap() {
    assertAll("itShouldCreateAnEmptyZeroMap",
        () -> assertEquals(0, zeroMap.size()),
        () -> assertEquals(DataType.ZERO, zeroMap.getType()),
        () -> assertEquals("ZeroMap{ }", zeroMap.toString()));
  }

  @Test
  @DisplayName("Allow adding and fetching primitive data")
  void itShouldFetchAllInsertedPrimitiveData() {
    zeroMap.putNull("null")
        .putBoolean("boolean", true)
        .putByte("byte", (byte) 1)
        .putShort("short", (short) 2)
        .putInteger("integer", 3)
        .putFloat("float", 4.5f)
        .putLong("long", 6L)
        .putDouble("double", 7.5)
        .putString("string", "test");

    assertAll("itShouldFetchAllInsertedPrimitiveData",
        () -> assertTrue(zeroMap.isNull("null")),
        () -> assertFalse(zeroMap.isNull("boolean")),
        () -> assertFalse(zeroMap.isNull("missing")),
        () -> assertTrue(zeroMap.getBoolean("boolean")),
        () -> assertEquals((byte) 1, zeroMap.getByte("byte")),
        () -> assertEquals((short) 2, zeroMap.getShort("short")),
        () -> assertEquals(3, zeroMap.getInteger("integer")),
        () -> assertEquals(4.5f, zeroMap.getFloat("float")),
        () -> assertEquals(6L, zeroMap.getLong("long")),
        () -> assertEquals(7.5, zeroMap.getDouble("double")),
        () -> assertEquals("test", zeroMap.getString("string")),
        () -> assertEquals(ZeroType.STRING, zeroMap.getZeroElement("string").getType()));
  }

  @Test
  @DisplayName("Fetching a missing key should return null")
  void itShouldReturnNullForMissingKeys() {
    assertAll("itShouldReturnNullForMissingKeys",
        () -> assertNull(zeroMap.getString("missing")),
        () -> assertNull(zeroMap.getZeroElement("missing")),
        () -> assertNull(zeroMap.getZeroMap("missing")),
        () -> assertNull(zeroMap.getZeroArray("missing")),
        () -> assertNull(zeroMap.getDataCollection("missing")),
        () -> assertFalse(zeroMap.containsKey("missing")));
  }

  @Test
  @DisplayName("Allow adding and fetching collection data")
  void itShouldFetchAllInsertedCollectionData() {
    zeroMap.putBooleanArray("booleans", List.of(true, false))
        .putByteArray("bytes", new byte[] {1, 2, 3})
        .putShortArray("shorts", List.of((short) 1, (short) 2))
        .putIntegerArray("integers", List.of(1, 2, 3))
        .putLongArray("longs", List.of(10L, 20L))
        .putFloatArray("floats", List.of(1.5f, 2.5f))
        .putDoubleArray("doubles", List.of(1.1, 2.2))
        .putStringArray("strings", List.of("a", "b"));

    assertAll("itShouldFetchAllInsertedCollectionData",
        () -> assertEquals(List.of(true, false), zeroMap.getBooleanArray("booleans")),
        () -> assertEquals(3, zeroMap.getByteArray("bytes").length),
        () -> assertEquals(List.of((short) 1, (short) 2), zeroMap.getShortArray("shorts")),
        () -> assertEquals(List.of(1, 2, 3), zeroMap.getIntegerArray("integers")),
        () -> assertEquals(List.of(10L, 20L), zeroMap.getLongArray("longs")),
        () -> assertEquals(List.of(1.5f, 2.5f), zeroMap.getFloatArray("floats")),
        () -> assertEquals(List.of(1.1, 2.2), zeroMap.getDoubleArray("doubles")),
        () -> assertEquals(List.of("a", "b"), zeroMap.getStringArray("strings")));
  }

  @Test
  @DisplayName("Allow adding and fetching nested ZeroArray and ZeroMap data")
  void itShouldFetchNestedCollections() {
    var nestedArray = ZeroUtility.newZeroArray().addInteger(7);
    var nestedMap = ZeroUtility.newZeroMap().putString("inner", "value");
    zeroMap.putZeroArray("array", nestedArray)
        .putZeroMap("map", nestedMap)
        .putZeroElement("element",
            ZeroUtility.newZeroElement(ZeroType.BOOLEAN, true));

    assertAll("itShouldFetchNestedCollections",
        () -> assertSame(nestedArray, zeroMap.getDataCollection("array")),
        () -> assertSame(nestedMap, zeroMap.getDataCollection("map")),
        () -> assertSame(nestedArray, zeroMap.getZeroArray("array")),
        () -> assertSame(nestedMap, zeroMap.getZeroMap("map")),
        () -> assertTrue((boolean) zeroMap.getZeroElement("element").getData()));
  }

  @Test
  @DisplayName("Put methods should return the same instance for chaining")
  void putMethodsShouldReturnSameInstance() {
    assertSame(zeroMap, zeroMap.putString("key", "value"));
    assertSame(zeroMap, zeroMap.putNull("null"));
    assertSame(zeroMap,
        zeroMap.putZeroMap("map", ZeroUtility.newZeroMap()));
  }

  @Test
  @DisplayName("Key sets and iterators should be unmodifiable")
  void keySetsAndIteratorsShouldBeUnmodifiable() {
    zeroMap.putInteger("a", 1);

    assertAll("keySetsAndIteratorsShouldBeUnmodifiable",
        () -> assertThrows(UnsupportedOperationException.class,
            () -> zeroMap.getKeys().add("b")),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> zeroMap.getReadonlyKeys().add("b")),
        () -> assertThrows(UnsupportedOperationException.class, () -> {
          var iterator = zeroMap.iterator();
          iterator.next();
          iterator.remove();
        }));
  }

  @Test
  @DisplayName("Removing an element should report whether a value was removed")
  void itShouldReportRemovedElements() {
    zeroMap.putInteger("a", 1);

    assertAll("itShouldReportRemovedElements",
        () -> assertTrue(zeroMap.removeElement("a")),
        () -> assertFalse(zeroMap.removeElement("a")),
        () -> assertFalse(zeroMap.containsKey("a")),
        () -> assertEquals(0, zeroMap.size()));
  }

  @Test
  @DisplayName("Readonly map should be a detached snapshot immune to later mutations")
  void getReadonlyZeroMapShouldReturnDetachedSnapshot() {
    zeroMap.putInteger("a", 1)
        .putIntegerArray("ints", List.of(1, 2))
        .putByteArray("bytes", new byte[] {1, 2})
        .putZeroMap("nested", ZeroUtility.newZeroMap().putString("inner", "v"));

    var snapshot = zeroMap.getReadonlyZeroMap();

    assertAll("getReadonlyZeroMapShouldReturnDetachedSnapshot",
        () -> assertEquals(1, (int) snapshot.getInteger("a")),
        () -> assertEquals(DataType.ZERO, snapshot.getType()),
        () -> assertEquals(List.of(1, 2), snapshot.getIntegerArray("ints")),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> snapshot.getIntegerArray("ints").add(3)),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> snapshot.getKeys().add("b")));

    var byteArray = snapshot.getByteArray("bytes");
    byteArray[0] = 99;
    assertEquals((byte) 1, snapshot.getByteArray("bytes")[0]);

    var nested = snapshot.getZeroMap("nested");
    nested.putInteger("inner", 999);
    assertEquals("v", snapshot.getZeroMap("nested").getString("inner"));

    zeroMap.putInteger("a", 100);
    assertEquals(1, (int) snapshot.getInteger("a"));
  }

  @Test
  @DisplayName("Binaries should round-trip through Zero serialization")
  void toBinariesShouldRoundTrip() {
    zeroMap.putInteger("id", 100)
        .putString("name", "tenio")
        .putBoolean("active", true)
        .putStringArray("tags", List.of("a", "b"));

    var decoded = ZeroUtility.binariesToMap(zeroMap.toBinaries());

    assertAll("toBinariesShouldRoundTrip",
        () -> assertEquals(100, (int) decoded.getInteger("id")),
        () -> assertEquals("tenio", decoded.getString("name")),
        () -> assertTrue(decoded.getBoolean("active")),
        () -> assertEquals(List.of("a", "b"), decoded.getStringArray("tags")),
        () -> assertEquals(DataType.ZERO, decoded.getType()));
  }
}
