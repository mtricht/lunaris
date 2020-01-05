package dev.tricht.poe.assistant.item;

import dev.tricht.poe.assistant.ninja.poe.Downloader;
import dev.tricht.poe.assistant.ninja.poe.ItemResolver;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

@Slf4j
public class ItemGrabber {

    private ItemResolver itemResolver;
    private Robot robot;

    public ItemGrabber(Robot robot) throws IOException {
        Downloader.download();
        itemResolver = new ItemResolver();
        this.robot = robot;
    }

    public Item grab() {
        String[] lines;
        try {
            lines = getItemText().split("\\r?\\n");
        } catch (IOException|UnsupportedFlavorException e) {
            log.error("Failed to grab item", e);
            return null;
        }
        if (lines.length == 1) {
            return null;
        }

        Item item = new Item();
        try {
            ItemParser parser = new ItemParser(lines);
            item = parser.parse();
        } catch (Exception e) {
            log.error("Failed to parse item", e);
            return item;
        }

        if (itemResolver.hasItem(item)) {
            item.setIconUrl(itemResolver.getItem(item).getIconUrl());
            item.setMeanPrice(itemResolver.appraise(item));
        }

        return item;
    }

    private String getItemText() throws IOException, UnsupportedFlavorException {
        pressControlC();
        String clipboard = getClipboard();
        setClipboard();
        return clipboard;
    }

    private String getClipboard() throws IOException, UnsupportedFlavorException {
        return (String) Toolkit.getDefaultToolkit()
                .getSystemClipboard().getData(DataFlavor.stringFlavor);
    }

    private void setClipboard() {
        StringSelection stringSelection = new StringSelection("");
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
                stringSelection, null);
    }

    private void pressControlC() {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_C);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_C);
    }

}
