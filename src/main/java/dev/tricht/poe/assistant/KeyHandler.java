package dev.tricht.poe.assistant;

import dev.tricht.poe.assistant.tooltip.ItemRequest;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.function.Consumer;

public class KeyHandler implements NativeKeyListener {

    private Consumer<ItemRequest> callback;
    private Robot robot;
    public static boolean ctrlPressed = false;

    KeyHandler(Consumer<ItemRequest> callback) {
        this.callback = callback;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void pressControlC() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_C);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        ctrlPressed = event.getModifiers() == NativeInputEvent.CTRL_L_MASK;
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        if (event.getKeyCode() == NativeKeyEvent.VC_D && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
            ItemRequest itemRequest = new ItemRequest();
            itemRequest.position = MouseHandler.position;
            pressControlC();
            String clipboard;
            try {
                clipboard = (String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
                return;
            }
            itemRequest.clipboard = clipboard;
            this.callback.accept(itemRequest);
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        ctrlPressed = event.getModifiers() == NativeInputEvent.CTRL_L_MASK;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }
}
