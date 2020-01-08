package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.WindowsAPI;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class StashListener implements NativeMouseWheelListener, NativeKeyListener {

    private Robot robot;
    private boolean ctrlPressed = false;

    public StashListener(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent event) {
        if (WindowsAPI.isPoeActive() && ctrlPressed) {
            VoidDispatchService.consume(event);
            if (event.getWheelRotation() > 0) {
                robot.keyPress(KeyEvent.VK_RIGHT);
            } else {
                robot.keyPress(KeyEvent.VK_LEFT);
            }
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        ctrlPressed = event.getModifiers() == NativeInputEvent.CTRL_L_MASK;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        ctrlPressed = event.getModifiers() == NativeInputEvent.CTRL_L_MASK;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }

}
