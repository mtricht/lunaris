package dev.tricht.poe.assistant.item.parser;

import dev.tricht.poe.assistant.item.types.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class StatsPart {


    private ArrayList<String> lines;

    public StatsPart(ArrayList<String> lines) {
        this.lines = lines;
    }

    public ItemType getWeaponType() {
        String name = lines.get(0);

        if (name.equals("One Handed Axe")) {
            return new WeaponItem(WeaponType.AXE_ONE_HANDED);
        }
        if (name.equals("Two Handed Axe")) {
            return new WeaponItem(WeaponType.AXE_TWO_HANDED);
        }
        if (name.equals("Bow")) {
            return new WeaponItem(WeaponType.BOW);
        }
        if (name.equals("Claw")) {
            return new WeaponItem(WeaponType.CLAW);
        }
        if (name.equals("Dagger")) {
            return new WeaponItem(WeaponType.DAGGER);
        }
        if (name.equals("One Handed Mace")) {
            return new WeaponItem(WeaponType.MACE_ONE_HANDED);
        }
        if (name.equals("Two Handed Mace")) {
            return new WeaponItem(WeaponType.MACE_TWO_HANDED);
        }
        if (name.equals("Warstaff")) {
            return new WeaponItem(WeaponType.WAR_STAFF);
        }
        if (name.equals("Staff")) {
            return new WeaponItem(WeaponType.STAFF);
        }
        if (name.equals("One Handed Sword")) {
            return new WeaponItem(WeaponType.SWORD_ONE_HANDED);
        }
        if (name.equals("Two Handed Sword")) {
            return new WeaponItem(WeaponType.SWORD_TWO_HANDED);
        }
        if (name.equals("Wand")) {
            return new WeaponItem(WeaponType.WAND);
        }
        if (name.equals("Sceptre")) {
            return new WeaponItem(WeaponType.SCEPTRE);
        }
        if (name.equals("Fishing Rod")) {
            return new WeaponItem(WeaponType.FISHING_ROD);
        }

        return new UnknownItem();
    }

    public int getMapTier() {
        String name = lines.get(0);

        return Integer.parseInt(name.split("Map Tier: ")[1]);
    }
}
