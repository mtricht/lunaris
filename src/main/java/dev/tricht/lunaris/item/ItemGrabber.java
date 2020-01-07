package dev.tricht.lunaris.item;

import dev.tricht.lunaris.ninja.poe.ItemResolver;
import dev.tricht.lunaris.ninja.poe.Price;
import dev.tricht.lunaris.ninja.poe.RemoteItem;
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

    public ItemGrabber(Robot robot, ItemResolver itemResolver) throws IOException {
        this.itemResolver = itemResolver;
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
            RemoteItem remoteItem = itemResolver.getItem(item);
            item.setIconUrl(remoteItem.getIconUrl());

            Price price = itemResolver.appraise(remoteItem);
            item.setMeanPrice(price);
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
