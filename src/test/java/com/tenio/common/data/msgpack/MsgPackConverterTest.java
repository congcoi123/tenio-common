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

import com.tenio.common.data.msgpack.element.MsgPackMap;
import com.tenio.common.exception.UnsupportedMsgPackDataTypeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageBufferPacker;

import java.io.IOException;

@DisplayName("Unit Test Cases For MsgPackConverter")
class MsgPackConverterTest {

    @Test
    @DisplayName("pack and unpack should handle primitive types correctly")
    void packAndUnpack_shouldHandlePrimitiveTypesCorrectly() {
        // Given
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        originalMap.putNull("nullValue");
        originalMap.putBoolean("booleanValue", true);
        originalMap.putInteger("intValue", 30);
        originalMap.putFloat("floatValue", 40.5f);
        originalMap.putString("stringValue", "test");

        // When
        byte[] packed = MsgPackUtility.serialize(originalMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);

        // Then
        assertNull(unpacked.get("nullValue"));
        assertEquals(true, unpacked.getBoolean("booleanValue"));
        assertEquals(30, unpacked.getInteger("intValue"));
        assertEquals(40.5f, unpacked.getFloat("floatValue"));
        assertEquals("test", unpacked.getString("stringValue"));
    }

    @Test
    @DisplayName("pack and unpack should handle array types correctly")
    void packAndUnpack_shouldHandleArrayTypesCorrectly() {
        // Given
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        
        boolean[] boolArray = {true, false, true};
        originalMap.putBooleanArray("boolArray", boolArray);
        
        int[] intArray = {100, 200, 300};
        originalMap.putIntegerArray("intArray", intArray);
        
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        originalMap.putFloatArray("floatArray", floatArray);
        
        String[] stringArray = {"one", "two", "three"};
        originalMap.putStringArray("stringArray", stringArray);

        // When
        byte[] packed = MsgPackUtility.serialize(originalMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);

        // Then
        assertArrayEquals(boolArray, unpacked.getBooleanArray("boolArray"));
        assertArrayEquals(intArray, unpacked.getIntegerArray("intArray"));
        assertArrayEquals(floatArray, unpacked.getFloatArray("floatArray"));
        assertArrayEquals(stringArray, unpacked.getStringArray("stringArray"));
    }

    @Test
    @DisplayName("pack and unpack should handle nested maps correctly")
    void packAndUnpack_shouldHandleNestedMapsCorrectly() {
        // Given
        MsgPackMap innerMap = MsgPackUtility.newMsgPackMap();
        innerMap.putString("innerKey", "innerValue");
        
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        originalMap.putMsgPackMap("nestedMap", innerMap);

        // When
        byte[] packed = MsgPackUtility.serialize(originalMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);

        // Then
        MsgPackMap retrievedInnerMap = unpacked.getMsgPackMap("nestedMap");
        assertNotNull(retrievedInnerMap);
        assertEquals("innerValue", retrievedInnerMap.getString("innerKey"));
    }

    @Test
    @DisplayName("unpack should handle invalid data gracefully")
    void unpack_shouldHandleInvalidDataGracefully() {
        // Given
        byte[] invalidData = {0x01, 0x02, 0x03}; // Not valid MessagePack format

        // When/Then
        assertThrows(RuntimeException.class, () -> MsgPackUtility.deserialize(invalidData));
    }

    @Test
    @DisplayName("pack and unpack should handle multiple nested maps correctly")
    void packAndUnpack_shouldHandleMultipleNestedMapsCorrectly() {
        // Given
        MsgPackMap innerMap1 = MsgPackUtility.newMsgPackMap();
        innerMap1.putString("innerKey1", "innerValue1");
        
        MsgPackMap innerMap2 = MsgPackUtility.newMsgPackMap();
        innerMap2.putString("innerKey2", "innerValue2");
        innerMap2.putMsgPackMap("nestedInnerMap", innerMap1);
        
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        originalMap.putMsgPackMap("level1", innerMap2);

        // When
        byte[] packed = MsgPackUtility.serialize(originalMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);

        // Then
        MsgPackMap retrievedLevel1 = unpacked.getMsgPackMap("level1");
        assertNotNull(retrievedLevel1);
        assertEquals("innerValue2", retrievedLevel1.getString("innerKey2"));
        
        MsgPackMap retrievedNestedInner = retrievedLevel1.getMsgPackMap("nestedInnerMap");
        assertNotNull(retrievedNestedInner);
        assertEquals("innerValue1", retrievedNestedInner.getString("innerKey1"));
    }

    @Test
    @DisplayName("serialize should throw exception for unsupported data type")
    void serialize_shouldThrowExceptionForUnsupportedDataType() {
        // Given
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        // Put an unsupported object type
        originalMap.put("unsupported", new Object());

        // When/Then
        assertThrows(UnsupportedMsgPackDataTypeException.class, () -> MsgPackUtility.serialize(originalMap));
    }

    @Test
    @DisplayName("deserialize should handle empty data correctly")
    void deserialize_shouldHandleEmptyDataCorrectly() {
        // Given
        MsgPackMap emptyMap = MsgPackUtility.newMsgPackMap();
        
        // When
        byte[] packed = MsgPackUtility.serialize(emptyMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);
        
        // Then
        assertTrue(unpacked.isEmpty());
    }

    @Test
    @DisplayName("newMsgPackMap should create a new empty instance")
    void newMsgPackMap_shouldCreateNewEmptyInstance() {
        // When
        MsgPackMap map = MsgPackUtility.newMsgPackMap();
        
        // Then
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }
    
    @Test
    @DisplayName("pack should handle supported numeric types correctly")
    void pack_shouldHandleSupportedNumericTypesCorrectly() throws IOException {
        // Given
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        originalMap.putInteger("intValue", 30);
        originalMap.putFloat("floatValue", 50.5f);
        
        // When
        byte[] packed = MsgPackUtility.serialize(originalMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);
        
        // Then
        assertEquals(30, unpacked.getInteger("intValue"));
        assertEquals(50.5f, unpacked.getFloat("floatValue"));
    }
    
    @Test
    @DisplayName("pack should handle supported array types correctly")
    void pack_shouldHandleSupportedArrayTypesCorrectly() throws IOException {
        // Given
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        
        int[] intArray = {100, 200, 300};
        originalMap.putIntegerArray("intArray", intArray);
        
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        originalMap.putFloatArray("floatArray", floatArray);
        
        // When
        byte[] packed = MsgPackUtility.serialize(originalMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);
        
        // Then
        assertArrayEquals(intArray, unpacked.getIntegerArray("intArray"));
        assertArrayEquals(floatArray, unpacked.getFloatArray("floatArray"));
    }
    
    @Test
    @DisplayName("unpack should handle all value types from MessagePack")
    void unpack_shouldHandleAllValueTypesFromMessagePack() throws IOException {
        // Given
        MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();
        
        // Create a map with various types
        packer.packMapHeader(8);
        
        // Add null value
        packer.packString("nullValue");
        packer.packNil();
        
        // Add boolean value
        packer.packString("boolValue");
        packer.packBoolean(true);
        
        // Add integer value
        packer.packString("intValue");
        packer.packInt(42);
        
        // Add float value
        packer.packString("floatValue");
        packer.packFloat(3.14f);
        
        // Add string value
        packer.packString("stringValue");
        packer.packString("test");
        
        // Add boolean array
        packer.packString("boolArray");
        packer.packArrayHeader(2);
        packer.packBoolean(true);
        packer.packBoolean(false);
        
        // Add integer array
        packer.packString("intArray");
        packer.packArrayHeader(2);
        packer.packInt(1);
        packer.packInt(2);
        
        // Add nested map
        packer.packString("mapValue");
        packer.packMapHeader(1);
        packer.packString("nestedKey");
        packer.packString("nestedValue");
        
        // When
        byte[] packed = packer.toByteArray();
        MsgPackMap result = MsgPackUtility.deserialize(packed);
        
        // Then
        assertNull(result.get("nullValue"));
        assertEquals(true, result.getBoolean("boolValue"));
        assertEquals(42, result.getInteger("intValue"));
        assertEquals(3.14f, result.getFloat("floatValue"));
        assertEquals("test", result.getString("stringValue"));
        
        boolean[] expectedBoolArray = {true, false};
        assertArrayEquals(expectedBoolArray, result.getBooleanArray("boolArray"));
        
        int[] expectedIntArray = {1, 2};
        assertArrayEquals(expectedIntArray, result.getIntegerArray("intArray"));
        
        MsgPackMap nestedResult = result.getMsgPackMap("mapValue");
        assertNotNull(nestedResult);
        assertEquals("nestedValue", nestedResult.getString("nestedKey"));
    }
    
    @Test
    @DisplayName("unpack should handle complex nested structures")
    void unpack_shouldHandleComplexNestedStructures() throws IOException {
        // Given
        MsgPackMap level3Map = MsgPackUtility.newMsgPackMap();
        level3Map.putString("level3Key", "level3Value");
        
        MsgPackMap level2Map = MsgPackUtility.newMsgPackMap();
        level2Map.putString("level2Key", "level2Value");
        level2Map.putMsgPackMap("level3", level3Map);
        
        MsgPackMap level1Map = MsgPackUtility.newMsgPackMap();
        level1Map.putString("level1Key", "level1Value");
        level1Map.putMsgPackMap("level2", level2Map);
        
        MsgPackMap rootMap = MsgPackUtility.newMsgPackMap();
        rootMap.putString("rootKey", "rootValue");
        rootMap.putMsgPackMap("level1", level1Map);
        
        // When
        byte[] packed = MsgPackUtility.serialize(rootMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);
        
        // Then
        assertEquals("rootValue", unpacked.getString("rootKey"));
        
        MsgPackMap level1Result = unpacked.getMsgPackMap("level1");
        assertNotNull(level1Result);
        assertEquals("level1Value", level1Result.getString("level1Key"));
        
        MsgPackMap level2Result = level1Result.getMsgPackMap("level2");
        assertNotNull(level2Result);
        assertEquals("level2Value", level2Result.getString("level2Key"));
        
        MsgPackMap level3Result = level2Result.getMsgPackMap("level3");
        assertNotNull(level3Result);
        assertEquals("level3Value", level3Result.getString("level3Key"));
    }
    
    @Test
    @DisplayName("pack should handle empty arrays correctly")
    void pack_shouldHandleEmptyArrays() throws IOException {
        // Given
        MsgPackMap originalMap = MsgPackUtility.newMsgPackMap();
        originalMap.putBooleanArray("emptyBoolArray", new boolean[0]);
        originalMap.putIntegerArray("emptyIntArray", new int[0]);
        originalMap.putFloatArray("emptyFloatArray", new float[0]);
        originalMap.putStringArray("emptyStringArray", new String[0]);

        // When
        byte[] packed = MsgPackUtility.serialize(originalMap);
        MsgPackMap unpacked = MsgPackUtility.deserialize(packed);

        // Then
        assertTrue(unpacked.contains("emptyBoolArray"));
        assertTrue(unpacked.contains("emptyIntArray")); 
        assertTrue(unpacked.contains("emptyFloatArray"));
        assertTrue(unpacked.contains("emptyStringArray"));

        assertEquals(0, unpacked.getBooleanArray("emptyBoolArray").length);
        assertEquals(0, unpacked.getIntegerArray("emptyIntArray").length);
        assertEquals(0, unpacked.getFloatArray("emptyFloatArray").length);
        assertEquals(0, unpacked.getStringArray("emptyStringArray").length);
    }
} 