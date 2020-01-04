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

    public KeyHandler(Consumer<ItemRequest> callback) {
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
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.position = MouseHandler.position;
        if (event.getVirtualKeyCode() == GlobalKeyEvent.VK_D && event.isMenuPressed()) {
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_C);
            robot.delay(10);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyRelease(KeyEvent.VK_C);
            String clipboard = null;
            try {
                clipboard = (String) Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor);
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }
            itemRequest.itemText = clipboard;
            this.callback.accept(itemRequest);
        }
    }

    @Override
    public void keyReleased(GlobalKeyEvent event) {
    }
}
