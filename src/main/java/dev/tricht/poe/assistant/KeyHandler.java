package dev.tricht.poe.assistant;

import lc.kra.system.keyboard.event.GlobalKeyEvent;
import lc.kra.system.keyboard.event.GlobalKeyListener;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.function.Consumer;

public class KeyHandler implements GlobalKeyListener {

    private Consumer<ItemRequest> callback;
    private Robot robot;

    KeyHandler(Consumer<ItemRequest> callback) {
        this.callback = callback;
        try {
            this.robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void keyPressed(GlobalKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_D && event.isMenuPressed()) {
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
            itemRequest.itemText = clipboard;
            this.callback.accept(itemRequest);
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
    public void keyReleased(GlobalKeyEvent event) {
    }
}
