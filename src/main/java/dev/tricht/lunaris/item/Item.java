package dev.tricht.lunaris.item;

import dev.tricht.lunaris.item.types.ItemType;
import dev.tricht.lunaris.ninja.poe.Price;
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
    private ArrayList<String> implicits;

    public boolean hasPrice() {
        return meanPrice != null;
    }
}
