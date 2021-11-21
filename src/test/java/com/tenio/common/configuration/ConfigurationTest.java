package com.tenio.common.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

class ConfigurationTest {
  @Test
  void testConstructor() throws Exception {
    DefaultConfiguration actualMyConfiguration = new DefaultConfiguration();
    HashMap<String, String> stringStringMap = new HashMap<String, String>(1);
    actualMyConfiguration.extend(stringStringMap);
    actualMyConfiguration.load("File");
    assertEquals("{}", actualMyConfiguration.toString());
    assertTrue(stringStringMap.isEmpty());
  }
}

