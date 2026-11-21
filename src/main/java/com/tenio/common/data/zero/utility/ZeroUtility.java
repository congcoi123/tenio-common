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

package com.tenio.common.data.zero.utility;

import com.tenio.common.data.DataCollection;
import com.tenio.common.data.zero.ZeroArray;
import com.tenio.common.data.zero.ZeroElement;
import com.tenio.common.data.zero.ZeroMap;
import com.tenio.common.data.zero.ZeroType;
import com.tenio.common.data.zero.implement.ZeroArrayImpl;
import com.tenio.common.data.zero.implement.ZeroElementImpl;
import com.tenio.common.data.zero.implement.ZeroMapImpl;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * This class provides all necessary methods to work with the self-definition data elements.
 */
public final class ZeroUtility {

  private static final int BUFFER_CHUNK_BYTES = 512;
  private static final int ENCODE_SHORT_BYTES = Byte.BYTES + Short.BYTES;
  private static final int ENCODE_INTEGER_BYTES = Byte.BYTES + Integer.BYTES;
  private static final int ENCODE_LONG_BYTES = Byte.BYTES + Long.BYTES;
  private static final int ENCODE_FLOAT_BYTES = Byte.BYTES + Float.BYTES;
  private static final int ENCODE_DOUBLE_BYTES = Byte.BYTES + Double.BYTES;
  private static final int ENCODE_HEADER_STRING_BYTES = Byte.BYTES + Short.BYTES;
  private static final int ENCODE_HEADER_BOOLEAN_ARRAY_BYTES = Byte.BYTES + Short.BYTES;
  private static final int ENCODE_HEADER_BYTE_ARRAY_BYTES = Byte.BYTES + Integer.BYTES;
  private static final int ENCODE_HEADER_NUMERIC_ARRAY_BYTES = Byte.BYTES + Short.BYTES;
  private static final int ENCODE_HEADER_STRING_ARRAY_BYTES = Byte.BYTES + Short.BYTES;
  private static final int MAX_MESSAGE_BYTES = 16 * 1024 * 1024;
  private static final int MAX_BINARY_BYTES = 8 * 1024 * 1024;
  private static final int MAX_NESTING_DEPTH = 64;
  private static final int MAX_SHORT_LENGTH = Short.MAX_VALUE;

  private ZeroUtility() {
    throw new UnsupportedOperationException("This class does not support to create an instance");
  }

  /**
   * Creates a new instance of {@link ZeroElement} class.
   *
   * @param type the type of element in {@link ZeroType}
   * @param data the data of element in variety of types
   * @return new instance of zero element
   * @see Boolean
   * @see Byte
   * @see Short
   * @see Integer
   * @see Long
   * @see Float
   * @see Double
   * @see String
   * @see Collection
   * @see ZeroArray
   * @see ZeroMap
   */
  public static ZeroElement newZeroElement(ZeroType type, Object data) {
    return new ZeroElementImpl(type, data);
  }

  /**
   * Creates a new instance of {@link ZeroArray} class.
   *
   * @return new instance of zero array
   */
  public static ZeroArray newZeroArray() {
    return new ZeroArrayImpl();
  }

  /**
   * Creates a new instance of {@link ZeroMap} class.
   *
   * @return new instance of zero map
   */
  public static ZeroMap newZeroMap() {
    return new ZeroMapImpl();
  }

  /**
   * Deserializes a stream of bytes to a zero collection.
   *
   * @param binaries the stream of bytes
   * @return a new zero collection instance
   */
  public static DataCollection binariesToCollection(byte[] binaries) {
    validatePayload(binaries);
    return switch (ZeroType.getByValue(binaries[0])) {
      case ZERO_MAP -> binariesToMap(binaries);
      case ZERO_ARRAY -> binariesToArray(binaries);
      default -> throw new UnsupportedOperationException(
          String.format("Unsupported value: %s", ZeroType.getByValue(binaries[0])));
    };
  }

  /**
   * Deserializes a stream of bytes to a zero array.
   *
   * @param binaries the stream of bytes
   * @return a new zero array instance
   */
  public static ZeroArray binariesToArray(byte[] binaries) {
    validatePayload(binaries);
    try {
      var buffer = ByteBuffer.wrap(binaries).order(ByteOrder.BIG_ENDIAN);
      var result = decodeZeroArray(buffer, 0);
      requireFullyConsumed(buffer);
      return result;
    } catch (IllegalArgumentException exception) {
      throw exception;
    } catch (RuntimeException exception) {
      throw new IllegalArgumentException("Malformed ZeroArray payload", exception);
    }
  }

  /**
   * Deserializes a stream of bytes to a zero map.
   *
   * @param binaries the stream of bytes
   * @return a new zero map instance
   */
  public static ZeroMap binariesToMap(byte[] binaries) {
    validatePayload(binaries);
    try {
      var buffer = ByteBuffer.wrap(binaries).order(ByteOrder.BIG_ENDIAN);
      var result = decodeZeroMap(buffer, 0);
      requireFullyConsumed(buffer);
      return result;
    } catch (IllegalArgumentException exception) {
      throw exception;
    } catch (RuntimeException exception) {
      throw new IllegalArgumentException("Malformed ZeroMap payload", exception);
    }
  }

  /**
   * Serialize a map to a stream of bytes.
   *
   * @param map the map
   * @return the stream of bytes converted from the map
   */
  public static byte[] mapToBinaries(ZeroMap map) {
    if (map == null) {
      throw new IllegalArgumentException("ZeroMap must not be null");
    }
    var buffer = ByteBuffer.allocate(BUFFER_CHUNK_BYTES);
    buffer = encodeZeroMap(buffer, map, 0);
    return toByteArray(buffer);
  }

  private static ByteBuffer encodeZeroMap(ByteBuffer buffer, ZeroMap map, int depth) {
    checkDepth(depth);
    checkShortLength(map.size(), "map");
    buffer = ensureCapacity(buffer, Byte.BYTES + Short.BYTES);
    buffer.put((byte) ZeroType.ZERO_MAP.getValue());
    buffer.putShort((short) map.size());

    var keys = map.getKeys();
    for (var key : keys) {
      var zeroElement = map.getZeroElement(key);
      buffer = encodeZeroMapKey(buffer, key);
      buffer = encodeElement(buffer, zeroElement.getType(), zeroElement.getData(), depth);
    }

    return buffer;
  }

  /**
   * Serializes an array to a stream of bytes.
   *
   * @param array the array
   * @return the stream of bytes converted from the array
   */
  public static byte[] arrayToBinaries(ZeroArray array) {
    if (array == null) {
      throw new IllegalArgumentException("ZeroArray must not be null");
    }
    var buffer = ByteBuffer.allocate(BUFFER_CHUNK_BYTES);
    buffer = encodeZeroArray(buffer, array, 0);
    return toByteArray(buffer);
  }

  private static ByteBuffer encodeZeroArray(ByteBuffer buffer, ZeroArray array, int depth) {
    checkDepth(depth);
    checkShortLength(array.size(), "array");
    buffer = ensureCapacity(buffer, Byte.BYTES + Short.BYTES);
    buffer.put((byte) ZeroType.ZERO_ARRAY.getValue());
    buffer.putShort((short) array.size());

    for (var iterator = array.iterator(); iterator.hasNext(); ) {
      var zeroElement = iterator.next();
      buffer = encodeElement(buffer, zeroElement.getType(), zeroElement.getData(), depth);
    }

    return buffer;
  }

  private static byte[] toByteArray(ByteBuffer buffer) {
    var position = buffer.position();
    var result = new byte[position];
    buffer.flip();
    buffer.get(result, 0, position);
    return result;
  }

  private static ZeroElement decodeElement(ByteBuffer buffer, int depth) throws RuntimeException {
    checkDepth(depth);
    requireRemaining(buffer, Byte.BYTES, "element type");
    var headerByte = buffer.get();
    var type = ZeroType.getByValue(headerByte);
    if (type == null) {
      return null;
    }

    return switch (type) {
      case NULL -> decodeNull();
      case BOOLEAN -> decodeBoolean(buffer);
      case BYTE -> decodeByte(buffer);
      case SHORT -> decodeShort(buffer);
      case INTEGER -> decodeInteger(buffer);
      case FLOAT -> decodeFloat(buffer);
      case LONG -> decodeLong(buffer);
      case DOUBLE -> decodeDouble(buffer);
      case STRING -> decodeString(buffer);
      case BOOLEAN_ARRAY -> decodeBooleanArray(buffer);
      case BYTE_ARRAY -> decodeByteArray(buffer);
      case SHORT_ARRAY -> decodeShortArray(buffer);
      case INTEGER_ARRAY -> decodeIntegerArray(buffer);
      case FLOAT_ARRAY -> decodeFloatArray(buffer);
      case LONG_ARRAY -> decodeLongArray(buffer);
      case DOUBLE_ARRAY -> decodeDoubleArray(buffer);
      case STRING_ARRAY -> decodeStringArray(buffer);
      case ZERO_ARRAY -> {
        buffer.position(buffer.position() - Byte.BYTES);
        yield newZeroElement(ZeroType.ZERO_ARRAY, decodeZeroArray(buffer, depth + 1));
      }
      case ZERO_MAP -> {
        buffer.position(buffer.position() - Byte.BYTES);
        yield newZeroElement(ZeroType.ZERO_MAP, decodeZeroMap(buffer, depth + 1));
      }
    };
  }

  @SuppressWarnings("unchecked")
  private static ByteBuffer encodeElement(
      ByteBuffer buffer, ZeroType type, Object data, int depth) {
    switch (type) {
      case NULL -> buffer = encodeNull(buffer);
      case BOOLEAN -> buffer = encodeBoolean(buffer, (Boolean) data);
      case BYTE -> buffer = encodeByte(buffer, (Byte) data);
      case SHORT -> buffer = encodeShort(buffer, (Short) data);
      case INTEGER -> buffer = encodeInteger(buffer, (Integer) data);
      case LONG -> buffer = encodeLong(buffer, (Long) data);
      case FLOAT -> buffer = encodeFloat(buffer, (Float) data);
      case DOUBLE -> buffer = encodeDouble(buffer, (Double) data);
      case STRING -> buffer = encodeString(buffer, (String) data);
      case BOOLEAN_ARRAY -> buffer = encodeBooleanArray(buffer, (Collection<Boolean>) data);
      case BYTE_ARRAY -> buffer = encodeByteArray(buffer, (byte[]) data);
      case SHORT_ARRAY -> buffer = encodeShortArray(buffer, (Collection<Short>) data);
      case INTEGER_ARRAY -> buffer = encodeIntegerArray(buffer, (Collection<Integer>) data);
      case LONG_ARRAY -> buffer = encodeLongArray(buffer, (Collection<Long>) data);
      case FLOAT_ARRAY -> buffer = encodeFloatArray(buffer, (Collection<Float>) data);
      case DOUBLE_ARRAY -> buffer = encodeDoubleArray(buffer, (Collection<Double>) data);
      case STRING_ARRAY -> buffer = encodeStringArray(buffer, (Collection<String>) data);
      case ZERO_ARRAY -> buffer = encodeZeroArray(buffer, (ZeroArray) data, depth + 1);
      case ZERO_MAP -> buffer = encodeZeroMap(buffer, (ZeroMap) data, depth + 1);
    }

    return buffer;
  }

  private static ZeroElement decodeNull() {
    return newZeroElement(ZeroType.NULL, null);
  }

  private static ZeroElement decodeBoolean(ByteBuffer buffer) {
    var bool = buffer.get();
    Boolean data;

    if (bool == 0) {
      data = Boolean.FALSE;
    } else {
      if (bool != 1) {
        throw new IllegalStateException(
            String.format("Expected value of 0 or 1, but found: %d", bool));
      }

      data = Boolean.TRUE;
    }

    return newZeroElement(ZeroType.BOOLEAN, data);
  }

  private static ZeroElement decodeByte(ByteBuffer buffer) {
    var data = buffer.get();
    return newZeroElement(ZeroType.BYTE, data);
  }

  private static ZeroElement decodeShort(ByteBuffer buffer) {
    var data = buffer.getShort();
    return newZeroElement(ZeroType.SHORT, data);
  }

  private static ZeroElement decodeInteger(ByteBuffer buffer) {
    var data = buffer.getInt();
    return newZeroElement(ZeroType.INTEGER, data);
  }

  private static ZeroElement decodeLong(ByteBuffer buffer) {
    var data = buffer.getLong();
    return newZeroElement(ZeroType.LONG, data);
  }

  private static ZeroElement decodeFloat(ByteBuffer buffer) {
    var data = buffer.getFloat();
    return newZeroElement(ZeroType.FLOAT, data);
  }

  private static ZeroElement decodeDouble(ByteBuffer buffer) {
    var data = buffer.getDouble();
    return newZeroElement(ZeroType.DOUBLE, data);
  }

  private static ZeroElement decodeString(ByteBuffer buffer) {
    requireRemaining(buffer, Short.BYTES, "string length");
    var strLen = buffer.getShort();

    if (strLen < 0) {
      throw new IllegalStateException(
          String.format("The length of string is incorrect: %d", strLen));
    }

    requireRemaining(buffer, strLen, "string");
    var strData = new byte[strLen];
    buffer.get(strData, 0, strLen);
    var data = new String(strData, StandardCharsets.UTF_8);

    return newZeroElement(ZeroType.STRING, data);
  }

  private static ZeroElement decodeBooleanArray(ByteBuffer buffer) {
    var collectionSize = getCollectionSize(buffer);
    requireRemaining(buffer, collectionSize, "boolean array");
    var data = new ArrayList<Boolean>(collectionSize);

    for (int i = 0; i < collectionSize; ++i) {
      var bool = buffer.get();
      if (bool == 0) {
        data.add(false);
      } else {
        if (bool != 1) {
          throw new IllegalStateException(
              String.format("Expected value of 0 or 1, but found: %d", bool));
        }

        data.add(true);
      }
    }

    return newZeroElement(ZeroType.BOOLEAN_ARRAY, data);
  }

  private static ZeroElement decodeByteArray(ByteBuffer buffer) {
    requireRemaining(buffer, Integer.BYTES, "byte array length");
    var arraySize = buffer.getInt();
    if (arraySize < 0 || arraySize > MAX_BINARY_BYTES) {
      throw new IllegalArgumentException("Invalid byte array size: " + arraySize);
    }
    requireRemaining(buffer, arraySize, "byte array");
    var byteData = new byte[arraySize];
    buffer.get(byteData, 0, arraySize);

    return newZeroElement(ZeroType.BYTE_ARRAY, byteData);
  }

  private static ZeroElement decodeShortArray(ByteBuffer buffer) {
    var collectionSize = getCollectionSize(buffer);
    requireElements(buffer, collectionSize, Short.BYTES, "short array");
    var data = new ArrayList<Short>(collectionSize);

    for (int i = 0; i < collectionSize; ++i) {
      var shortValue = buffer.getShort();
      data.add(shortValue);
    }

    return newZeroElement(ZeroType.SHORT_ARRAY, data);
  }

  private static ZeroElement decodeIntegerArray(ByteBuffer buffer) {
    var collectionSize = getCollectionSize(buffer);
    requireElements(buffer, collectionSize, Integer.BYTES, "integer array");
    var data = new ArrayList<Integer>(collectionSize);

    for (int i = 0; i < collectionSize; ++i) {
      var intValue = buffer.getInt();
      data.add(intValue);
    }

    return newZeroElement(ZeroType.INTEGER_ARRAY, data);
  }

  private static ZeroElement decodeLongArray(ByteBuffer buffer) {
    var collectionSize = getCollectionSize(buffer);
    requireElements(buffer, collectionSize, Long.BYTES, "long array");
    var data = new ArrayList<Long>(collectionSize);

    for (int i = 0; i < collectionSize; ++i) {
      var longValue = buffer.getLong();
      data.add(longValue);
    }

    return newZeroElement(ZeroType.LONG_ARRAY, data);
  }

  private static ZeroElement decodeFloatArray(ByteBuffer buffer) {
    var collectionSize = getCollectionSize(buffer);
    requireElements(buffer, collectionSize, Float.BYTES, "float array");
    var data = new ArrayList<Float>(collectionSize);

    for (int i = 0; i < collectionSize; ++i) {
      var floatValue = buffer.getFloat();
      data.add(floatValue);
    }

    return newZeroElement(ZeroType.FLOAT_ARRAY, data);
  }

  private static ZeroElement decodeDoubleArray(ByteBuffer buffer) {
    var collectionSize = getCollectionSize(buffer);
    requireElements(buffer, collectionSize, Double.BYTES, "double array");
    var data = new ArrayList<Double>(collectionSize);

    for (int i = 0; i < collectionSize; ++i) {
      var doubleValue = buffer.getDouble();
      data.add(doubleValue);
    }

    return newZeroElement(ZeroType.DOUBLE_ARRAY, data);
  }

  private static ZeroElement decodeStringArray(ByteBuffer buffer) {
    var collectionSize = getCollectionSize(buffer);
    var data = new ArrayList<String>(collectionSize);

    for (int i = 0; i < collectionSize; ++i) {
      requireRemaining(buffer, Short.BYTES, "string array item length");
      var strLen = buffer.getShort();
      if (strLen < 0) {
        throw new IllegalStateException(
            String.format("The length of string is incorrect: %d", strLen));
      }

      requireRemaining(buffer, strLen, "string array item");
      var strData = new byte[strLen];
      buffer.get(strData, 0, strLen);
      var stringValue = new String(strData, StandardCharsets.UTF_8);
      data.add(stringValue);
    }

    return newZeroElement(ZeroType.STRING_ARRAY, data);
  }

  private static ZeroArray decodeZeroArray(ByteBuffer buffer, int depth) {
    checkDepth(depth);
    requireRemaining(buffer, Byte.BYTES + Short.BYTES, "ZeroArray header");
    var zeroArray = newZeroArray();
    var headerByte = buffer.get();

    if (ZeroType.getByValue(headerByte) != ZeroType.ZERO_ARRAY) {
      throw new IllegalStateException(
          String.format("Invalid ZeroType. Expected: %s, value: %d, but found: %s, value: %d",
              ZeroType.ZERO_ARRAY, ZeroType.ZERO_ARRAY.getValue(),
              ZeroType.getByValue(headerByte) != null ?
                  ZeroType.getByValue(headerByte).toString() : "null", headerByte));
    }

    var arraySize = buffer.getShort();
    if (arraySize < 0) {
      throw new NegativeArraySizeException(
          String.format("Could not create an array with negative size value: %d", arraySize));
    }

    try {
      for (int i = 0; i < arraySize; ++i) {
        var zeroElement = decodeElement(buffer, depth);
        if (zeroElement == null) {
          throw new IllegalStateException(
              String.format("Unable to not decode ZeroArray item at index: %d", i));
        }

        zeroArray.addZeroElement(zeroElement);
      }

      return zeroArray;
    } catch (IllegalArgumentException exception) {
      throw exception;
    } catch (RuntimeException exception) {
      throw new IllegalArgumentException("Malformed ZeroArray", exception);
    }
  }

  private static ZeroMap decodeZeroMap(ByteBuffer buffer, int depth) {
    checkDepth(depth);
    requireRemaining(buffer, Byte.BYTES + Short.BYTES, "ZeroMap header");
    var zeroMap = newZeroMap();
    var headerByte = buffer.get();

    if (ZeroType.getByValue(headerByte) != ZeroType.ZERO_MAP) {
      throw new IllegalStateException(
          String.format("Invalid ZeroType. Expected: %s, value: %d, but found: %s, value: %d",
              ZeroType.ZERO_MAP, ZeroType.ZERO_MAP.getValue(),
              ZeroType.getByValue(headerByte), headerByte));
    }

    var mapSize = buffer.getShort();
    if (mapSize < 0) {
      throw new NegativeArraySizeException(
          String.format("Could not create an object with negative size value: %d", mapSize));
    }

    try {
      for (int i = 0; i < mapSize; ++i) {
        requireRemaining(buffer, Short.BYTES, "map key length");
        var keySize = buffer.getShort();
        if (keySize < 0) {
          throw new IllegalArgumentException("Invalid map key size: " + keySize);
        }
        requireRemaining(buffer, keySize, "map key");
        var keyData = new byte[keySize];
        buffer.get(keyData, 0, keyData.length);
        var key = new String(keyData, StandardCharsets.UTF_8);
        var zeroElement = decodeElement(buffer, depth);

        if (zeroElement == null) {
          throw new IllegalStateException(
              String.format("Unable to decode value for key: %s", Arrays.toString(keyData)));
        }

        zeroMap.putZeroElement(key, zeroElement);
      }

      return zeroMap;
    } catch (IllegalArgumentException exception) {
      throw exception;
    } catch (RuntimeException exception) {
      throw new IllegalArgumentException("Malformed ZeroMap", exception);
    }
  }

  private static short getCollectionSize(ByteBuffer buffer) {
    requireRemaining(buffer, Short.BYTES, "collection length");
    var collectionSize = buffer.getShort();
    if (collectionSize < 0) {
      throw new NegativeArraySizeException(
          String.format("Could not create a collection with negative size value: %d",
              collectionSize));
    }

    return collectionSize;
  }

  private static ByteBuffer encodeNull(ByteBuffer buffer) {
    buffer = ensureCapacity(buffer, Byte.BYTES);
    buffer.put((byte) ZeroType.NULL.getValue());
    return buffer;
  }

  private static ByteBuffer encodeBoolean(ByteBuffer buffer, Boolean data) {
    buffer = ensureCapacity(buffer, Byte.BYTES + Byte.BYTES);
    buffer.put((byte) ZeroType.BOOLEAN.getValue());
    buffer.put((byte) (data ? 1 : 0));
    return buffer;
  }

  private static ByteBuffer encodeByte(ByteBuffer buffer, Byte data) {
    buffer = ensureCapacity(buffer, Byte.BYTES + Byte.BYTES);
    buffer.put((byte) ZeroType.BYTE.getValue());
    buffer.put(data);
    return buffer;
  }

  private static ByteBuffer encodeShort(ByteBuffer buffer, Short data) {
    buffer = ensureCapacity(buffer, ENCODE_SHORT_BYTES);
    buffer.put((byte) ZeroType.SHORT.getValue());
    buffer.putShort(data);
    return buffer;
  }

  private static ByteBuffer encodeInteger(ByteBuffer buffer, Integer data) {
    buffer = ensureCapacity(buffer, ENCODE_INTEGER_BYTES);
    buffer.put((byte) ZeroType.INTEGER.getValue());
    buffer.putInt(data);
    return buffer;
  }

  private static ByteBuffer encodeLong(ByteBuffer buffer, Long data) {
    buffer = ensureCapacity(buffer, ENCODE_LONG_BYTES);
    buffer.put((byte) ZeroType.LONG.getValue());
    buffer.putLong(data);
    return buffer;
  }

  private static ByteBuffer encodeFloat(ByteBuffer buffer, Float data) {
    buffer = ensureCapacity(buffer, ENCODE_FLOAT_BYTES);
    buffer.put((byte) ZeroType.FLOAT.getValue());
    buffer.putFloat(data);
    return buffer;
  }

  private static ByteBuffer encodeDouble(ByteBuffer buffer, Double data) {
    buffer = ensureCapacity(buffer, ENCODE_DOUBLE_BYTES);
    buffer.put((byte) ZeroType.DOUBLE.getValue());
    buffer.putDouble(data);
    return buffer;
  }

  private static ByteBuffer encodeString(ByteBuffer buffer, String data) {
    if (data == null) {
      throw new IllegalArgumentException("String value must not be null");
    }
    var stringBytes = data.getBytes(StandardCharsets.UTF_8);
    checkShortLength(stringBytes.length, "string");
    buffer = ensureCapacity(buffer, ENCODE_HEADER_STRING_BYTES + stringBytes.length);
    buffer.put((byte) ZeroType.STRING.getValue());
    buffer.putShort((short) stringBytes.length);
    buffer.put(stringBytes);
    return buffer;
  }

  private static ByteBuffer encodeBooleanArray(ByteBuffer buffer, Collection<Boolean> data) {
    checkCollection(data, "boolean array");
    buffer = ensureCapacity(buffer, ENCODE_HEADER_BOOLEAN_ARRAY_BYTES + data.size());
    buffer.put((byte) ZeroType.BOOLEAN_ARRAY.getValue());
    buffer.putShort((short) data.size());

    for (Boolean boolValue : data) {
      buffer.put((byte) (boolValue ? 1 : 0));
    }

    return buffer;
  }

  private static ByteBuffer encodeByteArray(ByteBuffer buffer, byte[] data) {
    if (data == null || data.length > MAX_BINARY_BYTES) {
      throw new IllegalArgumentException("Invalid byte array size");
    }
    buffer = ensureCapacity(buffer, ENCODE_HEADER_BYTE_ARRAY_BYTES + data.length);
    buffer.put((byte) ZeroType.BYTE_ARRAY.getValue());
    buffer.putInt(data.length);
    buffer.put(data);
    return buffer;
  }

  private static ByteBuffer encodeShortArray(ByteBuffer buffer, Collection<Short> data) {
    checkCollection(data, "short array");
    buffer = ensureCapacity(buffer, ENCODE_HEADER_NUMERIC_ARRAY_BYTES + Short.BYTES * data.size());
    buffer.put((byte) ZeroType.SHORT_ARRAY.getValue());
    buffer.putShort((short) data.size());

    for (Short shortValue : data) {
      buffer.putShort(shortValue);
    }

    return buffer;
  }

  private static ByteBuffer encodeIntegerArray(ByteBuffer buffer, Collection<Integer> data) {
    checkCollection(data, "integer array");
    buffer = ensureCapacity(
        buffer, ENCODE_HEADER_NUMERIC_ARRAY_BYTES + Integer.BYTES * data.size());
    buffer.put((byte) ZeroType.INTEGER_ARRAY.getValue());
    buffer.putShort((short) data.size());

    for (Integer integerValue : data) {
      buffer.putInt(integerValue);
    }

    return buffer;
  }

  private static ByteBuffer encodeLongArray(ByteBuffer buffer, Collection<Long> data) {
    checkCollection(data, "long array");
    buffer = ensureCapacity(buffer, ENCODE_HEADER_NUMERIC_ARRAY_BYTES + Long.BYTES * data.size());
    buffer.put((byte) ZeroType.LONG_ARRAY.getValue());
    buffer.putShort((short) data.size());

    for (Long longValue : data) {
      buffer.putLong(longValue);
    }

    return buffer;
  }

  private static ByteBuffer encodeFloatArray(ByteBuffer buffer, Collection<Float> data) {
    checkCollection(data, "float array");
    buffer = ensureCapacity(buffer, ENCODE_HEADER_NUMERIC_ARRAY_BYTES + Float.BYTES * data.size());
    buffer.put((byte) ZeroType.FLOAT_ARRAY.getValue());
    buffer.putShort((short) data.size());

    for (Float floatValue : data) {
      buffer.putFloat(floatValue);
    }

    return buffer;
  }

  private static ByteBuffer encodeDoubleArray(ByteBuffer buffer, Collection<Double> data) {
    checkCollection(data, "double array");
    buffer = ensureCapacity(
        buffer, ENCODE_HEADER_NUMERIC_ARRAY_BYTES + Double.BYTES * data.size());
    buffer.put((byte) ZeroType.DOUBLE_ARRAY.getValue());
    buffer.putShort((short) data.size());

    for (Double doubleValue : data) {
      buffer.putDouble(doubleValue);
    }

    return buffer;
  }

  private static ByteBuffer encodeStringArray(ByteBuffer buffer, Collection<String> collection) {
    checkCollection(collection, "string array");
    var encodedItems = new ArrayList<byte[]>(collection.size());
    var totalStringsLengthInBytes = 0L;

    for (var item : collection) {
      if (item == null) {
        throw new IllegalArgumentException("String array items must not be null");
      }
      var stringInBinary = item.getBytes(StandardCharsets.UTF_8);
      checkShortLength(stringInBinary.length, "string array item");
      encodedItems.add(stringInBinary);
      totalStringsLengthInBytes += Short.BYTES + stringInBinary.length;
    }

    long totalBytes = ENCODE_HEADER_STRING_ARRAY_BYTES + totalStringsLengthInBytes;
    if (totalBytes > MAX_MESSAGE_BYTES) {
      throw new IllegalArgumentException("Encoded Zero payload exceeds maximum size");
    }

    buffer = ensureCapacity(buffer, (int) totalBytes);
    buffer.put((byte) ZeroType.STRING_ARRAY.getValue());
    buffer.putShort((short) encodedItems.size());
    for (var bytes : encodedItems) {
      buffer.putShort((short) bytes.length);
      buffer.put(bytes);
    }

    return buffer;
  }

  private static ByteBuffer encodeZeroMapKey(ByteBuffer buffer, String key) {
    if (key == null) {
      throw new IllegalArgumentException("Map key must not be null");
    }
    var keyBytes = key.getBytes(StandardCharsets.UTF_8);
    checkShortLength(keyBytes.length, "map key");
    buffer = ensureCapacity(buffer, Short.BYTES + keyBytes.length);
    buffer.putShort((short) keyBytes.length);
    buffer.put(keyBytes);
    return buffer;
  }

  private static ByteBuffer ensureCapacity(ByteBuffer buffer, int additionalBytes) {
    long required = (long) buffer.position() + additionalBytes;
    if (required > MAX_MESSAGE_BYTES) {
      throw new IllegalArgumentException("Encoded Zero payload exceeds maximum size");
    }
    if (buffer.remaining() < additionalBytes) {
      long doubled = (long) buffer.capacity() << 1;
      int newCapacity = (int) Math.min(MAX_MESSAGE_BYTES, Math.max(doubled, required));
      var newBuffer = ByteBuffer.allocate(newCapacity);
      buffer.flip();
      newBuffer.put(buffer);
      buffer = newBuffer;
    }
    return buffer;
  }

  private static void validatePayload(byte[] binaries) {
    if (binaries == null || binaries.length < Byte.BYTES + Short.BYTES
        || binaries.length > MAX_MESSAGE_BYTES) {
      throw new IllegalArgumentException("Zero payload size is invalid");
    }
  }

  private static void requireFullyConsumed(ByteBuffer buffer) {
    if (buffer.hasRemaining()) {
      throw new IllegalArgumentException("Trailing bytes are not allowed in a Zero payload");
    }
  }

  private static void requireRemaining(ByteBuffer buffer, int bytes, String description) {
    if (bytes < 0 || buffer.remaining() < bytes) {
      throw new IllegalArgumentException("Insufficient bytes for " + description);
    }
  }

  private static void requireElements(ByteBuffer buffer, int elements, int bytesPerElement, String description) {
    long required = (long) elements * bytesPerElement;
    if (required > Integer.MAX_VALUE) {
      throw new IllegalArgumentException("Invalid size for " + description);
    }
    requireRemaining(buffer, (int) required, description);
  }

  private static void checkDepth(int depth) {
    if (depth > MAX_NESTING_DEPTH) {
      throw new IllegalArgumentException("Zero payload nesting is too deep");
    }
  }

  private static void checkShortLength(int size, String description) {
    if (size < 0 || size > MAX_SHORT_LENGTH) {
      throw new IllegalArgumentException(description + " exceeds the protocol limit: " + size);
    }
  }

  private static void checkCollection(Collection<?> collection, String description) {
    if (collection == null) {
      throw new IllegalArgumentException(description + " must not be null");
    }
    checkShortLength(collection.size(), description);
  }
}
