package dev.tricht.lunaris.util.platform.windows

import com.sun.jna.platform.win32.User32
import java.util.function.Consumer


class VulkanFullscreenFixer {

    companion object {
        var isPoEActive = false;
        lateinit var focusListener: WindowFocusListener;

        fun fixFullscreen() {
            val focusConsumer = Consumer<String> { windowTitle: String? ->
                if (!windowTitle.equals("Path of Exile")) {
                    println(windowTitle)
                    isPoEActive = false;
                } else if (windowTitle.equals("Path of Exile") && !isPoEActive) {
                    isPoEActive = true;
                    this.changeGameWindowProperties()
                }
            }
            focusListener = WindowFocusListener(focusConsumer)
            this.changeGameWindowProperties()
        }

        fun changeGameWindowProperties() {
            var hWnd = User32.INSTANCE.FindWindow(null, "Path of Exile");
            if (hWnd != null) {
                val GWL_STYLE = -16
                val WS_BORDER = 0x00800000
                val WS_DLGFRAME = 0x00400000
                val WS_CAPTION = WS_BORDER or WS_DLGFRAME

                var style = User32.INSTANCE.GetWindowLong(hWnd, GWL_STYLE);
                if(style == (style and WS_CAPTION.inv())) {
                    return;
                }

                Thread.sleep(1000);

                User32.INSTANCE.SetWindowLong(hWnd, GWL_STYLE, style and WS_CAPTION.inv());

                User32.INSTANCE.SetForegroundWindow(hWnd);

                val KEY_LWIN = 0x5BL;
                val KEY_UP_ARROW = 0x26L;
                val KEY_DOWN_ARROW = 0x28L;
                WindowsKeyboard.sendKeyDown(KEY_LWIN);

                WindowsKeyboard.sendKeyDown(KEY_UP_ARROW);
                WindowsKeyboard.sendKeyUp(KEY_UP_ARROW);

                Thread.sleep(250);

                WindowsKeyboard.sendKeyDown(KEY_DOWN_ARROW);
                WindowsKeyboard.sendKeyUp(KEY_DOWN_ARROW);

                Thread.sleep(250);

                WindowsKeyboard.sendKeyDown(KEY_UP_ARROW);
                WindowsKeyboard.sendKeyUp(KEY_UP_ARROW);

                WindowsKeyboard.sendKeyUp(KEY_LWIN);
            }
        }

        fun removeHook() {
            if (this::focusListener.isInitialized) {
                focusListener.destroy()
            };
        }
    }
}