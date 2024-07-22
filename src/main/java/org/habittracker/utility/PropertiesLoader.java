package org.habittracker.utility;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private Properties properties;

    // Constructor responsible for loading the properties from a specified file into the properties object.
    public PropertiesLoader(String propertiesFileName) {
        properties = new Properties();
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
            if (input == null) {
                System.out.println("Unable to find " + propertiesFileName);
                return;
            }
            // load method reads the properties from the input stream and populates the properties object with key-value pairs
            properties.load(input);
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }
}
