package dev.tricht.lunaris.item;

import dev.tricht.lunaris.item.types.EquipmentItem;
import dev.tricht.lunaris.item.types.ItemType;
import dev.tricht.lunaris.item.types.WeaponItem;

import java.util.List;
import java.util.regex.Pattern;

public class LocalAffixResolver {
    private final static String LOCAL = " (Local)";
    final static List<Pattern> localArmorAffixes = List.of(
            Pattern.compile(".* to Armour"),
            Pattern.compile(".* to Evasion Rating"),
            Pattern.compile(".* to maximum Energy Shield"),
            Pattern.compile(".* increased Armour"),
            Pattern.compile(".* increased Evasion Rating"),
            Pattern.compile(".* increased Energy Shield"),
            Pattern.compile(".* increased Armour and Evasion"),
            Pattern.compile(".* increased Armour and Energy Shield"),
            Pattern.compile(".* increased Evasion and Energy Shield"),
            Pattern.compile(".* increased increased Armour, Evasion and Energy Shield"));
    final static List<Pattern> localWeaponAffixes = List.of(
            Pattern.compile("Adds .* to .* Physical Damage"),
            Pattern.compile("Adds .* to .* Chaos Damage"),
            Pattern.compile("Adds .* to .* Cold Damage"),
            Pattern.compile("Adds .* to .* Fire Damage"),
            Pattern.compile("Adds .* to .* Lightning Damage"),
            Pattern.compile(".* to Accuracy Rating"),
            Pattern.compile(".* chance to Poison on Hit"),
            Pattern.compile(".* increased Attack Speed"));

    public static String resolve(String affix, ItemType itemType) {
        if ((itemType instanceof EquipmentItem) || (itemType instanceof WeaponItem)) {
            if (itemType instanceof EquipmentItem && localArmorAffixes.stream().anyMatch(p -> p.matcher(affix).matches())) {
                return affix + LOCAL;
            } else if (itemType instanceof  WeaponItem && localWeaponAffixes.stream().anyMatch(p -> p.matcher(affix).matches())) {
                return affix + LOCAL;
            }
        }
        return affix;
    }
}
