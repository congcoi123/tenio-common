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
import java.util.Set;

/**
 * This class holds data by pairs of key and value, works like a map in read-only mode.
 */
public interface ReadonlyZeroMap extends ZeroCollection {

  boolean isNull(String key);

  boolean containsKey(String key);

  Set<String> getKeys();

  Set<String> getReadonlyKeys();

  Boolean getBoolean(String key);

  Byte getByte(String key);

  Short getShort(String key);

  Integer getInteger(String key);

  Long getLong(String key);

  Float getFloat(String key);

  Double getDouble(String key);

  String getString(String key);

  ZeroArray getZeroArray(String key);

  ZeroMap getZeroMap(String key);

  ZeroElement getZeroElement(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the key in the map of {@link Boolean} type
*/
  Collection<Boolean> getBooleanArray(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the key in a map of an array of binaries type
*/
  byte[] getByteArray(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the index in a collection of {@link Short} type
*/
  Collection<Short> getShortArray(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the key in  the map of {@link Integer} type
*/
  Collection<Integer> getIntegerArray(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the key in the map of {@link Long} type
*/
  Collection<Long> getLongArray(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the key in the map of {@link Float} type
*/
  Collection<Float> getFloatArray(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the key in the map of {@link Double} type
*/
  Collection<Double> getDoubleArray(String key);

/**
* Retrieves the data of element by its key in the map.
*
* @param key the key needs to be checked
* @return the value held at the key in the map of {@link String} type
*/
  Collection<String> getStringArray(String key);
}
