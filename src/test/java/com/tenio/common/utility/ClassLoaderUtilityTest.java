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

package com.tenio.common.utility;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tenio.common.custom.DisabledTestFindingSolution;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Unit Test Cases For Class Loader Utility")
class ClassLoaderUtilityTest {

  @Test
  @DisplayName("Throw an exception when the class's instance is attempted creating")
  void createNewInstanceShouldThrowException() throws NoSuchMethodException {
    var constructor = ClassLoaderUtility.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(constructor.getModifiers()));
    assertThrows(InvocationTargetException.class, () -> {
      constructor.setAccessible(true);
      constructor.newInstance();
    });
  }

  @Test
  @DisplayName("Scanning a package should return a list of classes inside it")
  void scanPackageShouldReturnListOfClasses() throws ClassNotFoundException {
    var listClasses = ClassLoaderUtility.getClasses("com.tenio.common.bootstrap.loader");
    assertAll("scanPackageShouldReturnListOfClasses",
        () -> assertEquals(3, listClasses.size()),
        () -> assertTrue(
            listClasses.stream().map(Class::getName).anyMatch(name -> name.equals(
                "com.tenio.common.bootstrap.loader.TestClassA"))),
        () -> assertTrue(
            listClasses.stream().map(Class::getName).anyMatch(name -> name.equals(
                "com.tenio.common.bootstrap.loader.TestClassB"))),
        () -> assertTrue(
            listClasses.stream().map(Class::getName).anyMatch(name -> name.equals(
                "com.tenio.common.bootstrap.loader.TestClassC")))
    );
  }

  @Test
  @DisplayName("Scanning a non-existed package should return an empty list of classes")
  void scanNonExistedPackageShouldReturnEmptyArray() throws ClassNotFoundException {
    var listClasses = ClassLoaderUtility.getClasses("com.tenio.common.bootstrap.null");
    assertTrue(listClasses.isEmpty());
  }

  @Test
  @DisplayName("To be able to scan a package of a jar file and return a list of classes inside it")
  void scanExternalLibraryShouldReturnListOfClasses() throws ClassNotFoundException {
    var listClasses = ClassLoaderUtility.getClasses("com.google.common.annotations");
    assertFalse(listClasses.isEmpty());
  }


  @DisabledTestFindingSolution
  @DisplayName("Attempt to recall the private constructor in order to create an instance should " +
      "throw an exception")
  void tryToReCreateClassLoaderShouldThrowException() {
  }

  @DisabledTestFindingSolution
  @DisplayName("Try to procedure throwable exception cases")
  void getClassesInInvalidWaysShouldThrowException() {
  }
}
