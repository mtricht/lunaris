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
        file = DirectoryManager.getFile("lunaris.properties");
        try {
            file.createNewFile();
        } catch (IOException e) {
            ErrorUtil.showErrorDialogAndExit("Unable to create lunaris.properties file");
        }
        try {
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            ErrorUtil.showErrorDialogAndExit("Unable to read lunaris.properties");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultVal) {
        if (properties.containsKey(key)) {
            return getProperty(key);
        }
        return defaultVal;
    }

    public static void writeProperty(String key, String value) {
        properties.setProperty(key, value);
        try {
            properties.store(new FileOutputStream(file), null);
        } catch (IOException ex) {
            ErrorUtil.showErrorDialogAndExit("Unable to save lunaris.properties");
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
