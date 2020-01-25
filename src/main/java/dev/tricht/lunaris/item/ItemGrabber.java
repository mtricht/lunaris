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
import java.util.Arrays;

@Slf4j
public class ItemGrabber {

    private ItemResolver itemResolver;

    public ItemGrabber(ItemResolver itemResolver) {
        this.itemResolver = itemResolver;
    }

    public Item grab(String itemText) {
        String[] lines = itemText.split("\\r?\\n");
        if (lines.length == 1) {
            return null;
        }
        Item item = new Item();
        try {
            ItemParser parser = new ItemParser(lines);
            item = parser.parse();
            item.setClipboardText(itemText);
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

}
