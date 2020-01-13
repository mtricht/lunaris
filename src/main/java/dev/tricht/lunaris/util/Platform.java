package dev.tricht.lunaris.util;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

@Slf4j
public class Platform {

    public static boolean isPoeActive() {
        if (isWindows()) {
            return getForegroundWindowTitle().equals("Path of Exile");
        }
        return true;
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
            Desktop.getDesktop().browse(URI.create(
                    url.replace(" ", "%20")
            ));
        } catch (IOException e) {
            log.error("Failed to browse to " + url, e);
        }
    }

    public static boolean isLinux() {
        return com.sun.jna.Platform.isLinux();
    }

    public static boolean isWindows() {
        return com.sun.jna.Platform.isWindows();
    }

    public static String getOS() {
        return System.getProperty("os.name");
    }
}
