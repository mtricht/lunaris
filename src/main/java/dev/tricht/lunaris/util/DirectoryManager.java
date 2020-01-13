package dev.tricht.lunaris.util;

import java.io.File;

public class DirectoryManager {

    public static File getFile(String filename) {
        return new File(getDirectory().getAbsolutePath() + File.separator + filename);
    }

    public static File getDataDirectory(String folderName) {
        File dataDirectory = new File(getDirectory() + File.separator + "data"
                + (folderName.isEmpty() ? "" : File.separator + folderName));
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                throw new RuntimeException("Unable to create directory.");
            }
        }
        return dataDirectory;
    }

    private static File getDirectory() {
        File directory = null;
        if (Platform.isLinux()) {
            String home = System.getProperty("user.home");
            directory = new File(home + "/.config/Lunaris");
        } else if (Platform.isWindows()) {
            directory = new File(System.getenv("APPDATA") + "\\Lunaris");
        } else {
            ErrorUtil.showErrorDialogAndExit(String.format("Platform %s not supported.", Platform.getOS()));
        }
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                ErrorUtil.showErrorDialogAndExit("Unable to create lunaris data/config directory.");
            }
        }
        return directory;
    }

}
