package dev.tricht.poe.assistant;

import dev.tricht.poe.assistant.tooltip.ItemRequest;
import dev.tricht.poe.assistant.watch.poe.ItemResolver;

import java.io.IOException;

public class ItemParser {

    private ItemResolver itemResolver;

    public ItemParser() throws IOException {
        itemResolver = new ItemResolver();
    }

    public Item parse(ItemRequest itemRequest) {
        Item item = new Item();
        String[] lines = itemRequest.clipboard.split("\\r?\\n");
        int lineCount = 0;
        for (String line : lines) {
            lineCount++;
            if (line.contains("Rarity: ")) {
                item.setRarity(line.replace("Rarity: ", ""));
                continue;
            }
            if (lineCount == 2) {
                item.setName(line);
                item.setId(itemResolver.resolve(line));
            }
        }
        item.setMeanPrice(itemResolver.appraise(item.getId()));
        return item;
    }

}
