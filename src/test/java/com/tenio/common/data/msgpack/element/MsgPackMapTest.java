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

package com.tenio.common.data.msgpack.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tenio.common.data.DataType;
import com.tenio.common.data.msgpack.MsgPackUtility;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For MsgPack Map")
class MsgPackMapTest {

  private MsgPackMap msgPackMap;

  @BeforeEach
  void initialization() {
    msgPackMap = MsgPackMap.newInstance();
  }

  @Test
  @DisplayName("Creating a new instance should produce an empty MSG_PACK map")
  void itShouldCreateAnEmptyMsgPackMap() {
    assertAll("itShouldCreateAnEmptyMsgPackMap",
        () -> assertTrue(msgPackMap.isEmpty()),
        () -> assertEquals(DataType.MSG_PACK, msgPackMap.getType()));
  }

  @Test
  @DisplayName("Allow adding and fetching primitive data")
  void itShouldFetchAllInsertedPrimitiveData() {
    msgPackMap.putNull("null")
        .putBoolean("boolean", true)
        .putByte("byte", (byte) 1)
        .putShort("short", (short) 2)
        .putInteger("integer", 3)
        .putFloat("float", 4.5f)
        .putLong("long", 6L)
        .putDouble("double", 7.5)
        .putString("string", "test");

    assertAll("itShouldFetchAllInsertedPrimitiveData",
        () -> assertNull(msgPackMap.get("null")),
        () -> assertTrue(msgPackMap.isNull("null")),
        () -> assertTrue(msgPackMap.getBoolean("boolean")),
        () -> assertEquals((byte) 1, msgPackMap.getByte("byte")),
        () -> assertEquals((short) 2, msgPackMap.getShort("short")),
        () -> assertEquals(3, msgPackMap.getInteger("integer")),
        () -> assertEquals(4.5f, msgPackMap.getFloat("float")),
        () -> assertEquals(6L, msgPackMap.getLong("long")),
        () -> assertEquals(7.5, msgPackMap.getDouble("double")),
        () -> assertEquals("test", msgPackMap.getString("string")),
        () -> assertTrue(msgPackMap.isNull("missing")));
  }

  @Test
  @DisplayName("Fetching a missing key should return null-based results")
  void itShouldReturnNullForMissingKeys() {
    assertAll("itShouldReturnNullForMissingKeys",
        () -> assertNull(msgPackMap.getString("missing")),
        () -> assertFalse(msgPackMap.contains("missing")),
        () -> assertNull(msgPackMap.getMsgPackMap("missing")));
  }

  @Test
  @DisplayName("Allow adding and fetching array data")
  void itShouldFetchAllInsertedArrayData() {
    msgPackMap.putBooleanArray("booleans", new boolean[] {true, false})
        .putByteArray("bytes", new byte[] {1, 2, 3})
        .putShortArray("shorts", new short[] {1, 2})
        .putIntegerArray("integers", new int[] {1, 2, 3})
        .putFloatArray("floats", new float[] {1.5f, 2.5f})
        .putLongArray("longs", new long[] {10L, 20L})
        .putDoubleArray("doubles", new double[] {1.1, 2.2})
        .putStringArray("strings", new String[] {"a", "b"});

    assertAll("itShouldFetchAllInsertedArrayData",
        () -> assertArrayEquals(new boolean[] {true, false},
            msgPackMap.getBooleanArray("booleans")),
        () -> assertArrayEquals(new byte[] {1, 2, 3}, msgPackMap.getByteArray("bytes")),
        () -> assertArrayEquals(new short[] {1, 2}, msgPackMap.getShortArray("shorts")),
        () -> assertArrayEquals(new int[] {1, 2, 3}, msgPackMap.getIntegerArray("integers")),
        () -> assertArrayEquals(new float[] {1.5f, 2.5f},
            msgPackMap.getFloatArray("floats"), 0.0001f),
        () -> assertArrayEquals(new long[] {10L, 20L}, msgPackMap.getLongArray("longs")),
        () -> assertArrayEquals(new double[] {1.1, 2.2}, msgPackMap.getDoubleArray("doubles"),
            0.0001),
        () -> assertArrayEquals(new String[] {"a", "b"}, msgPackMap.getStringArray("strings")));
  }

  @Test
  @DisplayName("Array getters should coerce between compatible array types")
  void itShouldCoerceCompatibleArrayTypes() {
    msgPackMap.put("intBytes", new int[] {1, 2, 300});
    msgPackMap.put("intShorts", new int[] {1, 2});
    msgPackMap.put("intLongs", new int[] {1, 2});
    msgPackMap.put("doubleFloats", new double[] {1.5, 2.5});
    msgPackMap.put("floatDoubles", new float[] {1.5f, 2.5f});

    assertAll("itShouldCoerceCompatibleArrayTypes",
        () -> assertArrayEquals(new byte[] {1, 2, 44}, msgPackMap.getByteArray("intBytes")),
        () -> assertArrayEquals(new short[] {1, 2}, msgPackMap.getShortArray("intShorts")),
        () -> assertArrayEquals(new long[] {1L, 2L}, msgPackMap.getLongArray("intLongs")),
        () -> assertArrayEquals(new float[] {1.5f, 2.5f},
            msgPackMap.getFloatArray("doubleFloats"), 0.0001f),
        () -> assertArrayEquals(new double[] {1.5, 2.5}, msgPackMap.getDoubleArray("floatDoubles"),
            0.0001));
  }

  @Test
  @DisplayName("Allow adding and fetching nested MsgPackMap data")
  void itShouldFetchNestedMsgPackMap() {
    var nested = MsgPackMap.newInstance().putInteger("inner", 42);
    msgPackMap.putMsgPackMap("nested", nested);

    assertAll("itShouldFetchNestedMsgPackMap",
        () -> assertSame(nested, msgPackMap.getMsgPackMap("nested")),
        () -> assertEquals(42, msgPackMap.getMsgPackMap("nested").getInteger("inner")));
  }

  @Test
  @DisplayName("Put methods should return the same instance for chaining")
  void putMethodsShouldReturnSameInstance() {
    assertSame(msgPackMap, msgPackMap.putString("key", "value"));
    assertSame(msgPackMap, msgPackMap.putNull("null"));
    assertSame(msgPackMap, msgPackMap.putMsgPackMap("map", MsgPackMap.newInstance()));
  }

  @Test
  @SuppressWarnings("unchecked")
  @DisplayName("Readonly map should be an unmodifiable detached snapshot")
  void getReadonlyMapShouldReturnDetachedUnmodifiableSnapshot() {
    var nested = MsgPackMap.newInstance().putInteger("inner", 1);
    msgPackMap.putString("key", "value")
        .putByteArray("bytes", new byte[] {1, 2})
        .putIntegerArray("integers", new int[] {1, 2})
        .putMsgPackMap("nested", nested);

    Map<String, Object> snapshot = msgPackMap.getReadonlyMap();

    assertAll("getReadonlyMapShouldReturnDetachedUnmodifiableSnapshot",
        () -> assertEquals("value", snapshot.get("key")),
        () -> assertEquals(List.of(1, 2), snapshot.get("integers")),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> snapshot.put("new", "value")),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> ((List<Object>) snapshot.get("integers")).add(3)),
        () -> assertThrows(UnsupportedOperationException.class,
            () -> ((Map<String, Object>) snapshot.get("nested")).put("x", 1)));

    msgPackMap.putString("key", "changed");
    assertAll("snapshotShouldNotReflectLaterMutations",
        () -> assertEquals("value", snapshot.get("key")),
        () -> assertEquals(List.of(1, 2), snapshot.get("integers")));
  }

  @Test
  @DisplayName("Binaries should round-trip through MsgPack serialization")
  void toBinariesShouldRoundTrip() {
    msgPackMap.putInteger("id", 100)
        .putString("name", "tenio")
        .putBoolean("active", true)
        .putStringArray("tags", new String[] {"a", "b"});

    var decoded = MsgPackUtility.deserialize(msgPackMap.toBinaries());

    assertAll("toBinariesShouldRoundTrip",
        () -> assertEquals(100, decoded.getInteger("id")),
        () -> assertEquals("tenio", decoded.getString("name")),
        () -> assertTrue(decoded.getBoolean("active")),
        () -> assertEquals(DataType.MSG_PACK, decoded.getType()));
  }
}
