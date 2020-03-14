package dev.tricht.lunaris.util

import java.io.File

object DirectoryManager {
    private val directory: File?
        get() {
            val directory = when {
                Platform.isLinux -> {
                    val home = System.getProperty("user.home")
                    File("$home/.config/Lunaris")
                }
                Platform.isWindows -> File(System.getenv("APPDATA") + "\\Lunaris")
                else -> null
            }
            if (directory == null) {
                ErrorUtil.showErrorDialogAndExit(String.format("Platform %s not supported.", Platform.os))
            }
            if (directory?.exists() == false) {
                if (!directory.mkdirs()) {
                    ErrorUtil.showErrorDialogAndExit("Unable to create lunaris data/config directory.")
                }
            }
            return directory
        }

    fun getFile(filename: String): File {
        return File(directory?.absolutePath + File.separator + filename)
    }

    fun getDataDirectory(folderName: String): File {
        val dataDirectory = File(directory?.toString() + File.separator + "data"
                + if (folderName.isEmpty()) "" else File.separator + folderName)
        if (!dataDirectory.exists()) {
            if (!dataDirectory.mkdirs()) {
                throw RuntimeException("Unable to create directory.")
            }
        }
        return dataDirectory
    }
}
