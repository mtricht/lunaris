package dev.tricht.poe.assistant.item;

import dev.tricht.poe.assistant.item.types.ItemType;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

@Data
public class Item {
    private Point mousePosition;
    private String name;
    private Integer meanPrice;
    private String iconUrl;

    private ItemRarity rarity;
    private ItemProps props;

    private String[] lines;
    private ItemType type;
    private String base;
    private ArrayList<String> affixes;
}
