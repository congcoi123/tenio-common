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

package com.tenio.common.data;

import java.util.Collection;

/**
 * This class holds sequence elements in read-only mode.
 */
public interface ReadonlyZeroArray extends ZeroCollection {

  boolean contains(Object element);

  Object getDataForElementAt(int index);

  boolean isNull(int index);

  Boolean getBoolean(int index);

  Byte getByte(int index);

  Short getShort(int index);

  Integer getInteger(int index);

  Long getLong(int index);

  Float getFloat(int index);

  Double getDouble(int index);

  String getString(int index);

  ZeroArray getZeroArray(int index);

  ZeroMap getZeroMap(int index);

  ZeroElement getZeroElement(int index);

  Collection<Boolean> getBooleanArray(int index);

  byte[] getByteArray(int index);

  Collection<Short> getShortArray(int index);

  Collection<Integer> getIntegerArray(int index);

  Collection<Long> getLongArray(int index);

  Collection<Float> getFloatArray(int index);

  Collection<Double> getDoubleArray(int index);

  Collection<String> getStringArray(int index);
}