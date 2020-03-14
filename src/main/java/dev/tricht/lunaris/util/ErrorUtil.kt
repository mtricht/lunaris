package dev.tricht.lunaris.util

import javax.swing.*
import kotlin.system.exitProcess

object ErrorUtil {
    fun showErrorDialogAndExit(message: String) {
        JOptionPane.showMessageDialog(null, message, "Lunaris", JOptionPane.ERROR_MESSAGE)
        exitProcess(1)
    }
}
