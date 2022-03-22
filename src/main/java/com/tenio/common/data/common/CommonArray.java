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
import java.util.ArrayList;

/**
 * This is an element array class holds data in a map. All message comes from other services
 * will be converted to this object. That helps normalize the way to communicate and easy to use.
 */
public final class CommonArray extends ArrayList<Object> implements Serializable {

  private static final long serialVersionUID = -5100842875580575666L;

  public CommonArray() {
  }

  public static CommonArray newInstance() {
    return new CommonArray();
  }

  public CommonArray put(Object value) {
    add(value);
    return this;
  }

  public double getDouble(int index) {
    return (double) get(index);
  }

  public float getFloat(int index) {
    return (float) get(index);
  }

  public long getLong(int index) {
    return (long) get(index);
  }

  public int getInt(int index) {
    return (int) get(index);
  }

  public boolean getBoolean(int index) {
    return (boolean) get(index);
  }

  public String getString(int index) {
    return (String) get(index);
  }

  public Object getObject(int index) {
    return get(index);
  }

  public CommonArray getCommonObjectArray(int index) {
    return (CommonArray) get(index);
  }
}
