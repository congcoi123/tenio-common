package com.tenio.common.data;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tenio.common.data.implement.ZeroArrayImpl;
import com.tenio.common.data.utility.ZeroDataSerializerUtility;
import org.junit.jupiter.api.Test;

public final class ZeroDataTest {

  @Test
  public void primitiveDataInArrayShouldMatch() {
    var origin = ZeroArrayImpl.newInstance();
    origin.addBoolean(true).addShort((short) 11).addInteger(1000).addFloat(101.1f).addLong(1000L)
        .addDouble(1010101.101);
    var binary = origin.toBinary();
    var newOne = ZeroDataSerializerUtility.binaryToArray(binary);

    assertAll("primitiveDataInArrayShouldMatch",
        () -> assertTrue(newOne.getBoolean(0)),
        () -> assertEquals(newOne.getShort(1), (short) 11),
        () -> assertEquals(newOne.getInteger(2), 1000),
        () -> assertEquals(newOne.getFloat(3), 101.1f),
        () -> assertEquals(newOne.getLong(4), 1000L),
        () -> assertEquals(newOne.getDouble(5), 1010101.101)
    );
  }
}
