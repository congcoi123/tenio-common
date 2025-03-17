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

import java.io.File;
import java.util.Map;

/**
 * Test implementation of CommonConfiguration for unit testing.
 */
public class TestConfiguration extends CommonConfiguration {

    @Override
    protected void loadInternal(String file) throws ConfigurationException {
        try {
            File configFile = new File(file);
            
            if (!configFile.exists()) {
                throw new ConfigurationException("Configuration file not found: " + file,
                    null, null, ConfigurationException.ErrorType.LOAD_ERROR);
            }

            // Load from XML file
            try {
                javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(configFile);
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
                            TestConfigurationType type = TestConfigurationType.valueOf(key);
                            push(type, convertValue(value, type.getValueType()));
                        } catch (IllegalArgumentException e) {
                            // Skip unknown properties
                        }
                    }
                }
            } catch (Exception e) {
                throw new ConfigurationException("Failed to load configuration file: " + file,
                    e, null, ConfigurationException.ErrorType.LOAD_ERROR);
            }
        } catch (ConfigurationException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigurationException("Failed to load configuration file: " + file,
                e, null, ConfigurationException.ErrorType.LOAD_ERROR);
        }
    }

    @Override
    protected void extend(Map<String, String> extProperties) {
        // Test implementation doesn't need extensions
    }

    @Override
    protected ConfigurationType getConfigurationType(String name) {
        try {
            return TestConfigurationType.valueOf(name);
        } catch (IllegalArgumentException e) {
            // Skip unknown properties
            return null;
        }
    }

    private Object convertValue(String value, Class<?> targetType) throws ConfigurationException {
        try {
            if (targetType == Boolean.class) {
                return Boolean.parseBoolean(value);
            } else if (targetType == Integer.class) {
                return Integer.parseInt(value);
            } else if (targetType == Float.class) {
                return Float.parseFloat(value);
            } else if (targetType == String.class) {
                return value;
            } else {
                throw new ConfigurationException("Unsupported type: " + targetType.getSimpleName(),
                    null, null, ConfigurationException.ErrorType.INVALID_VALUE_TYPE);
            }
        } catch (NumberFormatException e) {
            throw new ConfigurationException("Failed to convert value: " + value,
                e, null, ConfigurationException.ErrorType.PARSE_ERROR);
        }
    }

    @Override
    protected ConfigurationType[] getConfigurationTypes() {
        return TestConfigurationType.values();
    }

    @Override
    public void validate() throws ConfigurationException {
        super.validate();
        // No additional validation needed
    }

    @Override
    protected void loadInternalToMap(String file, Map<ConfigurationType, Object> configMap) throws ConfigurationException {
        try {
            File configFile = new File(file);
            
            if (!configFile.exists()) {
                throw new ConfigurationException("Configuration file not found: " + file,
                    null, null, ConfigurationException.ErrorType.LOAD_ERROR);
            }

            // Load from XML file
            try {
                javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
                javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                org.w3c.dom.Document doc = dBuilder.parse(configFile);
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
                            TestConfigurationType type = TestConfigurationType.valueOf(key);
                            configMap.put(type, convertValue(value, type.getValueType()));
                        } catch (IllegalArgumentException e) {
                            // Skip unknown properties
                        }
                    }
                }
            } catch (Exception e) {
                throw new ConfigurationException("Failed to load configuration file: " + file,
                    e, null, ConfigurationException.ErrorType.LOAD_ERROR);
            }
        } catch (ConfigurationException e) {
            throw e;
        } catch (Exception e) {
            throw new ConfigurationException("Failed to load configuration file: " + file,
                e, null, ConfigurationException.ErrorType.LOAD_ERROR);
        }
    }

    @Override
    public String getString(ConfigurationType key) {
        if (key == null) {
            return null;
        }
        
        return super.getString(key);
    }

    @Override
    public int getInt(ConfigurationType key) {
        if (key == null) {
            return 0;
        }
        
        return super.getInt(key);
    }
} 