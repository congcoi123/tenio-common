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

package com.tenio.common.configuration.format;

import com.tenio.common.configuration.ConfigurationException;
import com.tenio.common.configuration.ConfigurationException.ErrorType;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A configuration format handler for XML files.
 * <p>
 * This class implements the {@link ConfigurationFormatHandler} interface to provide
 * support for loading and saving configuration data in XML format. It uses Java's
 * built-in {@link Properties} class to handle XML parsing and generation.
 * <p>
 * The XML format supported by this handler follows the structure defined by the
 * {@link Properties#loadFromXML(java.io.InputStream)} and
 * {@link Properties#storeToXML(java.io.OutputStream, String)} methods. This format
 * is a simple key-value structure with the following example:
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd"&gt;
 * &lt;properties&gt;
 *   &lt;entry key="SERVER_PORT"&gt;8080&lt;/entry&gt;
 *   &lt;entry key="SERVER_NAME"&gt;TenIO Server&lt;/entry&gt;
 *   &lt;entry key="MAX_CONNECTIONS"&gt;1000&lt;/entry&gt;
 * &lt;/properties&gt;
 * </pre>
 * 
 * <p>
 * Note that all values in the XML file are stored as strings. When loading a configuration,
 * this handler returns all values as strings. It is the responsibility of the
 * {@link com.tenio.common.configuration.Configuration} implementation to convert these
 * strings to the appropriate types.
 * 
 * @see ConfigurationFormatHandler
 * @see Properties
 */
public class XmlConfigurationHandler implements ConfigurationFormatHandler {

    /**
     * Determines if this handler can handle the specified file.
     * <p>
     * This method checks if the file has an XML extension (.xml).
     *
     * @param file the file to check
     * @return true if the file has an XML extension, false otherwise
     */
    @Override
    public boolean canHandle(File file) {
        return file.getName().toLowerCase().endsWith(".xml");
    }

    /**
     * Loads configuration data from an XML file.
     * <p>
     * This method reads the XML file using Java's {@link Properties} class and
     * converts the properties to a map of configuration values. All values in the
     * returned map are strings.
     * <p>
     * The method performs the following validations:
     * <ul>
     *   <li>Checks if the file is null</li>
     *   <li>Checks if the file exists</li>
     *   <li>Checks if the file is empty</li>
     * </ul>
     * If any of these validations fail, a {@link ConfigurationException} is thrown.
     *
     * @param file the XML file to load configuration data from
     * @return a map of configuration values where keys are property names and values are strings
     * @throws ConfigurationException if the file is null, does not exist, is empty,
     *         or if there is an error loading the file
     */
    @Override
    public Map<String, Object> load(File file) throws ConfigurationException {
        if (file == null) {
            throw new ConfigurationException("Cannot load configuration from a null file",
                    null, null, ErrorType.LOAD_ERROR);
        }

        if (!file.exists()) {
            throw new ConfigurationException("Configuration file does not exist: " + file.getAbsolutePath(),
                    null, null, ErrorType.LOAD_ERROR);
        }

        if (file.length() == 0) {
            throw new ConfigurationException("Configuration file is empty: " + file.getAbsolutePath(),
                    null, null, ErrorType.LOAD_ERROR);
        }

        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(file)) {
            properties.loadFromXML(fis);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Configuration file not found: " + file.getAbsolutePath(),
                    e, null, ErrorType.LOAD_ERROR);
        } catch (IOException e) {
            throw new ConfigurationException("Error loading XML configuration: " + e.getMessage(),
                    e, null, ErrorType.LOAD_ERROR);
        }

        Map<String, Object> config = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            config.put(key, properties.getProperty(key));
        }
        return config;
    }

    /**
     * Saves configuration data to an XML file.
     * <p>
     * This method converts the configuration map to Java {@link Properties} and
     * writes them to the specified file in XML format. The XML file will include
     * a comment indicating it was generated by TenIO.
     * <p>
     * The method performs the following validations:
     * <ul>
     *   <li>Checks if the file is null</li>
     *   <li>Checks if the configuration map is null</li>
     * </ul>
     * If any of these validations fail, a {@link ConfigurationException} is thrown.
     *
     * @param file the file to save configuration data to
     * @param config the configuration data to save
     * @throws ConfigurationException if the file or configuration map is null,
     *         or if there is an error saving the file
     */
    @Override
    public void save(File file, Map<String, Object> config) throws ConfigurationException {
        if (file == null) {
            throw new ConfigurationException("Cannot save configuration to a null file",
                    null, null, ErrorType.LOAD_ERROR);
        }

        if (config == null) {
            throw new ConfigurationException("Cannot save null configuration",
                    null, null, ErrorType.LOAD_ERROR);
        }

        Properties properties = new Properties();
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            if (entry.getValue() != null) {
                properties.setProperty(entry.getKey(), entry.getValue().toString());
            }
        }

        try (FileOutputStream fos = new FileOutputStream(file)) {
            properties.storeToXML(fos, "TenIO Configuration", "UTF-8");
        } catch (IOException e) {
            throw new ConfigurationException("Error saving XML configuration: " + e.getMessage(),
                    e, null, ErrorType.LOAD_ERROR);
        }
    }
} 