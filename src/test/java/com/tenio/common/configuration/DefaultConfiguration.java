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
      push(DefaultConfigurationType.BOOLEAN, true);
      push(DefaultConfigurationType.FLOAT, 100.0f);
      push(DefaultConfigurationType.INTEGER, 99);
      push(DefaultConfigurationType.STRING, "test");
      push(DefaultConfigurationType.STRING, "test overridden");
      push(DefaultConfigurationType.NOT_DEFINED, -1);
      push(DefaultConfigurationType.OBJECT, dummyObject);
    } else {
      // Load from XML file
      try {
        javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(new java.io.File(file));
        doc.getDocumentElement().normalize();
        
        org.w3c.dom.Element root = doc.getDocumentElement();
        org.w3c.dom.NodeList properties = root.getChildNodes();
        
        for (int i = 0; i < properties.getLength(); i++) {
          org.w3c.dom.Node node = properties.item(i);
          if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
            org.w3c.dom.Element element = (org.w3c.dom.Element) node;
            String key = element.getNodeName();
            String value = element.getTextContent();
            
            try {
              DefaultConfigurationType type = DefaultConfigurationType.valueOf(key);
              // Special handling for OBJECT property
              if (type == DefaultConfigurationType.OBJECT) {
                push(type, dummyObject);
              } else if (type.getValueType() == Boolean.class) {
                push(type, Boolean.parseBoolean(value));
              } else if (type.getValueType() == Integer.class) {
                push(type, Integer.parseInt(value));
              } else if (type.getValueType() == Float.class) {
                push(type, Float.parseFloat(value));
              } else {
                push(type, value);
              }
            } catch (IllegalArgumentException e) {
              // Skip unknown properties
            }
          }
        }
        
        // Always add the dummy object if it's not already added
        if (!isDefined(DefaultConfigurationType.OBJECT)) {
          push(DefaultConfigurationType.OBJECT, dummyObject);
        }
      } catch (Exception e) {
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

  @Override
  protected void loadInternalToMap(String file, Map<ConfigurationType, Object> configMap) throws ConfigurationException {
    if (file.equals("dummy")) {
      // Use hardcoded values for dummy test
      configMap.put(DefaultConfigurationType.BOOLEAN, true);
      configMap.put(DefaultConfigurationType.FLOAT, 100.0f);
      configMap.put(DefaultConfigurationType.INTEGER, 99);
      configMap.put(DefaultConfigurationType.STRING, "test");
      configMap.put(DefaultConfigurationType.NOT_DEFINED, -1);
      configMap.put(DefaultConfigurationType.OBJECT, dummyObject);
    } else {
      // Load from XML file
      try {
        javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        org.w3c.dom.Document doc = dBuilder.parse(new java.io.File(file));
        doc.getDocumentElement().normalize();
        
        org.w3c.dom.Element root = doc.getDocumentElement();
        org.w3c.dom.NodeList properties = root.getChildNodes();
        
        for (int i = 0; i < properties.getLength(); i++) {
          org.w3c.dom.Node node = properties.item(i);
          if (node.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
            org.w3c.dom.Element element = (org.w3c.dom.Element) node;
            String key = element.getNodeName();
            String value = element.getTextContent();
            
            try {
              DefaultConfigurationType type = DefaultConfigurationType.valueOf(key);
              // Special handling for OBJECT property
              if (type == DefaultConfigurationType.OBJECT) {
                configMap.put(type, dummyObject);
              } else if (type.getValueType() == Boolean.class) {
                configMap.put(type, Boolean.parseBoolean(value));
              } else if (type.getValueType() == Integer.class) {
                configMap.put(type, Integer.parseInt(value));
              } else if (type.getValueType() == Float.class) {
                configMap.put(type, Float.parseFloat(value));
              } else {
                configMap.put(type, value);
              }
            } catch (IllegalArgumentException e) {
              // Skip unknown properties
            }
          }
        }
        
        // Always add the dummy object if it's not already added
        if (!configMap.containsKey(DefaultConfigurationType.OBJECT)) {
          configMap.put(DefaultConfigurationType.OBJECT, dummyObject);
        }
      } catch (Exception e) {
        throw new ConfigurationException("Failed to load configuration file: " + file,
            e, null, ConfigurationException.ErrorType.LOAD_ERROR);
      }
    }
  }
}
