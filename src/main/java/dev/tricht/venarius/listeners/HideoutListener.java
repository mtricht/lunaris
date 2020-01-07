package dev.tricht.venarius.listeners;

import dev.tricht.venarius.WindowsAPI;
import lombok.SneakyThrows;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HideoutListener implements NativeKeyListener {

    private Robot robot;

    public HideoutListener(Robot robot) {
        this.robot = robot;
    }

    @SneakyThrows
    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (WindowsAPI.isPoeActive() && event.getKeyCode() == NativeKeyEvent.VC_F5) {
            this.pressAndRelease(KeyEvent.VK_ENTER);
            this.pressAndRelease(KeyEvent.VK_SLASH);
            this.pressAndRelease(KeyEvent.VK_H);
            this.pressAndRelease(KeyEvent.VK_I);
            this.pressAndRelease(KeyEvent.VK_D);
            this.pressAndRelease(KeyEvent.VK_E);
            this.pressAndRelease(KeyEvent.VK_O);
            this.pressAndRelease(KeyEvent.VK_U);
            this.pressAndRelease(KeyEvent.VK_T);
            this.pressAndRelease(KeyEvent.VK_ENTER);
        }
    }

    private void pressAndRelease(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }
}
