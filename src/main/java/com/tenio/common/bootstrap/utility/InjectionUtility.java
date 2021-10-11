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

package com.tenio.common.bootstrap.utility;

import com.tenio.common.bootstrap.annotation.Autowired;
import com.tenio.common.bootstrap.annotation.AutowiredAcceptNull;
import com.tenio.common.bootstrap.annotation.AutowiredQualifier;
import com.tenio.common.bootstrap.injector.Injector;
import com.tenio.common.exception.NoImplementedClassFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class helps the process of creating <b>"bean"</b> instance by
 * {@link #autowire(Injector, Class, Object)} method.
 */
public final class InjectionUtility {

  private InjectionUtility() {
    throw new UnsupportedOperationException("This class did not support to create a new "
        + "instance");
  }

  /**
   * Assigns bean values to the corresponding fields in a class.
   *
   * @param injector the injector
   * @param clazz    the target class that holds declared bean fields
   * @param bean     the bean instance associated with the declared field
   * @throws IllegalArgumentException  related to illegal argument exception
   * @throws SecurityException         related to security exception
   * @throws NoSuchMethodException     caused by <b>getDeclaredConstructor()</b>
   * @throws InvocationTargetException caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws InstantiationException    caused by <b>getDeclaredConstructor().newInstance()</b>
   * @throws IllegalAccessException    caused by <b>getDeclaredConstructor().newInstance()</b>
   * @see Injector
   */
  public static void autowire(Injector injector, Class<?> clazz, Object bean)
      throws InstantiationException, IllegalAccessException, IllegalArgumentException,
      InvocationTargetException,
      NoSuchMethodException, SecurityException, ClassNotFoundException {
    var fields = findFields(clazz);
    for (var field : fields) {
      var qualifier = field.isAnnotationPresent(AutowiredQualifier.class)
          ? field.getAnnotation(AutowiredQualifier.class).value()
          : null;
      if (field.isAnnotationPresent(AutowiredAcceptNull.class)) {
        try {
          var fieldInstance =
              injector.getBeanInstance(field.getType(), field.getName(), qualifier);
          field.set(bean, fieldInstance);
          autowire(injector, fieldInstance.getClass(), fieldInstance);
        } catch (NoImplementedClassFoundException e) {
          // do nothing
        }
      } else {
        var fieldInstance =
            injector.getBeanInstance(field.getType(), field.getName(), qualifier);
        field.set(bean, fieldInstance);
        autowire(injector, fieldInstance.getClass(), fieldInstance);
      }
    }
  }

  /**
   * Retrieves all the fields having {@link Autowired} or {@link AutowiredAcceptNull}
   * annotation used while declaration.
   *
   * @param clazz a target class
   * @return a set of fields in the class
   */
  private static Set<Field> findFields(Class<?> clazz) {
    var fields = new HashSet<Field>();

    while (clazz != null) {
      for (var field : clazz.getDeclaredFields()) {
        if (field.isAnnotationPresent(Autowired.class)
            || field.isAnnotationPresent(AutowiredAcceptNull.class)) {
          field.setAccessible(true);
          fields.add(field);
        }
      }
      // make recursion
      clazz = clazz.getSuperclass();
    }

    return fields;
  }
}
