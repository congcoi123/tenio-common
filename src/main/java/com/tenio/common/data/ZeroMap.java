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
import java.util.Map;

/**
 * This class holds data by pairs of key and value, works like a map.
 */
public interface ZeroMap extends ReadonlyZeroMap, Iterable<Map.Entry<String, ZeroElement>> {

  boolean removeElement(String key);

  ZeroMap putNull(String key);

  ZeroMap putBoolean(String key, boolean data);

  ZeroMap putByte(String key, byte data);

  ZeroMap putShort(String key, short data);

  ZeroMap putInteger(String key, int data);

  ZeroMap putLong(String key, long data);

  ZeroMap putFloat(String key, float data);

  ZeroMap putDouble(String key, double data);

  ZeroMap putString(String key, String data);

  ZeroMap putZeroArray(String key, ZeroArray data);

  ZeroMap putZeroMap(String key, ZeroMap data);

  ZeroMap putZeroElement(String key, ZeroElement element);

  ZeroMap putBooleanArray(String key, Collection<Boolean> data);

  ZeroMap putByteArray(String key, byte[] data);

  ZeroMap putShortArray(String key, Collection<Short> data);

  ZeroMap putIntegerArray(String key, Collection<Integer> data);

  ZeroMap putLongArray(String key, Collection<Long> data);

  ZeroMap putFloatArray(String key, Collection<Float> data);

  ZeroMap putDoubleArray(String key, Collection<Double> data);

  ZeroMap putStringArray(String key, Collection<String> data);

  ReadonlyZeroMap getReadonlyZeroMap();
}
