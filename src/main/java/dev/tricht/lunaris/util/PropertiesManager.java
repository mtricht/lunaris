package dev.tricht.lunaris.util;

import dev.tricht.lunaris.settings.event.PropertyListener;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class PropertiesManager {

    private static File file;
    private static Properties properties = new Properties();

    public static final String LEAGUE = "LEAGUE";
    public static final String POESESSID = "POESESSID";

    private static HashMap<String, PropertyListener> propertyListeners = new HashMap<>();

    public static void load() {
        file = new File(System.getenv("APPDATA") + "\\Lunaris\\lunaris.properties");
        try {
            file.createNewFile();
        } catch (IOException e) {
            log.error("Unable to create lunaris.properties", e);
            System.exit(0);
        }
        try {
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            log.error("Unable to read lunaris.properties", e);
            System.exit(0);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static void writeProperty(String key, String value) {
        properties.setProperty(key, value);
        try {
            properties.store(new FileOutputStream(file), null);
        } catch (IOException ex) {
            log.error("Failed saving configuration", ex);
        }

        for (Map.Entry<String, PropertyListener> listener : propertyListeners.entrySet()) {
            if (key.matches(listener.getKey())) {
                listener.getValue().onPropertyChange();
            }
        }
    }

    public static boolean containsKey(String key) {
        return properties.containsKey(key);
    }

    public static void addPropertyListener(String propMask, PropertyListener listener) {
        propertyListeners.put(propMask, listener);
    }

}
