package dev.tricht.lunaris.item;

import dev.tricht.lunaris.item.parser.*;
import dev.tricht.lunaris.item.types.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ItemParser {

    private String[] lines;

    public ItemParser(String[] lines) {
        this.lines = lines;
    }

    public Item parse() {
        ArrayList<ArrayList<String>> parts = getParts();

        if (parts.size() <= 1) {
            return new Item();
        }

        NamePart namePart = new NamePart(parts.get(0));
        StatsPart statsPart = new StatsPart(parts.get(1));

        ItemType itemType = namePart.getItemType();
        if (itemType instanceof UnknownItem) {
            itemType = statsPart.getWeaponType();
        }

        //If the item is a weapon, parse damage and attackspeed stats for DPS calculation
        if (itemType instanceof WeaponItem){
            log.debug("Weapon found!");

            Pattern digitPattern = Pattern.compile("(\\d+\\.?\\d*)");

            for(String stat : parts.get(1)){
                Matcher m = digitPattern.matcher(stat);
                ArrayList<Double> digits = new ArrayList<>();
                while(m.find()){
                    digits.add(Double.parseDouble(m.group(1)));
                }

                if(stat.contains("Physical Damage")){
                    ((WeaponItem) itemType).setPhysMin(digits.get(0));
                    ((WeaponItem) itemType).setPhysMax(digits.get(1));
                }
                else if(stat.contains("Elemental Damage")){
                    ((WeaponItem) itemType).setFireMin(digits.get(0));
                    ((WeaponItem) itemType).setFireMax(digits.get(1));
                    if(digits.size()>2){
                        ((WeaponItem) itemType).setColdMin(digits.get(2));
                        ((WeaponItem) itemType).setColdMax(digits.get(3));
                    }
                    if(digits.size()>4){
                        ((WeaponItem) itemType).setLightningMin(digits.get(4));
                        ((WeaponItem) itemType).setLightningMax((digits.get(5)));
                    }
                }
                else if(stat.contains("Chaos Damage")){
                    ((WeaponItem) itemType).setChaosMin(digits.get(0));
                    ((WeaponItem) itemType).setChaosMax(digits.get(1));
                }
                else if(stat.contains("Attacks per Second")){
                    ((WeaponItem) itemType).setAtkSpeed(digits.get(0));
                }
            }

            ((WeaponItem) itemType).calcTotalDPS();

        }
        if (itemType instanceof MapItem) {
            ((MapItem) itemType).setTier(statsPart.getMapTier());
        }

        //TODO: Prophecy

        ItemProps itemProps = new ItemPropsParts(parts).getProps();

        int affixIndex = new AffixPartIndexCalculator(namePart.getRarity(), itemType, itemProps, parts).getAffixIndex();
        AffixPart affixPart = new AffixPart(parts.get(affixIndex));

        ImplicitPart implicitPart = new ImplicitPart(parts.get(affixIndex - 1));
        ArrayList<String> implicits = implicitPart.getImplicits();
        if (implicitPart.getImplicits().size() > 0 && implicitPart.isRealImplicit()) {
            implicits.addAll(new ImplicitPart(parts.get(affixIndex - 2)).getImplicits());
        }

        // TODO: Abyssal sockets

        Item item = new Item();
        item.setType(itemType);
        item.setRarity(namePart.getRarity());
        item.setBase(namePart.getNameWithoutAffixes(affixPart.getAffixes(), itemProps.isIdentified()));
        item.setAffixes(affixPart.getAffixes());
        item.setProps(itemProps);
        item.setName(namePart.getItemName());
        item.setImplicits(implicits);

        if (itemType instanceof GemItem) {
            ((GemItem) itemType).setLevel(statsPart.getGemLevel());
            if (statsPart.isVaal()) {
                item.setBase("Vaal " + item.getBase());
            }
        }

        return item;
    }

    public ArrayList<ArrayList<String>> getParts() {
        ArrayList<ArrayList<String>> parts = new ArrayList<>();

        ArrayList<String> currentPart = new ArrayList<>();
        boolean notEquippable = false;
        for (String line : lines) {
            if (line.equals("You cannot use this item. Its stats will be ignored")) {
                notEquippable = true;
                continue;
            }
            if (line.equals("--------")) {
                if (notEquippable && parts.size() == 0) {
                    notEquippable = false;
                    continue;
                }
                parts.add(currentPart);
                currentPart = new ArrayList<>();
                continue;
            }
            currentPart.add(line);
        }
        parts.add(currentPart);

        return parts;
    }
}
