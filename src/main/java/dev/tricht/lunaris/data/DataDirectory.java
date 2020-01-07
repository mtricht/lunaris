package dev.tricht.lunaris.data;

import java.io.File;

public class DataDirectory {

    public static File getDirectory() {
        return getDirectory("");
    }

    public static File getDirectory(String folderName) {
        File dataDirectory = new File(System.getenv("APPDATA") + "\\Lunaris\\data" + (folderName.isEmpty() ? "" : "\\" + folderName));
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                throw new RuntimeException("Unable to create directory.");
            }
        }
        return dataDirectory;
    }

}
