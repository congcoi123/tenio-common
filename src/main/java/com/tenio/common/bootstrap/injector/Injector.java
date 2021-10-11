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

package com.tenio.common.bootstrap.injector;

import com.tenio.common.bootstrap.annotation.Component;
import com.tenio.common.bootstrap.utility.ClassLoaderUtility;
import com.tenio.common.bootstrap.utility.InjectionUtility;
import com.tenio.common.exception.MultipleImplementedClassForInterfaceException;
import com.tenio.common.exception.NoImplementedClassFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.reflections.Reflections;

/**
 * This class helps us create and retrieve <b>"beans"</b> (the class instances).
 */
public final class Injector {

  private static final Injector instance = new Injector();

  /**
   * With the key is the interface and the value holds an implemented class.
   */
  private final Map<Class<?>, Class<?>> classesMap;
  /**
   * With the key is the interface implemented class and the value holds its instance.
   */
  private final Map<Class<?>, Object> classBeansMap;

  private Injector() {
    if (instance != null) {
      throw new ExceptionInInitializerError("Could not re-create the class instance");
    }

    classesMap = new HashMap<Class<?>, Class<?>>();
    classBeansMap = new HashMap<Class<?>, Object>();
  }

  public static Injector newInstance() {
    return instance;
  }

  /**
   * Scans all input packages to create beans and put them into map.
   *
   * @param entryClass the root class which should be located in the parent package of other
   *                   class' packages
   * @param packages   free to define the scanning packages by their names
   * @throws IOException               related to input/output exception
   * @throws IllegalArgumentException  related to illegal argument exception
   * @throws SecurityException         related to security exception
   * @throws ClassNotFoundException    caused by <b>getImplementedClass()</b>
   * @throws NoSuchMethodException     caused by <b>getDeclaredConstructor()</b>
   * @throws InvocationTargetException caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws InstantiationException    caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws IllegalAccessException    caused by <b>getDeclaredConstructor().newInstance()</b>
   */
  public void scanPackages(Class<?> entryClass, String... packages)
      throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
      SecurityException {
    // fetches all classes that are in the same package as the root one
    var classes = ClassLoaderUtility.getClasses(entryClass.getPackage().getName());
    // declares a reflection object based on the package of root class
    var reflections = new Reflections(entryClass.getPackage().getName());

    for (var pack : packages) {
      var packageClasses = ClassLoaderUtility.getClasses(pack);
      classes = Stream.concat(Arrays.stream(classes), Arrays.stream(packageClasses))
          .toArray(Class<?>[]::new);

      var reflectionPackage = new Reflections(pack);
      reflections.merge(reflectionPackage);
    }

    // the implemented class is defined with the "Component" annotation declared inside it
    // in case you need more annotations with the same effect with this one, you should put them
    // in here
    var implementedClasses = reflections.getTypesAnnotatedWith(Component.class);

    // scans all interfaces with their implemented classes
    for (var implementedClass : implementedClasses) {
      var classInterfaces = implementedClass.getInterfaces();
      // in case the class has not implemented any interfaces, it still can be created, so put
      // the class into the map
      if (classInterfaces.length == 0) {
        classesMap.put(implementedClass, implementedClass);
      } else {
        // normal case, put the pair of class and interface
        // the interface will be used to retrieved back the corresponding class when we want to
        // create a bean by its interface
        for (var classInterface : classInterfaces) {
          classesMap.put(implementedClass, classInterface);
        }
      }
    }

    // create beans (class instances) based on annotations
    for (var clazz : classes) {
      // in case you need to create a bean with another annotation, put it in here
      // but notices to put it in "implementedClasses" first
      if (clazz.isAnnotationPresent(Component.class)) {
        var bean = clazz.getDeclaredConstructor().newInstance();
        classBeansMap.put(clazz, bean);
        // recursively create field instance for this class instance
        InjectionUtility.autowire(this, clazz, bean);
      }
    }
  }

  /**
   * Gets a bean by its declared interface.
   *
   * @param clazz the interface class
   * @param <T>   the returned type of interface
   * @return a bean (an instance of the interface
   * @throws ClassNotFoundException    caused by <b>getImplementedClass()</b>
   * @throws NoSuchMethodException     caused by <b>getDeclaredConstructor()</b>
   * @throws InvocationTargetException caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws InstantiationException    caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws IllegalAccessException    caused by <b>getDeclaredConstructor().newInstance()</b>
   */
  public <T> T getBean(Class<T> clazz)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    return (T) getBeanInstance(clazz, null, null);
  }

  /**
   * Retrieves a bean which is declared in a class's field and put it in map of beans as well.
   *
   * @param classInterface The interface using to create a new bean
   * @param fieldName      The name of field that holds a reference of a bean in a class
   * @param qualifier      To differentiate which implemented class should be used to create the
   *                       bean
   * @param <T>            the type of implemented class
   * @return a bean object, an instance of the implemented class
   * @throws ClassNotFoundException    caused by <b>getImplementedClass()</b>
   * @throws NoSuchMethodException     caused by <b>getDeclaredConstructor()</b>
   * @throws InvocationTargetException caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws InstantiationException    caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws IllegalAccessException    caused by <b>getDeclaredConstructor().newInstance()</b>
   */
  public <T> Object getBeanInstance(Class<T> classInterface, String fieldName, String qualifier)
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
      InstantiationException, IllegalAccessException {
    var implementedClass = getImplementedClass(classInterface, fieldName, qualifier);

    synchronized (classBeansMap) {
      if (classBeansMap.containsKey(implementedClass)) {
        return classBeansMap.get(implementedClass);
      }

      var bean = implementedClass.getDeclaredConstructor().newInstance();
      classBeansMap.put(implementedClass, bean);
      return bean;
    }
  }

  private Class<?> getImplementedClass(Class<?> classInterface, String fieldName,
                                       String qualifier) throws ClassNotFoundException {
    var implementedClasses = classesMap.entrySet().stream()
        .filter(entry -> entry.getValue() == classInterface).collect(Collectors.toSet());

    if (implementedClasses == null || implementedClasses.isEmpty()) {
      throw new NoImplementedClassFoundException(classInterface);
    } else if (implementedClasses.size() == 1) {
      // just only one implemented class for the interface
      var optional = implementedClasses.stream().findFirst();
      return optional.map(Entry::getKey).orElseThrow(ClassNotFoundException::new);
    } else if (implementedClasses.size() > 1) {
      // multiple implemented class from the interface, need to be selected by
      // "qualifier" value
      final var findBy =
          (qualifier == null || qualifier.trim().length() == 0) ? fieldName : qualifier;
      var optional = implementedClasses.stream()
          .filter(entry -> entry.getKey().getSimpleName().equalsIgnoreCase(findBy)).findAny();
      // in case of could not find an appropriately single instance, so throw an exception
      return optional.map(Entry::getKey)
          .orElseThrow(() -> new MultipleImplementedClassForInterfaceException(classInterface));
    }

    return null;
  }
}
