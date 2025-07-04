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

package com.tenio.common.exception;

import com.tenio.common.data.msgpack.MsgPackUtility;
import java.io.Serial;

/**
 * This exception would be thrown when you try to use an unsupported data type with MsgPack.
 *
 * @see MsgPackUtility
 */
public final class UnsupportedMsgPackDataTypeException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -814968367492853470L;

  /**
   * Constructor.
   */
  public UnsupportedMsgPackDataTypeException() {
    super("The data type is not supported. Please check out this available list: null, boolean, byte, short, int, " +
        "float, long, double, String, boolean[], byte[], short[], int[], float[], long[], double[], String[].");
  }
}
