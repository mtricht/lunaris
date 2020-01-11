package dev.tricht.lunaris.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class Platform {

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

    public static void browse(String url) {
        try {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            log.error("Failed to browse to " + url, e);
        }
    }

}
