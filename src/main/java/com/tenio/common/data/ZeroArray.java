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
 * This class holds sequence elements.
 */
public interface ZeroArray extends ReadonlyZeroArray, Iterable<ZeroElement> {

  void removeElementAt(int index);

  ZeroArray addNull();

  ZeroArray addBoolean(boolean data);

  ZeroArray addByte(byte data);

  ZeroArray addShort(short data);

  ZeroArray addInteger(int data);

  ZeroArray addLong(long data);

  ZeroArray addFloat(float data);

  ZeroArray addDouble(double data);

  ZeroArray addString(String data);

  ZeroArray addZeroArray(ZeroArray data);

  ZeroArray addZeroMap(ZeroMap data);

  ZeroArray addZeroElement(ZeroElement element);

  ZeroArray addBooleanArray(Collection<Boolean> data);

  ZeroArray addByteArray(byte[] data);

  ZeroArray addShortArray(Collection<Short> data);

  ZeroArray addIntegerArray(Collection<Integer> data);

  ZeroArray addLongArray(Collection<Long> data);

  ZeroArray addFloatArray(Collection<Float> data);

  ZeroArray addDoubleArray(Collection<Double> data);

  ZeroArray addStringArray(Collection<String> data);

  ReadonlyZeroArray getReadonlyZeroArray();
}
