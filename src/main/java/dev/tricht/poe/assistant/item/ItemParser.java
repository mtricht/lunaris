package dev.tricht.poe.assistant.item;

import dev.tricht.poe.assistant.item.parser.*;
import dev.tricht.poe.assistant.item.types.ItemType;
import dev.tricht.poe.assistant.item.types.MapItem;
import dev.tricht.poe.assistant.item.types.UnknownItem;

import java.util.ArrayList;

public class ItemParser {

    private String[] lines;

    public ItemParser(String[] lines) {
        this.lines = lines;
    }

    public Item parse() {
        ArrayList<ArrayList<String>> parts = getParts();

        if(parts.size() <= 1) {
            return new Item();
        }

        NamePart namePart = new NamePart(parts.get(0));
        StatsPart statsPart = new StatsPart(parts.get(1));

        ItemType itemType = namePart.getItemType();
        if (itemType instanceof UnknownItem) {
            itemType = statsPart.getWeaponType();
        }

        if (itemType instanceof MapItem) {
            ((MapItem) itemType).setTier(statsPart.getMapTier());
        }

        //TODO: Prophecy

        ItemProps itemProps = new ItemPropsParts(parts).getProps();

        AffixPart affixPart = new AffixPart(parts.get(new AffixPartIndexCalculator(namePart.getRarity(), itemType, itemProps, parts).getAffixIndex()));

        // TODO: Quality
        // TODO: Gem lvl
        // TODO: Links
        // TODO: Sockets
        // TODO: Abyssal sockets

        Item item = new Item();
        item.setType(itemType);
        item.setRarity(namePart.getRarity());
        item.setBase(namePart.getNameWithoutAffixes(affixPart.getAffixes(), itemProps.isIdentified()));
        item.setAffixes(affixPart.getAffixes());
        item.setProps(itemProps);

        return item;
    }

    public ArrayList<ArrayList<String>> getParts() {
        ArrayList<ArrayList<String>> parts = new ArrayList<ArrayList<String>>();

        ArrayList<String> currentPart = new ArrayList<String>();
        for (String line : lines) {
            if (line.equals("--------")) {
                parts.add(currentPart);
                currentPart = new ArrayList<String>();
                continue;
            }
            currentPart.add(line);
        }
        parts.add(currentPart);

        return parts;
    }
}
