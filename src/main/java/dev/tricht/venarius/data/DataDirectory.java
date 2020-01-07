package dev.tricht.venarius.data;

import java.io.File;

public class DataDirectory {

    public static File getDirectory() {
        return getDirectory("");
    }

    public static File getDirectory(String folderName) {
        File dataDirectory = new File(System.getenv("APPDATA") + "\\Venarius\\data" + (folderName.isEmpty() ? "" : "\\" + folderName));
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                throw new RuntimeException("Unable to create directory.");
            }
        }
        return dataDirectory;
    }

}
