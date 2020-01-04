package dev.tricht.poe.assistant.item;

import dev.tricht.poe.assistant.ninja.poe.Downloader;
import dev.tricht.poe.assistant.ninja.poe.ItemResolver;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ItemGrabber {

    private ItemResolver itemResolver;
    private Robot robot;

    public ItemGrabber() throws IOException {
        Downloader.download();
        itemResolver = new ItemResolver();
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public Item grab() throws IOException, UnsupportedFlavorException {
        Item item = new Item();
        String[] lines = getItemText().split("\\r?\\n");
        item.setName(lines[1]);
        if (itemResolver.hasItem(item.getName())) {
            item.setMeanPrice(itemResolver.appraise(item.getName()));
        }
        return item;
    }

    private String getItemText() throws IOException, UnsupportedFlavorException {
        String oldClipboard = getClipboard();
        pressControlC();
        String clipboard = getClipboard();
        setClipboard(oldClipboard);
        return clipboard;
    }

    private String getClipboard() throws IOException, UnsupportedFlavorException {
        return (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
    }

    private void setClipboard(String clipboard) {
        StringSelection stringSelection = new StringSelection(clipboard);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                stringSelection, null);
    }

    private void pressControlC() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_C);
    }

}
