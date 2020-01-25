package dev.tricht.lunaris.item.parser;

import dev.tricht.lunaris.item.types.ItemType;
import dev.tricht.lunaris.item.types.UnknownItem;
import dev.tricht.lunaris.item.types.WeaponItem;
import dev.tricht.lunaris.item.types.WeaponType;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatsPart {
    private ArrayList<String> lines;

    public StatsPart(ArrayList<String> lines) {
        this.lines = lines;
    }

    public ItemType getWeaponType() {
        String name = lines.get(0);

        WeaponItem weaponItem = new WeaponItem();

        if (name.equals("One Handed Axe")) {
            weaponItem.setType(WeaponType.AXE_ONE_HANDED);
        }
        if (name.equals("Two Handed Axe")) {
            weaponItem.setType(WeaponType.AXE_TWO_HANDED);
        }
        if (name.equals("Bow")) {
            weaponItem.setType(WeaponType.BOW);
        }
        if (name.equals("Claw")) {
            weaponItem.setType(WeaponType.CLAW);
        }
        if (name.equals("Dagger") || name.equals("Rune Dagger")) {
            weaponItem.setType(WeaponType.DAGGER);
        }
        if (name.equals("One Handed Mace")) {
            weaponItem.setType(WeaponType.MACE_ONE_HANDED);
        }
        if (name.equals("Two Handed Mace")) {
            weaponItem.setType(WeaponType.MACE_TWO_HANDED);
        }
        if (name.equals("Warstaff")) {
            weaponItem.setType(WeaponType.WAR_STAFF);
        }
        if (name.equals("Staff")) {
            weaponItem.setType(WeaponType.STAFF);
        }
        if (name.equals("One Handed Sword")) {
            weaponItem.setType(WeaponType.SWORD_ONE_HANDED);
        }
        if (name.equals("Two Handed Sword")) {
            weaponItem.setType(WeaponType.SWORD_TWO_HANDED);
        }
        if (name.equals("Wand")) {
            weaponItem.setType(WeaponType.WAND);
        }
        if (name.equals("Sceptre")) {
            weaponItem.setType(WeaponType.SCEPTRE);
        }
        if (name.equals("Fishing Rod")) {
            weaponItem.setType(WeaponType.FISHING_ROD);
        }

        if(weaponItem.getType()!=null){
            parseWeaponStats(weaponItem);
            return weaponItem;
        } else return new UnknownItem();

    }

    private void parseWeaponStats(WeaponItem weaponItem){
        Pattern digitPattern = Pattern.compile("(\\d+\\.?\\d*)");

        for(String stat : lines){
            Matcher m = digitPattern.matcher(stat);
            ArrayList<Double> digits = new ArrayList<>();
            while(m.find()){
                digits.add(Double.parseDouble(m.group(1)));
            }

            if(stat.contains("Physical Damage")){
                weaponItem.setPhysMin(digits.get(0));
                weaponItem.setPhysMax(digits.get(1));
            }
            else if(stat.contains("Elemental Damage")){
                weaponItem.setFireMin(digits.get(0));
                weaponItem.setFireMax(digits.get(1));
                if(digits.size()>2){
                    weaponItem.setColdMin(digits.get(2));
                    weaponItem.setColdMax(digits.get(3));
                }
                if(digits.size()>4){
                    weaponItem.setLightningMin(digits.get(4));
                    weaponItem.setLightningMax((digits.get(5)));
                }
            }
            else if(stat.contains("Chaos Damage")){
                weaponItem.setChaosMin(digits.get(0));
                weaponItem.setChaosMax(digits.get(1));
            }
            else if(stat.contains("Attacks per Second")){
                weaponItem.setAtkSpeed(digits.get(0));
            }
        }
        weaponItem.calcTotalDPS();

    }
    public int getMapTier() {
        String name = lines.get(0);

        return Integer.parseInt(name.split("Map Tier: ")[1]);
    }

    public int getGemLevel() {
        String level = lines.get(1);

        return Integer.parseInt(level.split(" ")[1]);
    }

    public boolean isVaal() {
        return lines.get(0).contains("Vaal");
    }
}
