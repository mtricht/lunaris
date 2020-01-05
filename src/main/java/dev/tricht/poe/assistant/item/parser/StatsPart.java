package dev.tricht.poe.assistant.item.parser;

import dev.tricht.poe.assistant.item.types.*;

import java.util.ArrayList;

public class StatsPart {


    private ArrayList<String> lines;

    public StatsPart(ArrayList<String> lines) {
        this.lines = lines;
    }

    public ItemType getWeaponType() {
        String name = lines.get(0);

        if (name.matches("One Handed Axe")) {
            return new WeaponItem(WeaponType.AXE_ONE_HANDED);
        }
        if (name.matches("Two Handed Axe")) {
            return new WeaponItem(WeaponType.AXE_TWO_HANDED);
        }
        if (name.matches("Bow")) {
            return new WeaponItem(WeaponType.BOW);
        }
        if (name.matches("Claw")) {
            return new WeaponItem(WeaponType.CLAW);
        }
        if (name.matches("Dagger")) {
            return new WeaponItem(WeaponType.DAGGER);
        }
        if (name.matches("One Handed Mace")) {
            return new WeaponItem(WeaponType.MACE_ONE_HANDED);
        }
        if (name.matches("Two Handed Mace")) {
            return new WeaponItem(WeaponType.MACE_TWO_HANDED);
        }
        if (name.matches("Warstaff")) {
            return new WeaponItem(WeaponType.WAR_STAFF);
        }
        if (name.matches("Staff")) {
            return new WeaponItem(WeaponType.STAFF);
        }
        if (name.matches("One Handed Sword")) {
            return new WeaponItem(WeaponType.SWORD_ONE_HANDED);
        }
        if (name.matches("Two Handed Sword")) {
            return new WeaponItem(WeaponType.SWORD_TWO_HANDED);
        }
        if (name.matches("Wand")) {
            return new WeaponItem(WeaponType.WAND);
        }
        if (name.matches("Sceptre")) {
            return new WeaponItem(WeaponType.SCEPTRE);
        }
        if (name.matches("Fishing Rod")) {
            return new WeaponItem(WeaponType.FISHING_ROD);
        }

        return new UnknownItem();
    }
}
