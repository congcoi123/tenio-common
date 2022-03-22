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

import com.tenio.common.data.ReadonlyZeroArray;
import com.tenio.common.data.ZeroArray;
import com.tenio.common.data.ZeroElement;
import com.tenio.common.data.ZeroType;
import com.tenio.common.data.ZeroMap;
import com.tenio.common.data.utility.ZeroUtility;
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

  private final List<ZeroElement> data;

  private ZeroArrayImpl() {
    data = new ArrayList<>();
  }

  public static ZeroArray newInstance() {
    return new ZeroArrayImpl();
  }

  public static ZeroArray newInstance(byte[] binary) {
    return ZeroUtility.binaryToArray(binary);
  }

  @Override
  public byte[] toBinary() {
    return ZeroUtility.arrayToBinary(this);
  }

  @Override
  public boolean contains(Object element) {
    var match =
        data.stream().filter(data -> data.getData().equals(element)).findFirst();
    return match.orElse(null) != null;
  }

  @Override
  public Iterator<ZeroElement> iterator() {
    return data.iterator();
  }

  @Override
  public Object getElementAt(int index) {
    var data = getZeroElement(index);
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
    var data = getZeroElement(index);
    return data != null && data.getType() == ZeroType.NULL;
  }

  @Override
  public Boolean getBoolean(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Boolean) data.getData();
  }

  @Override
  public Byte getByte(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Byte) data.getData();
  }

  @Override
  public Short getShort(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Short) data.getData();
  }

  @Override
  public Integer getInteger(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Integer) data.getData();
  }

  @Override
  public Long getLong(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Long) data.getData();
  }

  @Override
  public Float getFloat(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Float) data.getData();
  }

  @Override
  public Double getDouble(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Double) data.getData();
  }

  @Override
  public String getString(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (String) data.getData();
  }

  @Override
  public ZeroArray getZeroArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (ZeroArray) data.getData();
  }

  @Override
  public ZeroMap getZeroMap(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (ZeroMap) data.getData();
  }

  @Override
  public ZeroElement getZeroElement(int index) {
    return data.get(index);
  }

  @Override
  public ZeroArray addNull() {
    return addData(ZeroType.NULL, null);
  }

  @Override
  public ZeroArray addBoolean(boolean element) {
    return addData(ZeroType.BOOLEAN, element);
  }

  @Override
  public ZeroArray addByte(byte element) {
    return addData(ZeroType.BYTE, element);
  }

  @Override
  public ZeroArray addShort(short element) {
    return addData(ZeroType.SHORT, element);
  }

  @Override
  public ZeroArray addInteger(int element) {
    return addData(ZeroType.INTEGER, element);
  }

  @Override
  public ZeroArray addLong(long element) {
    return addData(ZeroType.LONG, element);
  }

  @Override
  public ZeroArray addFloat(float element) {
    return addData(ZeroType.FLOAT, element);
  }

  @Override
  public ZeroArray addDouble(double element) {
    return addData(ZeroType.DOUBLE, element);
  }

  @Override
  public ZeroArray addString(String element) {
    return addData(ZeroType.STRING, element);
  }

  @Override
  public ZeroArray addZeroArray(ZeroArray element) {
    return addData(ZeroType.ZERO_ARRAY, element);
  }

  @Override
  public ZeroArray addZeroMap(ZeroMap element) {
    return addData(ZeroType.ZERO_OBJECT, element);
  }

  @Override
  public ZeroArray addZeroElement(ZeroElement element) {
    this.data.add(element);
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Boolean> getBooleanArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Collection<Boolean>) data.getData();
  }

  @Override
  public byte[] getByteArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (byte[]) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Short> getShortArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Collection<Short>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Integer> getIntegerArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Collection<Integer>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Long> getLongArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Collection<Long>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Float> getFloatArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Collection<Float>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<Double> getDoubleArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Collection<Double>) data.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public Collection<String> getStringArray(int index) {
    var data = getZeroElement(index);
    return data == null ? null : (Collection<String>) data.getData();
  }

  @Override
  public ZeroArray addBooleanArray(Collection<Boolean> element) {
    return addData(ZeroType.BOOLEAN_ARRAY, element);
  }

  @Override
  public ZeroArray addByteArray(byte[] element) {
    return addData(ZeroType.BYTE_ARRAY, element);
  }

  @Override
  public ZeroArray addShortArray(Collection<Short> element) {
    return addData(ZeroType.SHORT_ARRAY, element);
  }

  @Override
  public ZeroArray addIntegerArray(Collection<Integer> element) {
    return addData(ZeroType.INTEGER_ARRAY, element);
  }

  @Override
  public ZeroArray addLongArray(Collection<Long> element) {
    return addData(ZeroType.LONG_ARRAY, element);
  }

  @Override
  public ZeroArray addFloatArray(Collection<Float> element) {
    return addData(ZeroType.FLOAT_ARRAY, element);
  }

  @Override
  public ZeroArray addDoubleArray(Collection<Double> element) {
    return addData(ZeroType.DOUBLE_ARRAY, element);
  }

  @Override
  public ZeroArray addStringArray(Collection<String> element) {
    return addData(ZeroType.STRING_ARRAY, element);
  }

  @Override
  public ReadonlyZeroArray getReadonlyZeroArray() {
    return this;
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append('{');

    Object toString;
    ZeroElement zeroElement;
    for (var iterator = iterator(); iterator.hasNext(); builder.append(" (")
        .append(zeroElement.getType().toString().toLowerCase()).append(") ").append(toString)
        .append(';')) {
      zeroElement = iterator.next();
      if (zeroElement.getType() == ZeroType.ZERO_OBJECT) {
        toString = zeroElement.getData().toString();
      } else if (zeroElement.getType() == ZeroType.ZERO_ARRAY) {
        toString = zeroElement.getData().toString();
      } else if (zeroElement.getType() == ZeroType.BYTE_ARRAY) {
        toString = String.format("byte[%d]", ((byte[]) zeroElement.getData()).length);
      } else {
        toString = zeroElement.getData().toString();
      }
    }

    if (size() > 0) {
      builder.setLength(builder.length() - 1);
    }

    builder.append(" }");
    return builder.toString();
  }

  private ZeroArray addData(ZeroType type, Object element) {
    data.add(ZeroElementImpl.newInstance(type, element));
    return this;
  }
}
