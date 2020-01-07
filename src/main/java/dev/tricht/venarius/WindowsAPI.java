package dev.tricht.venarius;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

public class WindowsAPI {

    public static boolean isPoeActive() {
        return getForegroundWindowTitle().equals("Path of Exile");
    }

    private static String getForegroundWindowTitle() {
        WinDef.HWND fgWindow = User32.INSTANCE.GetForegroundWindow();
        int titleLength = User32.INSTANCE.GetWindowTextLength(fgWindow) + 1;
        char[] title = new char[titleLength];
        User32.INSTANCE.GetWindowText(fgWindow, title, titleLength);
        return Native.toString(title);
    }

}
