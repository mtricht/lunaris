package dev.tricht.lunaris.item;

import dev.tricht.lunaris.item.parser.AffixPart;
import dev.tricht.lunaris.item.types.ItemType;
import dev.tricht.lunaris.ninja.poe.Price;
import lombok.Data;

import java.awt.*;
import java.util.ArrayList;

@Data
public class Item {
    public String clipboardText;
    private Point mousePosition;
    private String name;
    private String base = "";
    private Price meanPrice = null;
    private String iconUrl;

    private ItemRarity rarity;
    private ItemProps props;

    private String[] lines;
    public ItemType type;
    private ArrayList<AffixPart.Affix> affixes;
    private ArrayList<String> craftedAffixes;
    private ArrayList<String> implicits;
    public String category;

    public boolean exists() {
        return !base.equals("");
    }

}
