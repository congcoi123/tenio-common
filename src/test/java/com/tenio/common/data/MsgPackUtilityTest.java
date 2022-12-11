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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tenio.common.data.msgpack.MsgPackUtility;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For MsgPack Utility")
class MsgPackUtilityTest {

  @Test
  @DisplayName("Throw an exception when the class's instance is attempted creating")
  void createNewInstanceShouldThrowException() throws NoSuchMethodException {
    var constructor = MsgPackUtility.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    assertThrows(InvocationTargetException.class, () -> {
      constructor.setAccessible(true);
      constructor.newInstance();
    });
  }

  @Test
  @DisplayName("Checking whether binaries data is a collection should work")
  void itShouldReturnCorrectDataCollection() {
    var msgpackMap = MsgPackUtility.newMsgPackMap().putBoolean("a", true).putMsgPackArray("b",
        MsgPackUtility.newMsgPackArray().addBoolean(true));
    var checkMsgPackMap = MsgPackUtility.deserialize(msgpackMap.toBinary());
    assert checkMsgPackMap != null;
    assertEquals(checkMsgPackMap.toString(), msgpackMap.toString());
  }

  @Test
  @DisplayName("Allow adding and fetching primitive data to/from MsgPackArray")
  void primitiveDataInArrayShouldMatch() {
    var origin = MsgPackUtility.newMsgPackArray();
    origin.addBoolean(true).addInteger(1000).addFloat(101.1f).addString("msgpack")
        .addBoolean(false);
    var carry = MsgPackUtility.newMsgPackMap().putMsgPackArray("k", origin);
    var binary = carry.toBinary();
    var newOne = MsgPackUtility.deserialize(binary);
    assert newOne != null;

    assertAll("primitiveDataInArrayShouldMatch",
        () -> assertTrue(newOne.getMsgPackArray("k").getBoolean(0)),
        () -> assertEquals(newOne.getMsgPackArray("k").getInteger(1), 1000),
        () -> assertEquals(newOne.getMsgPackArray("k").getFloat(2), 101.1f),
        () -> assertEquals(newOne.getMsgPackArray("k").getString(3), "msgpack"),
        () -> assertFalse(newOne.getMsgPackArray("k").getBoolean(4))
    );
  }

  @Test
  @DisplayName("Allow adding and fetching MsgPackArray data to/from MsgPackArray")
  void zeroMapInArrayShouldMatch() {
    var origin = MsgPackUtility.newMsgPackArray();
    var msgPackArray = MsgPackUtility.newMsgPackArray();
    msgPackArray.addBoolean(true)
        .addInteger(100)
        .addString("msgpack");
    origin.addMsgPackArray(msgPackArray);
    var carry = MsgPackUtility.newMsgPackMap().putMsgPackArray("k", origin);
    var binary = carry.toBinary();
    var newOne = MsgPackUtility.deserialize(binary);
    assert newOne != null;

    assertEquals(carry.getMsgPackArray("k").toString(), newOne.getMsgPackArray("k").toString());
  }

  @Test
  @DisplayName("Allow adding and fetching data to/from MsgPackMap")
  void putDataInMapShouldMatch() {
    var origin = MsgPackUtility.newMsgPackMap();
    origin.putBoolean("b", true)
        .putInteger("i", 1000)
        .putFloat("f", 101.1f)
        .putString("s", "msgpack")
        .putMsgPackArray("ma", MsgPackUtility.newMsgPackArray().addBoolean(true))
        .putMsgPackMap("mm", MsgPackUtility.newMsgPackMap().putBoolean("b", true));
    var binary = origin.toBinary();
    var newOne = MsgPackUtility.deserialize(binary);
    assert newOne != null;

    assertAll("putDataInMapShouldMatch",
        () -> assertFalse(newOne.contains("out")),
        () -> assertTrue(newOne.getBoolean("b")),
        () -> assertEquals(newOne.getInteger("i"), 1000),
        () -> assertEquals(newOne.getFloat("f"), 101.1f),
        () -> assertEquals(newOne.getString("s"), "msgpack"),
        () -> assertEquals(newOne.getMsgPackArray("ma").toString(),
            MsgPackUtility.newMsgPackArray().addBoolean(true).toString()),
        () -> assertEquals(newOne.getMsgPackMap("mm").toString(),
            MsgPackUtility.newMsgPackMap().putBoolean("b", true).toString())
    );

    var readonlyMap = origin.getReadonlyMap();
    assertAll("readonlyDataInMapShouldMatch",
        () -> assertTrue((boolean) readonlyMap.get("b")),
        () -> assertEquals((int) readonlyMap.get("i"), 1000),
        () -> assertEquals((float) readonlyMap.get("f"), 101.1f),
        () -> assertEquals(readonlyMap.get("s"), "msgpack"),
        () -> assertEquals(newOne.get("ma").toString(),
            MsgPackUtility.newMsgPackArray().addBoolean(true).toString()),
        () -> assertEquals(newOne.get("mm").toString(),
            MsgPackUtility.newMsgPackMap().putBoolean("b", true).toString())
    );
  }
}
