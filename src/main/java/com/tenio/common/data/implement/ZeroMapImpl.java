/*
The MIT License

Copyright (c) 2016-2022 kong <congcoi123@gmail.com>

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

package com.tenio.common.data.implement;

import com.tenio.common.data.ReadonlyZeroMap;
import com.tenio.common.data.ZeroArray;
import com.tenio.common.data.ZeroElement;
import com.tenio.common.data.ZeroMap;
import com.tenio.common.data.ZeroType;
import com.tenio.common.data.utility.ZeroUtility;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * An implementation for the zero object.
 */
public final class ZeroMapImpl implements ZeroMap {

  private final Map<String, ZeroElement> map;

  private ZeroMapImpl() {
    map = new HashMap<>();
  }

  public static ZeroMap newInstance() {
    return new ZeroMapImpl();
  }

  public static ZeroMap newInstance(byte[] binary) {
    return ZeroUtility.binaryToMap(binary);
  }

  @Override
  public byte[] toBinary() {
    return ZeroUtility.mapToBinary(this);
  }

  @Override
  public boolean isNull(String key) {
    var data = getZeroElement(key);
    return data != null && data.getType() == ZeroType.NULL;
  }

  @Override
  public boolean containsKey(String key) {
    return map.containsKey(key);
  }

  @Override
  public boolean removeElement(String key) {
    return map.remove(key) != null;
  }

  @Override
  public Set<String> getKeys() {
    return map.keySet();
  }

  @Override
  public Set<String> getReadonlyKeys() {
    return Set.copyOf(map.keySet());
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public Iterator<Entry<String, ZeroElement>> iterator() {
    return map.entrySet().iterator();
  }

  @Override
  public Boolean getBoolean(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Boolean) data.getData();
  }

  @Override
  public Byte getByte(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Byte) data.getData();
  }

  @Override
  public Short getShort(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Short) data.getData();
  }

  @Override
  public Integer getInteger(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Integer) data.getData();
  }

  @Override
  public Long getLong(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Long) data.getData();
  }

  @Override
  public Float getFloat(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Float) data.getData();
  }

  @Override
  public Double getDouble(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Double) data.getData();
  }

  @Override
  public String getString(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (String) data.getData();
  }

  @Override
  public ZeroArray getZeroArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (ZeroArray) data.getData();
  }

  @Override
  public ZeroMap getZeroMap(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (ZeroMap) data.getData();
  }

  @Override
  public ZeroElement getZeroElement(String key) {
    return map.get(key);
  }

  @Override
  public ZeroMap putNull(String key) {
    return putElement(key, ZeroType.NULL, null);
  }

  @Override
  public ZeroMap putBoolean(String key, boolean data) {
    return putElement(key, ZeroType.BOOLEAN, data);
  }

  @Override
  public ZeroMap putByte(String key, byte data) {
    return putElement(key, ZeroType.BYTE, data);
  }

  @Override
  public ZeroMap putShort(String key, short data) {
    return putElement(key, ZeroType.SHORT, data);
  }

  @Override
  public ZeroMap putInteger(String key, int data) {
    return putElement(key, ZeroType.INTEGER, data);
  }

  @Override
  public ZeroMap putLong(String key, long data) {
    return putElement(key, ZeroType.LONG, data);
  }

  @Override
  public ZeroMap putFloat(String key, float data) {
    return putElement(key, ZeroType.FLOAT, data);
  }

  @Override
  public ZeroMap putDouble(String key, double data) {
    return putElement(key, ZeroType.DOUBLE, data);
  }

  @Override
  public ZeroMap putString(String key, String data) {
    return putElement(key, ZeroType.STRING, data);
  }

  @Override
  public ZeroMap putZeroArray(String key, ZeroArray data) {
    return putElement(key, ZeroType.ZERO_ARRAY, data);
  }

  @Override
  public ZeroMap putZeroMap(String key, ZeroMap data) {
    return putElement(key, ZeroType.ZERO_MAP, data);
  }

  @Override
  public ZeroMap putZeroElement(String key, ZeroElement element) {
    map.put(key, element);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Boolean> getBooleanArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Collection<Boolean>) data.getData();
  }

  @Override
  public byte[] getByteArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (byte[]) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Short> getShortArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Collection<Short>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Integer> getIntegerArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Collection<Integer>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Long> getLongArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Collection<Long>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Float> getFloatArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Collection<Float>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Double> getDoubleArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Collection<Double>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<String> getStringArray(String key) {
    var data = getZeroElement(key);
    return data == null ? null : (Collection<String>) data.getData();
  }

  @Override
  public ZeroMap putBooleanArray(String key, Collection<Boolean> data) {
    return putElement(key, ZeroType.BOOLEAN_ARRAY, data);
  }

  @Override
  public ZeroMap putByteArray(String key, byte[] data) {
    return putElement(key, ZeroType.BYTE_ARRAY, data);
  }

  @Override
  public ZeroMap putShortArray(String key, Collection<Short> data) {
    return putElement(key, ZeroType.SHORT_ARRAY, data);
  }

  @Override
  public ZeroMap putIntegerArray(String key, Collection<Integer> data) {
    return putElement(key, ZeroType.INTEGER_ARRAY, data);
  }

  @Override
  public ZeroMap putLongArray(String key, Collection<Long> data) {
    return putElement(key, ZeroType.LONG_ARRAY, data);
  }

  @Override
  public ZeroMap putFloatArray(String key, Collection<Float> data) {
    return putElement(key, ZeroType.FLOAT_ARRAY, data);
  }

  @Override
  public ZeroMap putDoubleArray(String key, Collection<Double> data) {
    return putElement(key, ZeroType.DOUBLE_ARRAY, data);
  }

  @Override
  public ZeroMap putStringArray(String key, Collection<String> data) {
    return putElement(key, ZeroType.STRING_ARRAY, data);
  }

  @Override
  public ReadonlyZeroMap getReadonlyZeroMap() {
    return this;
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append('{');

    for (var iteratorKey = getKeys().iterator(); iteratorKey.hasNext(); builder.append(';')) {
      var key = iteratorKey.next();
      var zeroElement = getZeroElement(key);
      builder.append(" (").append(zeroElement.getType().toString().toLowerCase()).append(") ")
          .append(key)
          .append(": ");
      if (zeroElement.getType() == ZeroType.ZERO_MAP) {
        builder.append(zeroElement.getData().toString());
      } else if (zeroElement.getType() == ZeroType.ZERO_ARRAY) {
        builder.append(zeroElement.getData().toString());
      } else if (zeroElement.getType() == ZeroType.BYTE_ARRAY) {
        builder.append(String.format("byte[%d]", ((byte[]) zeroElement.getData()).length));
      } else {
        builder.append(zeroElement.getData().toString());
      }
    }

    if (size() > 0) {
      builder.setLength(builder.length() - 1);
    }

    builder.append(" }");
    return builder.toString();
  }

  private ZeroMap putElement(String key, ZeroType type, Object data) {
    map.put(key, ZeroUtility.newZeroElement(type, data));
    return this;
  }
}
