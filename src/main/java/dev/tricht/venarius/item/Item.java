package dev.tricht.venarius.item;

import dev.tricht.venarius.item.types.ItemType;
import dev.tricht.venarius.ninja.poe.Price;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

@Data
public class Item {
    private Point mousePosition;
    private String name;
    private Price meanPrice = null;
    private String iconUrl;

    private ItemRarity rarity;
    private ItemProps props;

    private String[] lines;
    private ItemType type;
    private String base;
    private ArrayList<String> affixes;

    public boolean hasPrice() {
        return meanPrice != null;
    }
}
