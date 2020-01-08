package dev.tricht.lunaris;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Properties;

@Slf4j
public class PropertiesManager {

    private static File file;
    private static Properties properties = new Properties();

    public static final String LEAGUE = "LEAGUE";
    public static final String POESESSID = "POESESSID";

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
    }

    public static boolean containsKey(String key) {
        return properties.containsKey(key);
    }

}
