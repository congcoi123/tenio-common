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

package com.tenio.common.data.msgpack.element;

import static org.junit.jupiter.api.Assertions.*;

import com.tenio.common.data.msgpack.MsgPackUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

@DisplayName("Unit Test Cases For MsgPackMap")
class MsgPackMapTest {

    @Test
    @DisplayName("newInstance should create a new empty instance")
    void newInstance_shouldCreateNewEmptyInstance() {
        // When
        MsgPackMap map = MsgPackMap.newInstance();
        
        // Then
        assertNotNull(map);
        assertTrue(map.isEmpty());
    }
    
    @Test
    @DisplayName("contains should return true when key exists")
    void contains_shouldReturnTrueWhenKeyExists() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        map.put("key", "value");
        
        // When/Then
        assertTrue(map.contains("key"));
        assertFalse(map.contains("nonexistent"));
    }
    
    @Test
    @DisplayName("isNull should return true when value is null")
    void isNull_shouldReturnTrueWhenValueIsNull() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        map.putNull("nullKey");
        map.put("nonNullKey", "value");
        
        // When/Then
        assertTrue(map.isNull("nullKey"));
        assertFalse(map.isNull("nonNullKey"));
        assertTrue(map.isNull("nonexistent")); // Non-existent keys return true for isNull
    }
    
    @Test
    @DisplayName("getBoolean should return boolean value")
    void getBoolean_shouldReturnBooleanValue() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        map.putBoolean("trueKey", true);
        map.putBoolean("falseKey", false);
        
        // When/Then
        assertTrue(map.getBoolean("trueKey"));
        assertFalse(map.getBoolean("falseKey"));
    }
    
    @Test
    @DisplayName("getInteger should return integer value")
    void getInteger_shouldReturnIntegerValue() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        int value = 100000;
        map.putInteger("key", value);
        
        // When/Then
        assertEquals(value, map.getInteger("key"));
    }
    
    @Test
    @DisplayName("getFloat should return float value")
    void getFloat_shouldReturnFloatValue() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        float value = 3.14f;
        map.putFloat("key", value);
        
        // When/Then
        assertEquals(value, map.getFloat("key"));
    }
    
    @Test
    @DisplayName("getString should return string value")
    void getString_shouldReturnStringValue() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        String value = "test string";
        map.putString("key", value);
        
        // When/Then
        assertEquals(value, map.getString("key"));
    }
    
    @Test
    @DisplayName("getBooleanArray should return boolean array")
    void getBooleanArray_shouldReturnBooleanArray() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        boolean[] value = {true, false, true};
        map.putBooleanArray("key", value);
        
        // When/Then
        assertArrayEquals(value, map.getBooleanArray("key"));
    }
    
    @Test
    @DisplayName("getIntegerArray should return integer array")
    void getIntegerArray_shouldReturnIntegerArray() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        int[] value = {100, 200, 300};
        map.putIntegerArray("key", value);
        
        // When/Then
        assertArrayEquals(value, map.getIntegerArray("key"));
    }
    
    @Test
    @DisplayName("getFloatArray should return float array")
    void getFloatArray_shouldReturnFloatArray() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        float[] value = {1.1f, 2.2f, 3.3f};
        map.putFloatArray("key", value);
        
        // When/Then
        assertArrayEquals(value, map.getFloatArray("key"));
    }
    
    @Test
    @DisplayName("getStringArray should return string array")
    void getStringArray_shouldReturnStringArray() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        String[] value = {"one", "two", "three"};
        map.putStringArray("key", value);
        
        // When/Then
        assertArrayEquals(value, map.getStringArray("key"));
    }
    
    @Test
    @DisplayName("getMsgPackMap should return nested MsgPackMap")
    void getMsgPackMap_shouldReturnNestedMsgPackMap() {
        // Given
        MsgPackMap nestedMap = MsgPackMap.newInstance();
        nestedMap.putString("nestedKey", "nestedValue");
        
        MsgPackMap map = MsgPackMap.newInstance();
        map.putMsgPackMap("key", nestedMap);
        
        // When
        MsgPackMap retrievedMap = map.getMsgPackMap("key");
        
        // Then
        assertNotNull(retrievedMap);
        assertEquals("nestedValue", retrievedMap.getString("nestedKey"));
    }
    
    @Test
    @DisplayName("putNull should set null value")
    void putNull_shouldSetNullValue() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        
        // When
        MsgPackMap result = map.putNull("key");
        
        // Then
        assertNull(map.get("key"));
        assertSame(map, result); // Verify method chaining
    }
    
    @Test
    @DisplayName("getReadonlyMap should return unmodifiable map")
    void getReadonlyMap_shouldReturnUnmodifiableMap() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        map.putString("key", "value");
        
        // When
        Map<String, Object> readonlyMap = map.getReadonlyMap();
        
        // Then
        assertNotNull(readonlyMap);
        assertEquals("value", readonlyMap.get("key"));
        
        // Verify it's unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> readonlyMap.put("newKey", "newValue"));
    }
    
    @Test
    @DisplayName("toBinary should serialize map to binary")
    void toBinary_shouldSerializeMapToBinary() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        map.putString("key", "value");
        
        // When
        byte[] binary = map.toBinary();
        
        // Then
        assertNotNull(binary);
        assertTrue(binary.length > 0);
        
        // Verify we can deserialize it back
        MsgPackMap deserializedMap = MsgPackUtility.deserialize(binary);
        assertEquals("value", deserializedMap.getString("key"));
    }
    
    @Test
    @DisplayName("put and get methods should handle supported types correctly")
    void putAndGet_shouldHandleSupportedTypesCorrectly() {
        // Given
        MsgPackMap map = MsgPackMap.newInstance();
        
        // When
        map.putNull("nullValue");
        map.putBoolean("booleanValue", true);
        map.putInteger("intValue", 30);
        map.putFloat("floatValue", 50.5f);
        map.putString("stringValue", "test");
        
        boolean[] boolArray = {true, false};
        map.putBooleanArray("boolArray", boolArray);
        
        int[] intArray = {100, 200, 300};
        map.putIntegerArray("intArray", intArray);
        
        float[] floatArray = {1.1f, 2.2f, 3.3f};
        map.putFloatArray("floatArray", floatArray);
        
        String[] stringArray = {"one", "two", "three"};
        map.putStringArray("stringArray", stringArray);
        
        MsgPackMap nestedMap = MsgPackMap.newInstance();
        nestedMap.putString("nestedKey", "nestedValue");
        map.putMsgPackMap("nestedMap", nestedMap);
        
        // Then
        assertNull(map.get("nullValue"));
        assertTrue(map.getBoolean("booleanValue"));
        assertEquals(30, map.getInteger("intValue"));
        assertEquals(50.5f, map.getFloat("floatValue"));
        assertEquals("test", map.getString("stringValue"));
        
        assertArrayEquals(boolArray, map.getBooleanArray("boolArray"));
        assertArrayEquals(intArray, map.getIntegerArray("intArray"));
        assertArrayEquals(floatArray, map.getFloatArray("floatArray"));
        assertArrayEquals(stringArray, map.getStringArray("stringArray"));
        
        MsgPackMap retrievedNestedMap = map.getMsgPackMap("nestedMap");
        assertNotNull(retrievedNestedMap);
        assertEquals("nestedValue", retrievedNestedMap.getString("nestedKey"));
    }
} 