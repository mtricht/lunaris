package dev.tricht.lunaris.item.parser;

import dev.tricht.lunaris.item.ItemProps;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.*;

import java.util.ArrayList;

public class AffixPartIndexCalculator {


    private ItemRarity itemRarity;
    private ArrayList<ArrayList<String>> parts;
    private ItemType itemType;
    private ItemProps itemProps;


    public AffixPartIndexCalculator(ItemRarity rarity, ItemType type, ItemProps itemProps, ArrayList<ArrayList<String>> parts) {
        this.itemRarity = rarity;
        this.itemType = type;
        this.itemProps = itemProps;
        this.parts = parts;
    }

    public int getAffixIndex() {
        return parts.size() - getNegativeOffset() - 1;
    }

    private int getNegativeOffset() {
        int offset = 0;

        if (itemType instanceof EquipmentItem) {
            if (((EquipmentItem) itemType).getSlot() == EquipmentSlot.ABYSS_JEWEL) {
                offset += 1;
            }
            if (((EquipmentItem) itemType).getSlot() == EquipmentSlot.JEWEL) {
                offset += 1;
            }
            if (((EquipmentItem) itemType).getSlot() == EquipmentSlot.FLASK) {
                offset += 1;
            }
        }

        if (itemType instanceof MapItem) {
            offset += 1;
        }

        if (itemType instanceof CurrencyItem) {
            offset += 1;
        }

        //TODO: Use enum
        if (itemRarity == ItemRarity.UNIQUE) {
            offset += 1;
        }

        if(itemProps.getNote() != null) {
            offset +=1 ;
        }

        if(itemProps.isInfluenced()) {
            offset +=1 ;
        }

        if (itemProps.isCorrupted()) {
            offset += 1;
        }

        if (itemProps.isMirrored()) {
            offset += 1;
        }


        //TODO: Talisman
        //TODO: HasEffect?
        //TODO: fking tabula rasa

        return offset;
    }
}
