package dev.tricht.lunaris.item.parser;

import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.*;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class NamePart {

    private ArrayList<String> lines;

    private static Pattern fragmentPattern = Pattern.compile("(^Sacrifice At(.*)|^Fragment of(.*)|^Mortal(.*)|^Offering to(.*)|(.*)'s Key$|(.*) Reliquary Key|(.*)Breachstone|Divine Vessel|Timeless (.*) Emblem)");
    private static Pattern beltPattern = Pattern.compile("(.*)(Belt|Stygian Vise|Rustic Sash)");
    private static Pattern amuletPattern = Pattern.compile("(.*)(Amulet|Talisman)");
    private static Pattern jewelPattern = Pattern.compile("(.*)(Cobalt|Crimson|Viridian|Prismatic|Small Cluster|Medium Cluster|Large Cluster) Jewel");
    private static Pattern abyssJewelPattern = Pattern.compile("(.*)(Murderous|Hypnotic|Searching|Ghastly) Eye Jewel");
    private static Pattern shieldPattern = Pattern.compile("(.*)(Buckler|Bundle|Shield)");
    private static Pattern glovesPattern = Pattern.compile("(.*)(Gauntlets|Gloves|Mitts)");
    private static Pattern bootsPattern = Pattern.compile("(.*)(Boots|Greaves|Slippers)");
    private static Pattern helmetPattern = Pattern.compile("(.*)(Bascinet|Burgonet|Cage|Circlet|Crown|Hood|Helm|Helmet|Mask|Sallet|Tricorne|Iron Hat|Leather Cap|Rusted Coif|Wolf Pelt|Ursine Pelt|Lion Pelt)");
    private static Pattern bodyArmourPattern = Pattern.compile("(.*)(Armour|Brigandine|Chainmail|Coat|Doublet|Garb|Hauberk|Jacket|Lamellar|Leather|Plate|Raiment|Regalia|Ringmail|Robe|Tunic|Vest|Vestment|Chestplate|Full Dragonscale|Full Wyrmscale|Necromancer Silks|Shabby Jerkin|Silken Wrap)");
    public NamePart(ArrayList<String> lines) {
        this.lines = lines;
    }

    private String getRarityRaw() {
        return lines.get(0).split("Rarity: ")[1];
    }
    public ItemRarity getRarity() {
        switch(lines.get(0).split("Rarity: ")[1]) {
            case "Magic":
                return ItemRarity.MAGIC;
            case "Rare":
                return ItemRarity.RARE;
            case "Unique":
                return ItemRarity.UNIQUE;
            case "Normal":
            default:
                // Normal items + Currency, div cards, gems
                return ItemRarity.NORMAL;
        }
    }

    public String getFullName() {
        if (lines.size() == 2) {
            return sanitizeName(lines.get(1));
        }

        return sanitizeName(lines.get( lines.size() - 2) + " " + lines.get( lines.size() - 1));
    }

    public String getItemName() {
        if (lines.size() == 2) {
            return "";
        }
        return sanitizeName(lines.get(1));
    }

    public String getBaseName() {
        if (lines.size() == 2) {
            return sanitizeName(lines.get(1));
        }

        return sanitizeName(lines.get(2));
    }

    private String sanitizeName(String name) {
        return name.replace("Superior ", "");
    }

    public ItemType getItemType() {
        int lineNum = 1;
        if (getRarity() == ItemRarity.UNIQUE || getRarity() == ItemRarity.RARE) {
            lineNum = Math.min(2, lines.size() - 1);
        }
        String name = sanitizeName(lines.get(lineNum));

        if (getRarityRaw().equals("Currency")) {
            return new CurrencyItem();
        }

        if (getRarityRaw().equals("Divination Card")) {
            return new DivinitationCardItem();
        }
        if (getRarityRaw().equals("Gem")) {
            return new GemItem();
        }

        if (fragmentPattern.matcher(name).matches()) {
            return new FragmentItem();
        }
        if (name.contains("Scarab")) {
            return new ScarabItem();
        }
        // TODO: Fossils?

        if (beltPattern.matcher(name).matches()) {
            return new EquipmentItem(EquipmentSlot.BELT);
        }
        if (amuletPattern.matcher(name).matches() && !(name.contains("Leaguestone"))) {
            return new EquipmentItem(EquipmentSlot.AMULET);
        }
        if (name.contains("Ring")) {
            return new EquipmentItem(EquipmentSlot.RING);
        }
        if (name.contains("Quiver")) {
            return new EquipmentItem(EquipmentSlot.QUIVER);
        }
        if (name.contains("Flask")) {
            return new EquipmentItem(EquipmentSlot.FLASK);
        }
        if (name.contains("Map")) {
            return new MapItem();
        }
        if (jewelPattern.matcher(name).lookingAt()) {
            return new EquipmentItem(EquipmentSlot.JEWEL);
        }
        if (abyssJewelPattern.matcher(name).matches()) {
            return new EquipmentItem(EquipmentSlot.ABYSS_JEWEL);
        }
        // TODO: Metamorph
        // TODO: Leaguestones

        if (shieldPattern.matcher(name).matches()) {
            return new EquipmentItem(EquipmentSlot.SHIELD);
        }

        if (glovesPattern.matcher(name).matches()) {
            return new EquipmentItem(EquipmentSlot.GLOVES);
        }
        if (bootsPattern.matcher(name).matches()) {
            return new EquipmentItem(EquipmentSlot.BOOTS);
        }
        if (helmetPattern.matcher(name).matches()) {
            return new EquipmentItem(EquipmentSlot.HELMET);
        }

        // Have to check this one after belt because of Leather Belt
        if (bodyArmourPattern.matcher(name).matches()) {
            return new EquipmentItem(EquipmentSlot.BODY_ARMOUR);
        }

        return new UnknownItem();
    }

    public String getNameWithoutAffixes(ArrayList<AffixPart.Affix> affixes, boolean isIdentified) {
        String name = getBaseName();

        if (getRarity() == ItemRarity.MAGIC && isIdentified) {
            String[] parts = name.split(" of ");
            if ((parts.length > 1 && affixes.size() > 1) || (parts.length == 1 && affixes.size() == 1)) {
                return parts[0].substring(parts[0].indexOf(" ") + 1);
            }
            return parts[0];
        }

        return name;
    }
}
