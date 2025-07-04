/*
The MIT License

Copyright (c) 2016-2025 kong <congcoi123@gmail.com>

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
import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This is an element object class holds data in a map. All message comes from other services
 * will be converted to this object. That helps normalize the way to communicate and be easy to use.
 */
public final class MsgPackMap extends HashMap<String, Object>
    implements DataCollection, Serializable {

  @Serial
  private static final long serialVersionUID = 6697372748701824069L;

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
   * Determines whether the data can be fetched by its key in the map.
   *
   * @param key the {@link String} key needs to be checked
   * @return <code>true</code> if a value is available, otherwise <code>false</code>
   */
  public boolean contains(String key) {
    return containsKey(key);
  }

  /**
   * Determines if the value is null.
   *
   * @param key the {@link String} key needs to be checked
   * @return {@code true} if the value is {@code null}, otherwise {@code false}
   */
  public boolean isNull(String key) {
    return Objects.isNull(get(key));
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
   * @return the value converted in <code>byte</code> type fetched by its key in the map
   */
  public byte getByte(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>short</code> type fetched by its key in the map
   */
  public short getShort(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>integer</code> type fetched by its key in the map
   */
  public int getInteger(String key) {
    return (int) get(key);
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
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>double</code> type fetched by its key in the map
   */
  public double getDouble(String key) {
    throw new UnsupportedOperationException();
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
   * @return the value converted in <code>boolean[]</code> type fetched by its key in the map
   */
  public boolean[] getBooleanArray(String key) {
    return (boolean[]) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>byte[]</code> type fetched by its key in the map
   */
  public byte[] getByteArray(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>short[]</code> type fetched by its key in the map
   */
  public short[] getShortArray(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>integer[]</code> type fetched by its key in the map
   */
  public int[] getIntegerArray(String key) {
    return (int[]) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>float[]</code> type fetched by its key in the map
   */
  public float[] getFloatArray(String key) {
    return (float[]) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>long[]</code> type fetched by its key in the map
   */
  public long[] getLongArray(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in <code>double[]</code> type fetched by its key in the map
   */
  public double[] getDoubleArray(String key) {
    throw new UnsupportedOperationException();
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in {@code String[]} type fetched by its key in the map
   */
  public String[] getStringArray(String key) {
    return (String[]) get(key);
  }

  /**
   * Retrieves value in the map by its key.
   *
   * @param key the {@link String} key in the map
   * @return the value converted in {@link MsgPackMap} type fetched by its key in the map
   */
  public MsgPackMap getMsgPackMap(String key) {
    return (MsgPackMap) get(key);
  }
  /**
   * Adds a null data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @return the pointer of this instance
   */
  public MsgPackMap putNull(String key) {
    put(key, null);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code boolean} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putBoolean(String key, boolean value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code byte} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putByte(String key, byte value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code short} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putShort(String key, short value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code integer} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putInteger(String key, int value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code float} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putFloat(String key, float value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code long} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putLong(String key, long value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code double} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putDouble(String key, double value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@link String} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putString(String key, String value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code boolean[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putBooleanArray(String key, boolean[] value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code byte[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putByteArray(String key, byte[] value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code short[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putShortArray(String key, short[] value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code integer[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putIntegerArray(String key, int[] value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code float[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putFloatArray(String key, float[] value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code long[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putLongArray(String key, long[] value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code double[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putDoubleArray(String key, double[] value) {
    throw new UnsupportedOperationException();
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@code String[]} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putStringArray(String key, String[] value) {
    put(key, value);
    return this;
  }

  /**
   * Adds new data into the map with its key.
   *
   * @param key   the {@link String} key of data
   * @param value the {@link MsgPackMap} value needs to be inserted
   * @return the pointer of this instance
   */
  public MsgPackMap putMsgPackMap(String key, MsgPackMap value) {
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
