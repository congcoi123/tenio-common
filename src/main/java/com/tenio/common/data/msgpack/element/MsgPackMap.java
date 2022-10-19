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

package com.tenio.common.data.msgpack.element;

import com.tenio.common.data.DataCollection;
import com.tenio.common.data.msgpack.MsgPackUtility;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * This is an element object class holds data in a map. All message comes from other services
 * will be converted to this object. That helps normalize the way to communicate and be easy to use.
 */
public final class MsgPackMap extends HashMap<String, Object>
    implements DataCollection, Serializable {

  private static final long serialVersionUID = 8818783476027583633L;

  /**
   * Creates a new fresh instance.
   */
  public MsgPackMap() {
  }

  /**
   * Retrieves new instance of the class.
   *
   * @return an instance
   */
  public static MsgPackMap newInstance() {
    return new MsgPackMap();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>double</code> type fetched by its key in the map
   */
  public double getDouble(String key) {
    return (double) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>float</code> type fetched by its key in the map
   */
  public float getFloat(String key) {
    return (float) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>long</code> type fetched by its key in the map
   */
  public long getLong(String key) {
    return (long) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>integer</code> type fetched by its key in the map
   */
  public int getInt(String key) {
    return (int) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>boolean</code> type fetched by its key in the map
   */
  public boolean getBoolean(String key) {
    return (boolean) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in {@link String} type fetched by its key in the map
   */
  public String getString(String key) {
    return (String) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value in {@link Object} type fetched by its key in the map
   */
  public Object getObject(String key) {
    return get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in {@link MsgPackMap} type fetched by its key in the map
   */
  public MsgPackMap getCommonObject(String key) {
    return (MsgPackMap) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in {@link MsgPackArray} type fetched by its key in the map
   */
  public MsgPackArray getCommonObjectArray(String key) {
    return (MsgPackArray) get(key);
  }

  /**
   * Determines whether the data can be fetched by its key in the map.
   *
   * @param key the {@link String} key needs to be checked
   * @return <code>true</code> if a value is available, otherwise <code>false</code>
   */
  public boolean contains(String key) {
    return containsKey(key);
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@link Object} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap add(String key, Object value) {
    put(key, value);
    return this;
  }

  /**
   * Retrieves a new copy of the interior map.
   *
   * @return a new copy of the map in unmodified mode.
   */
  public Map<String, Object> getReadonlyMap() {
    return Map.copyOf(this);
  }

  @Override
  public byte[] toBinary() {
    return MsgPackUtility.serialize(this);
  }
}
