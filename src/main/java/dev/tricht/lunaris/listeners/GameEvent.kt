package dev.tricht.lunaris.listeners

import dev.tricht.lunaris.item.Item
import org.jnativehook.keyboard.NativeKeyEvent
import java.awt.Point

data class GameEvent(var item: Item? = null,
                     var mousePos: Point? = null,
                     var mouseWheelRotation: Int = 0,
                     var originalEvent: NativeKeyEvent? = null
)