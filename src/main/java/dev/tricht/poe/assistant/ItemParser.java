package dev.tricht.poe.assistant;

import dev.tricht.poe.assistant.ninja.poe.Downloader;
import dev.tricht.poe.assistant.ninja.poe.ItemResolver;
import dev.tricht.poe.assistant.tooltip.ItemRequest;

import java.io.IOException;

public class ItemParser {

    private ItemResolver itemResolver;

    public ItemParser() throws IOException {
        Downloader.download();
        itemResolver = new ItemResolver();
    }

    public Item parse(ItemRequest itemRequest) {
        Item item = new Item();
        String[] lines = itemRequest.clipboard.split("\\r?\\n");
        item.setName(lines[1]);
        item.setMeanPrice(itemResolver.appraise(item.getName()));
        return item;
    }

}
