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

import com.tenio.common.utility.StringUtility;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This utility class helps you retrieve all classes from a java package.
 */
public final class ClassLoaderUtility {

  private ClassLoaderUtility() {
    throw new UnsupportedOperationException("This class did not support to create a new "
        + "instance");
  }

  /**
   * Scans a package to retrieve all classes inside it.
   *
   * @param packageName the package which contains loading classes
   * @return an array of classes or an empty array
   * @throws IOException when the class loader could not get the resources
   * @throws ClassNotFoundException when the target class was not found
   */
  public static Class<?>[] getClasses(String packageName)
      throws ClassNotFoundException, IOException {
    var classLoader = Thread.currentThread().getContextClassLoader();

    if (classLoader == null) {
      throw new NullPointerException("Could not find class loader");
    }

    var path = packageName.replace('.', '/');
    var resources = classLoader.getResources(path);
    var directories = new ArrayList<File>();

    while (resources.hasMoreElements()) {
      var resource = resources.nextElement();
      directories.add(new File(resource.getFile()));
    }

    var classes = new ArrayList<Class<?>>();
    for (var file : directories) {
      classes.addAll(findClasses(file, packageName));
    }

    return classes.toArray(new Class[classes.size()]);
  }

  private static List<Class<?>> findClasses(File directory, String packageName)
      throws ClassNotFoundException {
    var classes = new ArrayList<Class<?>>();

    if (!directory.exists()) {
      throw new ClassNotFoundException(String.format("The directory [%s] was not found",
          directory));
    }

    var files = directory.listFiles();
    for (var file : files) {
      if (file.isDirectory()) {
        if (!file.getName().contains(".")) {
          throw new IllegalArgumentException("Directory does not contain the separator");
        }
        classes.addAll(findClasses(file, StringUtility.strgen(packageName, ".", file.getName())));
      } else if (file.getName().endsWith(".class")) {
        String className = StringUtility.strgen(packageName, ".",
            file.getName().substring(0, file.getName().length() - 6));
        classes.add(Class.forName(className));
      }
    }
    return classes;
  }
}
