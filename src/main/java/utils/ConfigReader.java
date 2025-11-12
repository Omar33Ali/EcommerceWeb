package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader is responsible for loading and providing access to configuration properties
 * from the config.properties file used in the test framework.
 */
public class ConfigReader {
    Properties properties = new Properties();
    /**
     * Constructor to load the properties file.
     * Loads the configuration from src/test/resources/config.properties.
     */
    public ConfigReader() {
        try {
            // Load the properties file from the specified path
            FileInputStream inputStream = new FileInputStream("src\\test\\resources\\config.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Gets a property value by key from the loaded configuration.
     *
     * @param key the property key
     * @return the property value as a String
     */
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
