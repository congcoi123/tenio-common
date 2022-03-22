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

package com.tenio.common.data.common;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This is an element object class holds data in a map. All message comes from other services
 * will be converted to this object. That helps normalize the way to communicate and easy to use.
 */
public final class CommonMap extends HashMap<String, Object> implements Serializable {

  private static final long serialVersionUID = 8818783476027583633L;

  public CommonMap() {
  }

  public static CommonMap newInstance() {
    return new CommonMap();
  }

  public double getDouble(String key) {
    return (double) get(key);
  }

  public float getFloat(String key) {
    return (float) get(key);
  }

  public long getLong(String key) {
    return (long) get(key);
  }

  public int getInt(String key) {
    return (int) get(key);
  }

  public boolean getBoolean(String key) {
    return (boolean) get(key);
  }

  public String getString(String key) {
    return (String) get(key);
  }

  public Object getObject(String key) {
    return get(key);
  }

  public CommonMap getCommonObject(String key) {
    return (CommonMap) get(key);
  }

  public CommonArray getCommonObjectArray(String key) {
    return (CommonArray) get(key);
  }

  public boolean contains(String key) {
    return containsKey(key);
  }

  public CommonMap add(String key, Object value) {
    put(key, value);
    return this;
  }
}
