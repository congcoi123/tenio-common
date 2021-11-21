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

package com.tenio.common.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Test;

public final class ByteUtilityTest {

  @Test
  void testIntToBytes() {
    byte[] actualIntToBytesResult = ByteUtility.intToBytes(42);
    assertEquals(4, actualIntToBytesResult.length);
    assertEquals((byte) 0, actualIntToBytesResult[0]);
    assertEquals((byte) 0, actualIntToBytesResult[1]);
    assertEquals((byte) 0, actualIntToBytesResult[2]);
    assertEquals('*', actualIntToBytesResult[3]);
  }

  @Test
  void testBytesToInt() throws UnsupportedEncodingException {
    assertEquals(1094795585, ByteUtility.bytesToInt("AAAAAAAA".getBytes("UTF-8")));
  }

  @Test
  void testShortToBytes() {
    byte[] actualShortToBytesResult = ByteUtility.shortToBytes((short) 1);
    assertEquals(2, actualShortToBytesResult.length);
    assertEquals((byte) 0, actualShortToBytesResult[0]);
    assertEquals((byte) 1, actualShortToBytesResult[1]);
  }

  @Test
  void testBytesToShort() throws UnsupportedEncodingException {
    assertEquals((short) 16705, ByteUtility.bytesToShort("AAAAAAAA".getBytes("UTF-8")));
  }

  @Test
  void testResizeBytesArray() throws UnsupportedEncodingException {
    byte[] actualResizeBytesArrayResult =
        ByteUtility.resizeBytesArray("AAAAAAAA".getBytes("UTF-8"), 1, 3);
    assertEquals(3, actualResizeBytesArrayResult.length);
    assertEquals('A', actualResizeBytesArrayResult[0]);
    assertEquals('A', actualResizeBytesArrayResult[1]);
    assertEquals('A', actualResizeBytesArrayResult[2]);
  }

  @Test
  void testResizeBytesArray2() throws UnsupportedEncodingException {
    assertThrows(NegativeArraySizeException.class,
        () -> ByteUtility.resizeBytesArray("AAAAAAAA".getBytes("UTF-8"), 1, -1));
  }
}
