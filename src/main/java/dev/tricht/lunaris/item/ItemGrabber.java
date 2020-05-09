package dev.tricht.lunaris.item;

import dev.tricht.lunaris.ninja.poe.PoeNinjaItemResolver;
import dev.tricht.lunaris.ninja.poe.Price;
import dev.tricht.lunaris.ninja.poe.PoeNinjaRemoteItem;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemGrabber {

    private PoeNinjaItemResolver poeNinjaItemResolver;

    public ItemGrabber(PoeNinjaItemResolver poeNinjaItemResolver) {
        this.poeNinjaItemResolver = poeNinjaItemResolver;
    }

    public Item grab(String itemText) {
        String[] lines = itemText.split("\\r?\\n");
        if (lines.length == 1) {
            return null;
        }
        Item item = new Item();
        try {
            item = ItemParser.parse(lines);
            item.setClipboardText(itemText);
        } catch (Exception e) {
            log.error("Failed to parse item", e);
            return item;
        }

        if (poeNinjaItemResolver.hasItem(item)) {
            PoeNinjaRemoteItem poeNinjaRemoteItem = poeNinjaItemResolver.getItem(item);
            item.setIconUrl(poeNinjaRemoteItem.getIconUrl());

            Price price = poeNinjaItemResolver.appraise(poeNinjaRemoteItem);
            item.setMeanPrice(price);
        }

        return item;
    }

}
