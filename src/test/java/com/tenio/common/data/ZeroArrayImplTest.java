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

import com.tenio.common.data.zero.ZeroArray;
import com.tenio.common.data.zero.ZeroType;
import com.tenio.common.data.zero.utility.ZeroUtility;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Zero Array Implementation")
class ZeroArrayImplTest {

  private ZeroArray zeroArray;

  @BeforeEach
  void initialization() {
    zeroArray = ZeroUtility.newZeroArray();
  }

  @Test
  @DisplayName("Creating a new instance should produce an empty ZERO array")
  void itShouldCreateAnEmptyZeroArray() {
    assertAll("itShouldCreateAnEmptyZeroArray",
        () -> assertEquals(0, zeroArray.size()),
        () -> assertEquals(DataType.ZERO, zeroArray.getType()),
        () -> assertEquals("ZeroArray{ }", zeroArray.toString()));
  }

  @Test
  @DisplayName("Allow adding and fetching primitive data")
  void itShouldFetchAllInsertedPrimitiveData() {
    zeroArray.addNull()
        .addBoolean(true)
        .addByte((byte) 1)
        .addShort((short) 2)
        .addInteger(3)
        .addFloat(4.5f)
        .addLong(6L)
        .addDouble(7.5)
        .addString("test");

    assertAll("itShouldFetchAllInsertedPrimitiveData",
        () -> assertTrue(zeroArray.isNull(0)),
        () -> assertFalse(zeroArray.isNull(1)),
        () -> assertTrue(zeroArray.getBoolean(1)),
        () -> assertEquals((byte) 1, zeroArray.getByte(2)),
        () -> assertEquals((short) 2, zeroArray.getShort(3)),
        () -> assertEquals(3, zeroArray.getInteger(4)),
        () -> assertEquals(4.5f, zeroArray.getFloat(5)),
        () -> assertEquals(6L, zeroArray.getLong(6)),
        () -> assertEquals(7.5, zeroArray.getDouble(7)),
        () -> assertEquals("test", zeroArray.getString(8)),
        () -> assertEquals(ZeroType.STRING, zeroArray.getZeroElement(8).getType()));
  }

  @Test
  @DisplayName("Add methods should return the same instance for chaining")
  void addMethodsShouldReturnSameInstance() {
    assertSame(zeroArray, zeroArray.addInteger(1));
    assertSame(zeroArray, zeroArray.addNull());
    assertSame(zeroArray, zeroArray.addZeroMap(ZeroUtility.newZeroMap()));
  }

  @Test
  @DisplayName("Allow adding and fetching collection data")
  void itShouldFetchAllInsertedCollectionData() {
    zeroArray.addBooleanArray(List.of(true, false))
        .addByteArray(new byte[] {1, 2, 3})
        .addShortArray(List.of((short) 1, (short) 2))
        .addIntegerArray(List.of(1, 2, 3))
        .addLongArray(List.of(10L, 20L))
        .addFloatArray(List.of(1.5f, 2.5f))
        .addDoubleArray(List.of(1.1, 2.2))
        .addStringArray(List.of("a", "b"));

    assertAll("itShouldFetchAllInsertedCollectionData",
        () -> assertEquals(List.of(true, false), zeroArray.getBooleanArray(0)),
        () -> assertEquals(3, zeroArray.getByteArray(1).length),
        () -> assertEquals(List.of((short) 1, (short) 2), zeroArray.getShortArray(2)),
        () -> assertEquals(List.of(1, 2, 3), zeroArray.getIntegerArray(3)),
        () -> assertEquals(List.of(10L, 20L), zeroArray.getLongArray(4)),
        () -> assertEquals(List.of(1.5f, 2.5f), zeroArray.getFloatArray(5)),
        () -> assertEquals(List.of(1.1, 2.2), zeroArray.getDoubleArray(6)),
        () -> assertEquals(List.of("a", "b"), zeroArray.getStringArray(7)));
  }

  @Test
  @DisplayName("Allow adding and fetching nested ZeroArray, ZeroMap and ZeroElement")
  void itShouldFetchNestedCollections() {
    var nestedArray = ZeroUtility.newZeroArray().addInteger(7);
    var nestedMap = ZeroUtility.newZeroMap().putString("inner", "value");
    zeroArray.addZeroArray(nestedArray)
        .addZeroMap(nestedMap)
        .addZeroElement(ZeroUtility.newZeroElement(ZeroType.BOOLEAN, true));

    assertAll("itShouldFetchNestedCollections",
        () -> assertSame(nestedArray, zeroArray.getZeroArray(0)),
        () -> assertSame(nestedMap, zeroArray.getZeroMap(1)),
        () -> assertTrue((boolean) zeroArray.getZeroElement(2).getData()),
        () -> assertEquals(7, (int) zeroArray.getZeroArray(0).getInteger(0)));
  }

  @Test
  @DisplayName("contains and containsValueAt should check elements correctly")
  void itShouldCheckContainedElements() {
    zeroArray.addInteger(10).addInteger(20).addNull();

    assertAll("itShouldCheckContainedElements",
        () -> assertTrue(zeroArray.contains(10)),
        () -> assertTrue(zeroArray.contains(20)),
        () -> assertTrue(zeroArray.contains(null)),
        () -> assertFalse(zeroArray.contains(30)),
        () -> assertTrue(zeroArray.containsValueAt(0)),
        () -> assertTrue(zeroArray.containsValueAt(2)),
        () -> assertFalse(zeroArray.containsValueAt(3)),
        () -> assertFalse(zeroArray.containsValueAt(-1)));
  }

  @Test
  @DisplayName("getDataForElementAt should return raw element data")
  void itShouldReturnRawElementData() {
    zeroArray.addInteger(10).addNull();

    assertAll("itShouldReturnRawElementData",
        () -> assertEquals(10, zeroArray.getDataForElementAt(0)),
        () -> assertNull(zeroArray.getDataForElementAt(1)));
  }

  @Test
  @DisplayName("setByte, setShort and setLong should replace elements in place")
  void itShouldReplaceElementsInPlace() {
    zeroArray.addInteger(1).addInteger(2).addInteger(3);

    assertAll("itShouldReplaceElementsInPlace",
        () -> assertSame(zeroArray, zeroArray.setByte(0, (byte) 9)),
        () -> assertSame(zeroArray, zeroArray.setShort(1, (short) 8)),
        () -> assertSame(zeroArray, zeroArray.setLong(2, 7L)),
        () -> assertEquals((byte) 9, zeroArray.getByte(0)),
        () -> assertEquals((short) 8, zeroArray.getShort(1)),
        () -> assertEquals(7L, zeroArray.getLong(2)),
        () -> assertEquals(ZeroType.BYTE, zeroArray.getZeroElement(0).getType()));
  }

  @Test
  @DisplayName("Removing an element should shrink the array")
  void itShouldRemoveElements() {
    zeroArray.addInteger(1).addInteger(2).addInteger(3);

    zeroArray.removeElementAt(1);

    assertAll("itShouldRemoveElements",
        () -> assertEquals(2, zeroArray.size()),
        () -> assertEquals(1, zeroArray.getInteger(0)),
        () -> assertEquals(3, zeroArray.getInteger(1)),
        () -> assertFalse(zeroArray.contains(2)));
  }

  @Test
  @DisplayName("Iterator should be unmodifiable")
  void iteratorShouldBeUnmodifiable() {
    zeroArray.addInteger(1);

    assertThrows(UnsupportedOperationException.class, () -> {
      var iterator = zeroArray.iterator();
      iterator.next();
      iterator.remove();
    });
  }

  @Test
  @DisplayName("Readonly array should be a detached snapshot immune to later mutations")
  void getReadonlyZeroArrayShouldReturnDetachedSnapshot() {
    zeroArray.addInteger(1)
        .addIntegerArray(List.of(1, 2))
        .addByteArray(new byte[] {1, 2})
        .addZeroMap(ZeroUtility.newZeroMap().putString("inner", "v"));

    var snapshot = zeroArray.getReadonlyZeroArray();

    assertAll("getReadonlyZeroArrayShouldReturnDetachedSnapshot",
        () -> assertEquals(1, (int) snapshot.getInteger(0)),
        () -> assertEquals(DataType.ZERO, snapshot.getType()),
        () -> assertEquals(List.of(1, 2), snapshot.getIntegerArray(1)),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> snapshot.getIntegerArray(1).add(3)),
        () -> assertTrue(snapshot.contains(1)),
        () -> assertTrue(snapshot.containsValueAt(0)));

    var byteArray = snapshot.getByteArray(2);
    byteArray[0] = 99;
    assertEquals((byte) 1, snapshot.getByteArray(2)[0]);

    var nested = snapshot.getZeroMap(3);
    nested.putInteger("inner", 999);
    assertEquals("v", snapshot.getZeroMap(3).getString("inner"));

    zeroArray.setByte(0, (byte) 100);
    assertEquals(1, (int) snapshot.getInteger(0));
  }

  @Test
  @DisplayName("Binaries should round-trip through Zero serialization")
  void toBinariesShouldRoundTrip() {
    zeroArray.addInteger(100)
        .addString("tenio")
        .addBoolean(true)
        .addStringArray(List.of("a", "b"));

    var decoded = ZeroUtility.binariesToArray(zeroArray.toBinaries());

    assertAll("toBinariesShouldRoundTrip",
        () -> assertEquals(100, (int) decoded.getInteger(0)),
        () -> assertEquals("tenio", decoded.getString(1)),
        () -> assertTrue(decoded.getBoolean(2)),
        () -> assertEquals(List.of("a", "b"), decoded.getStringArray(3)),
        () -> assertEquals(DataType.ZERO, decoded.getType()));
  }
}
