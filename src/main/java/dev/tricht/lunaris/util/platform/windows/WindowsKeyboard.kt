package dev.tricht.lunaris.util.platform.windows

import com.sun.jna.platform.win32.BaseTSD.ULONG_PTR
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.DWORD
import com.sun.jna.platform.win32.WinDef.WORD
import com.sun.jna.platform.win32.WinUser.INPUT


class WindowsKeyboard {
    companion object {
        private val KEY_DOWN = 0L;
        private val KEY_UP = 2L;

        fun sendKeyDown(c: Long) {
            val input = INPUT()
            input.type = DWORD(INPUT.INPUT_KEYBOARD.toLong())
            input.input.setType("ki")
            input.input.ki.wScan = WORD(0)
            input.input.ki.time = DWORD(0)
            input.input.ki.dwExtraInfo = ULONG_PTR(0)
            input.input.ki.wVk = WORD(c)
            input.input.ki.dwFlags = DWORD(KEY_DOWN)
            User32.INSTANCE.SendInput(DWORD(1), input.toArray(1) as Array<INPUT?>, input.size())
        }

        fun sendKeyUp(c: Long) {
            val input = INPUT()
            input.type = DWORD(INPUT.INPUT_KEYBOARD.toLong())
            input.input.setType("ki")
            input.input.ki.wScan = WORD(0)
            input.input.ki.time = DWORD(0)
            input.input.ki.dwExtraInfo = ULONG_PTR(0)
            input.input.ki.wVk = WORD(c)
            input.input.ki.dwFlags = DWORD(KEY_UP)
            User32.INSTANCE.SendInput(DWORD(1), input.toArray(1) as Array<INPUT?>, input.size())
        }
    }
}