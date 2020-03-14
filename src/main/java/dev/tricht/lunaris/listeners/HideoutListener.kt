package dev.tricht.lunaris.listeners

import dev.tricht.lunaris.util.KeyboardUtil.type
import java.awt.Robot
import java.awt.event.KeyEvent

class HideoutListener(private val combo: KeyCombo, private val robot: Robot) : GameListener {
    override fun onEvent(event: GameEvent) {
        pressAndRelease(KeyEvent.VK_ENTER)
        type(robot, "/hideout")
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