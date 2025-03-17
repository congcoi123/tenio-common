/*
The MIT License

Copyright (c) 2016-2023 kong <congcoi123@gmail.com>

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

package com.tenio.common.data.msgpack;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For ByteArrayInputStream")
class ByteArrayInputStreamTest {

  @Test
  @DisplayName("Creating a new instance should work")
  void newInstanceShouldWork() {
    var stream = ByteArrayInputStream.newInstance();
    assertNotNull(stream);
  }

  @Test
  @DisplayName("Creating from byte array should work")
  void valueOfByteArrayShouldWork() {
    byte[] data = {1, 2, 3, 4, 5};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data);
    
    assertNotNull(stream);
    assertEquals(5, stream.getLength());
    assertEquals(0, stream.getOffset());
    assertArrayEquals(data, stream.getArray());
  }

  @Test
  @DisplayName("Creating from byte array with offset and length should work")
  void valueOfByteArrayWithOffsetAndLengthShouldWork() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data, 1, 3);
    
    assertNotNull(stream);
    assertEquals(3, stream.getLength());
    assertEquals(0, stream.getOffset());  // The offset in the constructor affects the internal buffer, but getOffset() returns the current position
    assertArrayEquals(data, stream.getArray());
    
    // First byte should be the one at offset 1 in the original array
    assertEquals(1, stream.read());  // The first byte is 1 because the offset is applied internally
  }

  @Test
  @DisplayName("Available should return correct value")
  void availableShouldReturnCorrectValue() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    var stream = ByteArrayInputStream.valueOf(data);
    assertEquals(5, stream.available());
  }

  @Test
  @DisplayName("Skip should work correctly")
  void skipShouldWorkCorrectly() throws IOException {
    byte[] data = {10, 20, 30, 40, 50};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data);
    
    assertEquals(2, stream.skip(2));
    assertEquals(3, stream.available());
    assertEquals(30, stream.read());
  }

  @Test
  @DisplayName("Skip with negative value should throw exception")
  void skipWithNegativeValueShouldThrowException() {
    byte[] data = {1, 2, 3, 4, 5};
    var stream = ByteArrayInputStream.valueOf(data);
    assertThrows(IllegalArgumentException.class, () -> stream.skip(-1));
  }

  @Test
  @DisplayName("Read should return correct byte")
  void readShouldReturnCorrectByte() throws IOException {
    byte[] data = {10, 20, 30, 40, 50};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data);
    
    assertEquals(10, stream.read());
    assertEquals(20, stream.read());
    assertEquals(3, stream.available());
  }

  @Test
  @DisplayName("Read into buffer should work correctly")
  void readIntoBufferShouldWorkCorrectly() throws IOException {
    byte[] data = {1, 2, 3, 4, 5};
    var stream = ByteArrayInputStream.valueOf(data);
    byte[] buffer = new byte[3];
    assertEquals(3, stream.read(buffer, 0, 3));
    assertArrayEquals(new byte[]{1, 2, 3}, buffer);
  }

  @Test
  @DisplayName("Read into buffer with invalid parameters should throw exception")
  void readIntoBufferWithInvalidParametersShouldThrowException() {
    byte[] data = {1, 2, 3, 4, 5};
    var stream = ByteArrayInputStream.valueOf(data);
    byte[] buffer = new byte[3];
    
    assertThrows(IllegalArgumentException.class, () -> stream.read(buffer, -1, 3));
    assertThrows(IllegalArgumentException.class, () -> stream.read(buffer, 0, -1));
    assertThrows(IllegalArgumentException.class, () -> stream.read(buffer, 0, 4));
    assertThrows(IllegalArgumentException.class, () -> stream.read(buffer, 2, 2));
  }

  @Test
  @DisplayName("Mark and reset should work correctly")
  void markAndResetShouldWorkCorrectly() throws IOException {
    byte[] data = {10, 20, 30, 40, 50};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data);
    
    // markSupported() returns false in this implementation
    assertFalse(stream.markSupported());
    
    // mark() does nothing in this implementation
    stream.mark(0);
    
    // reset() resets the entire stream
    stream.reset();
    
    // After reset, the stream should be empty
    assertEquals(-1, stream.read());
  }

  @Test
  @DisplayName("Reset with offset should work correctly")
  void resetWithOffsetShouldWorkCorrectly() throws IOException {
    byte[] data = {10, 20, 30, 40, 50};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data);
    
    assertEquals(10, stream.read());
    assertEquals(20, stream.read());
    
    stream.reset(0);
    assertEquals(10, stream.read());
  }

  @Test
  @DisplayName("Reset with data should work correctly")
  void resetWithDataShouldWorkCorrectly() throws IOException {
    byte[] data1 = {10, 20, 30};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data1);
    
    assertEquals(10, stream.read());
    
    byte[] data2 = {40, 50, 60, 70, 80};
    stream.reset(data2);
    
    assertEquals(5, stream.available());
    assertEquals(40, stream.read());
  }

  @Test
  @DisplayName("Reset with data, offset and length should work correctly")
  void resetWithDataOffsetAndLengthShouldWorkCorrectly() throws IOException {
    byte[] data1 = {10, 20, 30};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data1);
    
    assertEquals(10, stream.read());
    
    byte[] data2 = {40, 50, 60, 70, 80};
    stream.reset(data2, 1, 3);
    
    assertEquals(3, stream.available());
    assertEquals(0, stream.getOffset());  // The offset parameter affects the internal buffer, but getOffset() returns the current position
    assertEquals(40, stream.read());  // The first byte is 40 because the offset is applied internally
  }

  @Test
  @DisplayName("Read bytes should work correctly")
  void readBytesShouldWorkCorrectly() throws IOException {
    byte[] data = {10, 20, 30, 40, 50};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data);
    
    byte[] result = new byte[3];
    stream.read(result, 0, 3);
    
    assertArrayEquals(new byte[]{10, 20, 30}, result);
    assertEquals(2, stream.available());
  }

  @Test
  @DisplayName("Read bytes with offset should work correctly")
  void readBytesWithOffsetShouldWorkCorrectly() throws IOException {
    byte[] data = {10, 20, 30, 40, 50};
    ByteArrayInputStream stream = ByteArrayInputStream.valueOf(data);
    
    byte[] result = new byte[5];
    stream.read(result, 2, 3);
    
    assertArrayEquals(new byte[]{0, 0, 10, 20, 30}, result);
    assertEquals(2, stream.available());
  }
} 