package dev.tricht.lunaris.listeners

import dev.tricht.lunaris.util.KeyboardUtil.type
import java.awt.Robot
import java.awt.event.KeyEvent

class InviteLastWhisperListener(private val combo: KeyCombo, private val robot: Robot) : GameListener {
    override fun onEvent(event: GameEvent) {
        robot.keyPress(KeyEvent.VK_CONTROL)
        pressAndRelease(KeyEvent.VK_ENTER)
        robot.keyRelease(KeyEvent.VK_CONTROL)
        pressAndRelease(KeyEvent.VK_HOME)
        pressAndRelease(KeyEvent.VK_DELETE)
        type(robot, "/invite ")
        pressAndRelease(KeyEvent.VK_ENTER)
    }

    override fun supports(event: GameEvent): Boolean {
        return combo.matches(event.originalEvent)
    }

    private fun pressAndRelease(key: Int) {
        robot.keyPress(key)
        robot.keyRelease(key)
    }

}