package dev.tricht.lunaris.util

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32

import java.awt.*
import java.io.IOException
import java.net.URI

object Platform {

    private val log = org.slf4j.LoggerFactory.getLogger(Platform::class.java)

    val isPoeActive: Boolean
        get() = if (isWindows) {
            foregroundWindowTitle == "Path of Exile"
        } else true

    private val foregroundWindowTitle: String
        get() {
            val fgWindow = User32.INSTANCE.GetForegroundWindow()
            val titleLength = User32.INSTANCE.GetWindowTextLength(fgWindow) + 1
            val title = CharArray(titleLength)
            User32.INSTANCE.GetWindowText(fgWindow, title, titleLength)
            return Native.toString(title)
        }

    val isLinux: Boolean
        get() = com.sun.jna.Platform.isLinux()

    val isWindows: Boolean
        get() = com.sun.jna.Platform.isWindows()

    val os: String
        get() = System.getProperty("os.name")

    fun browse(url: String) {
        try {
            Desktop.getDesktop().browse(URI.create(
                    url.replace(" ", "%20")
            ))
        } catch (e: IOException) {
            log.error("Failed to browse to $url", e)
        }
    }

}
