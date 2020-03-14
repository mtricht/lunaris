package dev.tricht.lunaris.util

import java.awt.*
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent

object KeyboardUtil {
    fun type(robot: Robot, text: String) {
        Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(text), null)
        robot.keyPress(KeyEvent.VK_CONTROL)
        robot.keyPress(KeyEvent.VK_V)
        robot.keyRelease(KeyEvent.VK_V)
        robot.keyRelease(KeyEvent.VK_CONTROL)
    }
}
