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

    public ItemGrabber(Robot robot) throws IOException {
        Downloader.download();
        itemResolver = new ItemResolver();
        this.robot = robot;
    }

    public Item grab() throws IOException, UnsupportedFlavorException {
        String[] lines = new String[]{""};
        try {
            lines = getItemText().split("\\r?\\n");
        } catch (IOException|UnsupportedFlavorException e) {
            System.out.println(e.toString());
        }

        Item item = new Item();
        try {
            ItemParser parser = new ItemParser(lines);
            item = parser.parse();
        } catch (Exception e) {
            e.printStackTrace();
            return item;
        }

        if (itemResolver.hasItem(item.getBase())) {
            item.setIconUrl(itemResolver.getItem(item.getBase()).getIconUrl());
            item.setMeanPrice(itemResolver.appraise(item.getBase()));
        }

        return item;
    }

    private String getItemText() throws IOException, UnsupportedFlavorException {
        pressControlC();
        String clipboard = getClipboard();
        setClipboard("");

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
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_C);
    }

}
