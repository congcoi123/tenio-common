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

package com.tenio.common.configuration;

import java.util.Map;

class DefaultConfiguration extends CommonConfiguration {

  public DummyObject dummyObject = new DummyObject();

  @Override
  protected void loadInternal(String file) throws ConfigurationException {
    if (file.equals("dummy")) {
      // Use hardcoded values for dummy test
      push(DefaultConfigurationType.BOOLEAN, "true");
      push(DefaultConfigurationType.FLOAT, "100F");
      push(DefaultConfigurationType.INTEGER, "99");
      push(DefaultConfigurationType.STRING, "test");
      push(DefaultConfigurationType.STRING, "test overridden");
      push(DefaultConfigurationType.NOT_DEFINED, "-1");
      push(DefaultConfigurationType.OBJECT, dummyObject);
    } else {
      // Load from properties file
      java.util.Properties props = new java.util.Properties();
      try (java.io.FileInputStream fis = new java.io.FileInputStream(file)) {
        props.load(fis);
        for (String key : props.stringPropertyNames()) {
          try {
            DefaultConfigurationType type = DefaultConfigurationType.valueOf(key);
            // Special handling for OBJECT property
            if (type == DefaultConfigurationType.OBJECT) {
              push(type, dummyObject);
            } else {
              push(type, props.getProperty(key));
            }
          } catch (IllegalArgumentException e) {
            // Skip unknown properties
          }
        }
        
        // Always add the dummy object if it's not already added
        if (!isDefined(DefaultConfigurationType.OBJECT)) {
          push(DefaultConfigurationType.OBJECT, dummyObject);
        }
      } catch (java.io.IOException e) {
        throw new ConfigurationException("Failed to load configuration file: " + file,
            e, null, ConfigurationException.ErrorType.LOAD_ERROR);
      }
    }
  }

  @Override
  protected ConfigurationType getConfigurationType(String name) {
    try {
      return DefaultConfigurationType.valueOf(name);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  @Override
  protected void extend(Map<String, String> extProperties) {
    // do nothing
  }

  @Override
  protected ConfigurationType[] getConfigurationTypes() {
    return DefaultConfigurationType.values();
  }
}
