/*
The MIT License

Copyright (c) 2016-2021 kong <congcoi123@gmail.com>

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

import com.tenio.common.data.implement.ZeroDataImpl;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class holds data by pairs of key and value, works like a map.
 */
public interface ZeroMap extends ZeroCollection {

  boolean isNull(String key);

  boolean containsKey(String key);

  boolean removeElement(String key);

  Set<String> getKeys();

  Iterator<Entry<String, ZeroDataImpl>> iterator();

  Boolean getBoolean(String key);

  Byte getByte(String key);

  Short getShort(String key);

  Integer getInteger(String key);

  Long getLong(String key);

  Float getFloat(String key);

  Double getDouble(String key);

  String getString(String key);

  ZeroArray getZeroArray(String key);

  ZeroMap getZeroObject(String key);

  ZeroDataImpl getZeroData(String key);

  ZeroMap putNull(String key);

  ZeroMap putBoolean(String key, boolean element);

  ZeroMap putByte(String key, byte element);

  ZeroMap putShort(String key, short element);

  ZeroMap putInteger(String key, int element);

  ZeroMap putLong(String key, long element);

  ZeroMap putFloat(String key, float element);

  ZeroMap putDouble(String key, double element);

  ZeroMap putString(String key, String element);

  ZeroMap putZeroArray(String key, ZeroArray element);

  ZeroMap putZeroObject(String key, ZeroMap element);

  ZeroMap putZeroData(String key, ZeroDataImpl data);

  Collection<Boolean> getBooleanArray(String key);

  byte[] getByteArray(String key);

  Collection<Short> getShortArray(String key);

  Collection<Integer> getIntegerArray(String key);

  Collection<Long> getLongArray(String key);

  Collection<Float> getFloatArray(String key);

  Collection<Double> getDoubleArray(String key);

  Collection<String> getStringArray(String key);

  ZeroMap putBooleanArray(String key, Collection<Boolean> element);

  ZeroMap putByteArray(String key, byte[] element);

  ZeroMap putShortArray(String key, Collection<Short> element);

  ZeroMap putIntegerArray(String key, Collection<Integer> element);

  ZeroMap putLongArray(String key, Collection<Long> element);

  ZeroMap putFloatArray(String key, Collection<Float> element);

  ZeroMap putDoubleArray(String key, Collection<Double> element);

  ZeroMap putStringArray(String key, Collection<String> element);
}
