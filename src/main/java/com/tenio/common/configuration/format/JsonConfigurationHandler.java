package com.tenio.common.configuration.format;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tenio.common.configuration.ConfigurationException;
import com.tenio.common.configuration.ConfigurationException.ErrorType;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Handler for JSON format configuration files.
 */
public class JsonConfigurationHandler implements ConfigurationFormatHandler {

    private final Gson gson;
    private final Type mapType;

    public JsonConfigurationHandler() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        mapType = new TypeToken<Map<String, Object>>() {}.getType();
    }

    @Override
    public Map<String, Object> load(File file) throws ConfigurationException {
        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, mapType);
        } catch (IOException e) {
            throw new ConfigurationException("Failed to load JSON file: " + file,
                e, null, ErrorType.LOAD_ERROR);
        }
    }

    @Override
    public void save(File file, Map<String, Object> configuration) throws ConfigurationException {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(configuration, writer);
        } catch (IOException e) {
            throw new ConfigurationException("Failed to save JSON file: " + file,
                e, null, ErrorType.LOAD_ERROR);
        }
    }

    @Override
    public boolean canHandle(File file) {
        return file.getName().toLowerCase().endsWith(".json");
    }
} 