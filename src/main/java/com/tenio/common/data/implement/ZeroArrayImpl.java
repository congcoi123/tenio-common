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

import com.tenio.common.data.ZeroArray;
import com.tenio.common.data.ZeroDataType;
import com.tenio.common.data.ZeroMap;
import com.tenio.common.data.utility.ZeroDataUtility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * An implementation for the zero array.
 *
 * @see ZeroArray
 */
public final class ZeroArrayImpl implements ZeroArray {

  private final List<ZeroDataImpl> data;

  private ZeroArrayImpl() {
    data = new ArrayList<>();
  }

  public static ZeroArray newInstance() {
    return new ZeroArrayImpl();
  }

  public static ZeroArray newInstance(byte[] binary) {
    return ZeroDataUtility.binaryToArray(binary);
  }

  @Override
  public byte[] toBinary() {
    return ZeroDataUtility.arrayToBinary(this);
  }

  @Override
  public boolean contains(Object element) {
    var match =
        data.stream().filter(data -> data.getData().equals(element)).findFirst();
    return match.orElse(null) != null;
  }

  @Override
  public Iterator<ZeroDataImpl> iterator() {
    return data.iterator();
  }

  @Override
  public Object getElementAt(int index) {
    var data = getZeroData(index);
    return data != null ? data.getData() : null;
  }

  @Override
  public void removeElementAt(int index) {
    data.remove(index);
  }

  @Override
  public int size() {
    return data.size();
  }

  @Override
  public boolean isNull(int index) {
    var data = getZeroData(index);
    return data != null && data.getType() == ZeroDataType.NULL;
  }

  @Override
  public Boolean getBoolean(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Boolean) data.getData();
  }

  @Override
  public Byte getByte(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Byte) data.getData();
  }

  @Override
  public Short getShort(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Short) data.getData();
  }

  @Override
  public Integer getInteger(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Integer) data.getData();
  }

  @Override
  public Long getLong(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Long) data.getData();
  }

  @Override
  public Float getFloat(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Float) data.getData();
  }

  @Override
  public Double getDouble(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Double) data.getData();
  }

  @Override
  public String getString(int index) {
    var data = getZeroData(index);
    return data == null ? null : (String) data.getData();
  }

  @Override
  public ZeroArray getZeroArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (ZeroArray) data.getData();
  }

  @Override
  public ZeroMap getZeroObject(int index) {
    var data = getZeroData(index);
    return data == null ? null : (ZeroMap) data.getData();
  }

  @Override
  public ZeroDataImpl getZeroData(int index) {
    return data.get(index);
  }

  @Override
  public ZeroArray addNull() {
    return addData(ZeroDataType.NULL, null);
  }

  @Override
  public ZeroArray addBoolean(boolean element) {
    return addData(ZeroDataType.BOOLEAN, element);
  }

  @Override
  public ZeroArray addByte(byte element) {
    return addData(ZeroDataType.BYTE, element);
  }

  @Override
  public ZeroArray addShort(short element) {
    return addData(ZeroDataType.SHORT, element);
  }

  @Override
  public ZeroArray addInteger(int element) {
    return addData(ZeroDataType.INTEGER, element);
  }

  @Override
  public ZeroArray addLong(long element) {
    return addData(ZeroDataType.LONG, element);
  }

  @Override
  public ZeroArray addFloat(float element) {
    return addData(ZeroDataType.FLOAT, element);
  }

  @Override
  public ZeroArray addDouble(double element) {
    return addData(ZeroDataType.DOUBLE, element);
  }

  @Override
  public ZeroArray addString(String element) {
    return addData(ZeroDataType.STRING, element);
  }

  @Override
  public ZeroArray addZeroArray(ZeroArray element) {
    return addData(ZeroDataType.ZERO_ARRAY, element);
  }

  @Override
  public ZeroArray addZeroObject(ZeroMap element) {
    return addData(ZeroDataType.ZERO_OBJECT, element);
  }

  @Override
  public ZeroArray addZeroData(ZeroDataImpl data) {
    this.data.add(data);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Boolean> getBooleanArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Collection<Boolean>) data.getData();
  }

  @Override
  public byte[] getByteArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (byte[]) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Short> getShortArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Collection<Short>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Integer> getIntegerArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Collection<Integer>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Long> getLongArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Collection<Long>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Float> getFloatArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Collection<Float>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Double> getDoubleArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Collection<Double>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<String> getStringArray(int index) {
    var data = getZeroData(index);
    return data == null ? null : (Collection<String>) data.getData();
  }

  @Override
  public ZeroArray addBooleanArray(Collection<Boolean> element) {
    return addData(ZeroDataType.BOOLEAN_ARRAY, element);
  }

  @Override
  public ZeroArray addByteArray(byte[] element) {
    return addData(ZeroDataType.BYTE_ARRAY, element);
  }

  @Override
  public ZeroArray addShortArray(Collection<Short> element) {
    return addData(ZeroDataType.SHORT_ARRAY, element);
  }

  @Override
  public ZeroArray addIntegerArray(Collection<Integer> element) {
    return addData(ZeroDataType.INTEGER_ARRAY, element);
  }

  @Override
  public ZeroArray addLongArray(Collection<Long> element) {
    return addData(ZeroDataType.LONG_ARRAY, element);
  }

  @Override
  public ZeroArray addFloatArray(Collection<Float> element) {
    return addData(ZeroDataType.FLOAT_ARRAY, element);
  }

  @Override
  public ZeroArray addDoubleArray(Collection<Double> element) {
    return addData(ZeroDataType.DOUBLE_ARRAY, element);
  }

  @Override
  public ZeroArray addStringArray(Collection<String> element) {
    return addData(ZeroDataType.STRING_ARRAY, element);
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append('{');

    Object toString;
    ZeroDataImpl zeroDataImpl;
    for (var iterator = iterator(); iterator.hasNext(); builder.append(" (")
        .append(zeroDataImpl.getType().toString().toLowerCase()).append(") ").append(toString)
        .append(';')) {
      zeroDataImpl = iterator.next();
      if (zeroDataImpl.getType() == ZeroDataType.ZERO_OBJECT) {
        toString = zeroDataImpl.getData().toString();
      } else if (zeroDataImpl.getType() == ZeroDataType.ZERO_ARRAY) {
        toString = zeroDataImpl.getData().toString();
      } else if (zeroDataImpl.getType() == ZeroDataType.BYTE_ARRAY) {
        toString = String.format("byte[%d]", ((byte[]) zeroDataImpl.getData()).length);
      } else {
        toString = zeroDataImpl.getData().toString();
      }
    }

    if (size() > 0) {
      builder.setLength(builder.length() - 1);
    }

    builder.append(" }");
    return builder.toString();
  }

  private ZeroArray addData(ZeroDataType type, Object element) {
    data.add(ZeroDataImpl.newInstance(type, element));
    return this;
  }
}
