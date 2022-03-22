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

  private final Map<String, ZeroElement> data;

  private ZeroMapImpl() {
    data = new HashMap<>();
  }

  public static ZeroMap newInstance() {
    return new ZeroMapImpl();
  }

  public static ZeroMap newInstance(byte[] binary) {
    return ZeroUtility.binaryToObject(binary);
  }

  @Override
  public byte[] toBinary() {
    return ZeroUtility.objectToBinary(this);
  }

  @Override
  public boolean isNull(String key) {
    var data = getZeroElement(key);
    return data != null && data.getType() == ZeroType.NULL;
  }

  @Override
  public boolean containsKey(String key) {
    return data.containsKey(key);
  }

  @Override
  public boolean removeElement(String key) {
    return data.remove(key) != null;
  }

  @Override
  public Set<String> getKeys() {
    return data.keySet();
  }

  @Override
  public Set<String> getReadonlyKeys() {
    return Set.copyOf(data.keySet());
  }

  @Override
  public int size() {
    return data.size();
  }

  @Override
  public Iterator<Entry<String, ZeroElement>> iterator() {
    return data.entrySet().iterator();
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
    return data.get(key);
  }

  @Override
  public ZeroMap putNull(String key) {
    return putData(key, ZeroType.NULL, null);
  }

  @Override
  public ZeroMap putBoolean(String key, boolean element) {
    return putData(key, ZeroType.BOOLEAN, element);
  }

  @Override
  public ZeroMap putByte(String key, byte element) {
    return putData(key, ZeroType.BYTE, element);
  }

  @Override
  public ZeroMap putShort(String key, short element) {
    return putData(key, ZeroType.SHORT, element);
  }

  @Override
  public ZeroMap putInteger(String key, int element) {
    return putData(key, ZeroType.INTEGER, element);
  }

  @Override
  public ZeroMap putLong(String key, long element) {
    return putData(key, ZeroType.LONG, element);
  }

  @Override
  public ZeroMap putFloat(String key, float element) {
    return putData(key, ZeroType.FLOAT, element);
  }

  @Override
  public ZeroMap putDouble(String key, double element) {
    return putData(key, ZeroType.DOUBLE, element);
  }

  @Override
  public ZeroMap putString(String key, String element) {
    return putData(key, ZeroType.STRING, element);
  }

  @Override
  public ZeroMap putZeroArray(String key, ZeroArray element) {
    return putData(key, ZeroType.ZERO_ARRAY, element);
  }

  @Override
  public ZeroMap putZeroMap(String key, ZeroMap element) {
    return putData(key, ZeroType.ZERO_OBJECT, element);
  }

  @Override
  public ZeroMap putZeroElement(String key, ZeroElement element) {
    this.data.put(key, element);
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
  public ZeroMap putBooleanArray(String key, Collection<Boolean> element) {
    return putData(key, ZeroType.BOOLEAN_ARRAY, element);
  }

  @Override
  public ZeroMap putByteArray(String key, byte[] element) {
    return putData(key, ZeroType.BYTE_ARRAY, element);
  }

  @Override
  public ZeroMap putShortArray(String key, Collection<Short> element) {
    return putData(key, ZeroType.SHORT_ARRAY, element);
  }

  @Override
  public ZeroMap putIntegerArray(String key, Collection<Integer> element) {
    return putData(key, ZeroType.INTEGER_ARRAY, element);
  }

  @Override
  public ZeroMap putLongArray(String key, Collection<Long> element) {
    return putData(key, ZeroType.LONG_ARRAY, element);
  }

  @Override
  public ZeroMap putFloatArray(String key, Collection<Float> element) {
    return putData(key, ZeroType.FLOAT_ARRAY, element);
  }

  @Override
  public ZeroMap putDoubleArray(String key, Collection<Double> element) {
    return putData(key, ZeroType.DOUBLE_ARRAY, element);
  }

  @Override
  public ZeroMap putStringArray(String key, Collection<String> element) {
    return putData(key, ZeroType.STRING_ARRAY, element);
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
      if (zeroElement.getType() == ZeroType.ZERO_OBJECT) {
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

  private ZeroMap putData(String key, ZeroType type, Object element) {
    data.put(key, ZeroElementImpl.newInstance(type, element));
    return this;
  }
}
