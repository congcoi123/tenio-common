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

package com.tenio.common.data.zero.implement;

import com.tenio.common.data.zero.ZeroElement;
import com.tenio.common.data.zero.ZeroType;

/**
 * This class holds a relationship between a self-definition data type and its value.
 */
public class ZeroElementImpl implements ZeroElement {

  private static final long serialVersionUID = -3839072498152322959L;

  private final ZeroType type;
  private final Object data;

  /**
   * Constructor.
   *
   * @param type {@link ZeroType}
   * @param data {@link Object}
   */
  public ZeroElementImpl(ZeroType type, Object data) {
    this.type = type;
    this.data = data;
  }

  @Override
  public ZeroType getType() {
    return type;
  }

  @Override
  public Object getData() {
    return data;
  }

  @Override
  public String toString() {
    return "ZeroElement{" +
        "type=" + type +
        ", data=" + data +
        '}';
  }
}
