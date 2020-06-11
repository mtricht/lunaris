package dev.tricht.lunaris.util.platform.windows

import com.sun.jna.Native
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef.*
import com.sun.jna.platform.win32.WinNT.HANDLE
import com.sun.jna.platform.win32.WinUser.MSG
import com.sun.jna.platform.win32.WinUser.WinEventProc
import java.util.function.Consumer

class WindowFocusListener(private val callback: Consumer<String>) {
    private var winHookThread: Thread? = null
    private var winHookRunning = false
    private var winHook: HANDLE? = null

    private var EVENT_SYSTEM_FOREGROUND = 0x0003
    private val WINEVENT_SKIPOWNPROCESS = 0x0002

    private fun initWinHook() {

        val testproc = WinEventProc { _: HANDLE?, _: DWORD?, _: HWND?, _: LONG?, _: LONG?, _: DWORD?, _: DWORD? ->
            val buf = CharArray(1024 * 2)
            User32.INSTANCE.GetWindowText(User32.INSTANCE.GetForegroundWindow(), buf, 1024)
            callback.accept(Native.toString(buf))
        }
        winHookThread = Thread(Runnable {
            winHook = User32.INSTANCE.SetWinEventHook(
                    EVENT_SYSTEM_FOREGROUND,
                    EVENT_SYSTEM_FOREGROUND,
                    null, testproc, 0, 0,
                    WINEVENT_SKIPOWNPROCESS)
            winHookRunning = true
            val msg = MSG()
            while (winHookRunning) {
                while (User32.INSTANCE.PeekMessage(msg, null, 0, 0, 0)) {
                    User32.INSTANCE.TranslateMessage(msg)
                    User32.INSTANCE.DispatchMessage(msg)
                }
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                }
            }
        }, "Lunaris hook thread")
        winHookThread!!.priority = Thread.MIN_PRIORITY
        winHookThread!!.start()
    }

    fun destroy() {
        winHookRunning = false
        try {
            winHookThread!!.join(500)
        } catch (ex: InterruptedException) {
            System.err.println("joining error: $ex")
        }
        if (winHook != null) {
            User32.INSTANCE.UnhookWinEvent(winHook)
        }
    }

    init {
        initWinHook()
    }
}