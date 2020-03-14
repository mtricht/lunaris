package dev.tricht.lunaris.item;

import dev.tricht.lunaris.item.parser.AffixPart;
import dev.tricht.lunaris.item.types.EquipmentItem;
import dev.tricht.lunaris.item.types.EquipmentSlot;
import dev.tricht.lunaris.item.types.ItemType;
import dev.tricht.lunaris.item.types.WeaponItem;
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
    private ItemType type;
    private ArrayList<AffixPart.Affix> affixes;
    private ArrayList<String> craftedAffixes;
    private ArrayList<String> implicits;

    public boolean exists() {
        return !base.equals("");
    }


    public boolean hasLocalMods() {
        if (getType() instanceof WeaponItem) {
            return true;
        }

        if (getType() instanceof EquipmentItem) {
            EquipmentSlot slot = ((EquipmentItem) getType()).getSlot();
            return slot != EquipmentSlot.RING
                    && slot != EquipmentSlot.AMULET
                    && slot != EquipmentSlot.BELT
                    && slot != EquipmentSlot.FLASK
                    && slot != EquipmentSlot.JEWEL
                    && slot != EquipmentSlot.ABYSS_JEWEL
                    && slot != EquipmentSlot.QUIVER;
        }

        return false;
    }
}
