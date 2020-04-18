package dev.tricht.lunaris.item;

import dev.tricht.lunaris.item.types.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ItemParserTest {

    Item parse(String lines) {
        ItemParser parser = new ItemParser(
                lines.split("\n")
        );

        return parser.parse();
    }


    @Test
    void parserPartsForTimelessEmblemFragment() {
        Item item = parse("Rarity: Normal\n" +
                "Timeless Templar Emblem\n" +
                "--------\n" +
                "Place two or more different Emblems in a Map Device to access the Domain of Timeless Conflict. Can only be used once.\n");
        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Timeless Templar Emblem", item.getBase());
        Assertions.assertTrue(item.getType() instanceof FragmentItem);
    }

    @Test
    void parserPartsForMortalFragment() {
        Item item = parse("Rarity: Normal\n" +
                "Mortal Grief\n" +
                "--------\n" +
                "When we prostrate ourselves to the night, we worship mortality.\n" +
                "--------\n" +
                "Can be used in a personal Map Device.\n");
        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Mortal Grief", item.getBase());
        Assertions.assertTrue(item.getType() instanceof FragmentItem);
    }

    @Test
    void parserPartsForHydraFragment() {
        Item item = parse("Rarity: Normal\n" +
                "Fragment of the Hydra\n" +
                "--------\n" +
                "Enter the crucible. The nexus of\n" +
                "nothingness and equilibrium of eternity.\n" +
                "--------\n" +
                "Can be used in a personal Map Device.");
        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Fragment of the Hydra", item.getBase());
        Assertions.assertTrue(item.getType() instanceof FragmentItem);
    }

    @Test
    void parsesPartsForTalisman() {
        Item item = parse("Rarity: Rare\n" +
                "Grim Heart\n" +
                "Wereclaw Talisman\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 44\n" +
                "--------\n" +
                "Item Level: 73\n" +
                "--------\n" +
                "Talisman Tier: 2\n" +
                "--------\n" +
                "+29% to Global Critical Strike Multiplier (implicit)\n" +
                "--------\n" +
                "+23 to all Attributes\n" +
                "+16 to Intelligence\n" +
                "+23% to Global Critical Strike Multiplier\n" +
                "+86 to maximum Life\n" +
                "--------\n" +
                "It's said to be noble to stand one's ground.\n" +
                "To soak the earth in stalwart blood.\n" +
                "While the First Ones chose to laugh and run\n" +
                "and caper with untamed glee.\n" +
                "- The Wolven King\n" +
                "--------\n" +
                "Corrupted\n" +
                "--------\n" +
                "Note: ~price 20 chaos");
        Assertions.assertSame(ItemRarity.RARE, item.getRarity());
        Assertions.assertEquals("Wereclaw Talisman", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertTrue(item.getProps().isCorrupted());
        Assertions.assertTrue(item.getProps().isTalisman());
        Assertions.assertEquals("+29% to Global Critical Strike Multiplier", item.getImplicits().get(0));
        Assertions.assertEquals("+23 to all Attributes", item.getAffixes().get(0).getText());
    }

    @Test
    void parsesPartsForUniqueCorruptedBelt() {
        Item item = parse("Rarity: Unique\n" +
                "String of Servitude\n" +
                "Heavy Belt\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 48\n" +
                "--------\n" +
                "Item Level: 70\n" +
                "--------\n" +
                "30% increased Cast Speed during any Flask Effect (implicit)\n" +
                "--------\n" +
                "Implicit Modifier magnitudes are tripled\n" +
                "--------\n" +
                "For the Vaal, the relationship between slave and master\n" +
                "was as intimate and volatile as that of lovers.\n" +
                "--------\n" +
                "Corrupted\n" +
                "--------\n" +
                "Note: ~price 1 blessed\n");

        Assertions.assertSame(ItemRarity.UNIQUE, item.getRarity());
        Assertions.assertEquals("Heavy Belt", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertTrue(item.getProps().isCorrupted());
        Assertions.assertEquals("30% increased Cast Speed during any Flask Effect", item.getImplicits().get(0));
    }

    @Test
    void parsesPartsForGem() {
        Item item = parse("Rarity: Gem\n" +
                "Swift Affliction Support\n" +
                "--------\n" +
                "Support, Duration\n" +
                "Level: 20 (Max)\n" +
                "Mana Multiplier: 125%\n" +
                "Quality: +20% (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Dex: 111\n" +
                "--------\n" +
                "Supports any skill that has a duration, or can hit enemies to inflict ailments on them.\n" +
                "--------\n" +
                "Supported Skills deal 44% more Damage over Time\n" +
                "Supported Skills deal 10% increased Damage over Time\n" +
                "15% reduced Duration of Supported Skills and Damaging Ailments they inflict\n" +
                "--------\n" +
                "This is a Support Gem. It does not grant a bonus to your character, but to skills in sockets connected to it. Place into an item socket connected to a socket containing the Active Skill Gem you wish to augment. Right click to remove from a socket.\n" +
                "--------\n" +
                "Corrupted\n");

        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Swift Affliction Support", item.getBase());
        Assertions.assertTrue(item.getType() instanceof GemItem);
        Assertions.assertSame(20, ((GemItem) item.getType()).getLevel());
        Assertions.assertSame(20, item.getProps().getQuality());
        Assertions.assertTrue(item.getProps().isCorrupted());
    }

    @Test
    void parsesPartsForUniqueHelmet() {
        Item item = parse("Rarity: Unique\n" +
                "Goldrim\n" +
                "Leather Cap\n" +
                "--------\n" +
                "Evasion Rating: 65 (augmented)\n" +
                "--------\n" +
                "Sockets: G-G \n" +
                "--------\n" +
                "Item Level: 78\n" +
                "--------\n" +
                "+46 to Evasion Rating\n" +
                "10% increased Rarity of Items found\n" +
                "+38% to all Elemental Resistances\n" +
                "Reflects 4 Physical Damage to Melee Attackers\n" +
                "--------\n" +
                "No metal slips as easily through the fingers as gold."
        );

        Assertions.assertSame(ItemRarity.UNIQUE, item.getRarity());
        Assertions.assertEquals("Leather Cap", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.HELMET, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(4, item.getAffixes().size());
    }

    @Test
    void parsesPartsForUniqueSixLinkedBodyArmour() {
        Item item = parse("Rarity: Unique\n" +
                "Hyrri's Ire\n" +
                "Zodiac Leather\n" +
                "--------\n" +
                "Quality: +28% (augmented)\n" +
                "Evasion Rating: 2801 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Dex: 197\n" +
                "Int: 155\n" +
                "--------\n" +
                "Sockets: B-B-B-B-G-G \n" +
                "--------\n" +
                "Item Level: 75\n" +
                "--------\n" +
                "+49 to Dexterity\n" +
                "200% increased Evasion Rating\n" +
                "25% increased Chill Duration on Enemies\n" +
                "Adds 130 to 185 Cold Damage to Bow Attacks\n" +
                "10% chance to Dodge Attack Hits\n" +
                "10% chance to Dodge Spell Hits\n" +
                "--------\n" +
                "Hyrri loosed a barrage of arrows,\n" +
                "tipped with a poisoned hatred\n" +
                "only oppression can ferment.\n"
        );

        Assertions.assertSame(ItemRarity.UNIQUE, item.getRarity());
        Assertions.assertEquals("Zodiac Leather", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.BODY_ARMOUR, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(6, item.getAffixes().size());
        Assertions.assertEquals(6, item.getProps().getLinks());
    }

    @Test
    void parsesPartsForUniqueFiveLinkedBodyArmour() {
        Item item = parse("Rarity: Unique\n" +
                "Hyrri's Ire\n" +
                "Zodiac Leather\n" +
                "--------\n" +
                "Quality: +28% (augmented)\n" +
                "Evasion Rating: 2801 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Dex: 197\n" +
                "Int: 155\n" +
                "--------\n" +
                "Sockets: B B-B-B-G-G \n" +
                "--------\n" +
                "Item Level: 75\n" +
                "--------\n" +
                "+49 to Dexterity\n" +
                "200% increased Evasion Rating\n" +
                "25% increased Chill Duration on Enemies\n" +
                "Adds 130 to 185 Cold Damage to Bow Attacks\n" +
                "10% chance to Dodge Attack Hits\n" +
                "10% chance to Dodge Spell Hits\n" +
                "--------\n" +
                "Hyrri loosed a barrage of arrows,\n" +
                "tipped with a poisoned hatred\n" +
                "only oppression can ferment.\n"
        );

        Assertions.assertSame(ItemRarity.UNIQUE, item.getRarity());
        Assertions.assertEquals("Zodiac Leather", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.BODY_ARMOUR, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(6, item.getAffixes().size());
        Assertions.assertEquals(5, item.getProps().getLinks());
    }

    @Test
    void parsesPartsForUniqueCorrupted1HSword() {
        Item item = parse("Rarity: Unique\n" +
                "Innsbury Edge\n" +
                "Elder Sword\n" +
                "--------\n" +
                "One Handed Sword\n" +
                "Quality: +20% (augmented)\n" +
                "Physical Damage: 84-154 (augmented)\n" +
                "Critical Strike Chance: 5.00%\n" +
                "Attacks per Second: 1.56 (augmented)\n" +
                "Weapon Range: 11\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 47\n" +
                "Str: 81\n" +
                "Dex: 81\n" +
                "--------\n" +
                "Sockets: R-R R \n" +
                "--------\n" +
                "Item Level: 80\n" +
                "--------\n" +
                "15% chance to gain Onslaught for 4 seconds on Kill (implicit)\n" +
                "--------\n" +
                "113% increased Physical Damage\n" +
                "20% increased Attack Speed\n" +
                "0.2% of Chaos Damage Leeched as Life\n" +
                "25% of Physical Damage Converted to Chaos Damage\n" +
                "Attacks with this Weapon Maim on hit\n" +
                "--------\n" +
                "A sword he brought, his foes to maim and rend,\n" +
                "from places dark behind forbidden doors,\n" +
                "but night by night he woke with frighten'd roars\n" +
                "from ghoulish dreams, too strange to comprehend.\n" +
                "--------\n" +
                "Corrupted\n"
        );

        Assertions.assertSame(ItemRarity.UNIQUE, item.getRarity());
        Assertions.assertEquals("Elder Sword", item.getBase());
        Assertions.assertTrue(item.getType() instanceof WeaponItem);
        Assertions.assertNotNull(((WeaponItem) item.getType()).getType());
        Assertions.assertEquals(WeaponType.SWORD_ONE_HANDED, ((WeaponItem) item.getType()).getType());
        Assertions.assertEquals(5, item.getAffixes().size());
        Assertions.assertTrue(item.getProps().isCorrupted());
    }


    @Test
    void parsesPartsForMagicMap() {
        Item item = parse("Rarity: Magic\n" +
                "Ceremonial Tropical Island Map of Vulnerability\n" +
                "--------\n" +
                "Map Tier: 14\n" +
                "Item Quantity: +31% (augmented)\n" +
                "Item Rarity: +14% (augmented)\n" +
                "Monster Pack Size: +9% (augmented)\n" +
                "Quality: +8% (augmented)\n" +
                "--------\n" +
                "Item Level: 82\n" +
                "--------\n" +
                "Area contains many Totems\n" +
                "Players are Cursed with Vulnerability\n" +
                "--------\n" +
                "Travel to this Map by using it in a personal Map Device. Maps can only be used once.\n"
        );

        Assertions.assertSame(ItemRarity.MAGIC, item.getRarity());
        Assertions.assertEquals("Tropical Island Map", item.getBase());
        Assertions.assertTrue(item.getType() instanceof MapItem);
        Assertions.assertEquals(2, item.getAffixes().size());
    }

    @Test
    void parsesPartsForNormalMap() {
        Item item = parse("Rarity: Normal\n" +
                "Overgrown Shrine Map\n" +
                "--------\n" +
                "Map Tier: 16\n" +
                "--------\n" +
                "Item Level: 84\n" +
                "--------\n" +
                "Travel to this Map by using it in a personal Map Device. Maps can only be used once.\n"
        );

        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Overgrown Shrine Map", item.getBase());
        Assertions.assertTrue(item.getType() instanceof MapItem);
        //TODO: Maybe fix?
        //Assertions.assertEquals(0, item.getAffixes().size());
    }

    @Test
    void parsesPartsForNormalMapUnidentified() {
        Item item = parse("Rarity: Magic\n" +
                "Summit Map\n" +
                "--------\n" +
                "Map Tier: 16\n" +
                "--------\n" +
                "Item Level: 84\n" +
                "--------\n" +
                "Unidentified\n" +
                "--------\n" +
                "Travel to this Map by using it in a personal Map Device. Maps can only be used once.\n"
        );

        Assertions.assertSame(ItemRarity.MAGIC, item.getRarity());
        Assertions.assertEquals("Summit Map", item.getBase());
        Assertions.assertTrue(item.getType() instanceof MapItem);
        //TODO: Maybe fix?
        //Assertions.assertEquals(0, item.getAffixes().size());
    }

    @Test
    void parsesPartsForUnidentifiedRareMapWithQuality() {
        Item item = parse("Rarity: Rare\n" +
                "Superior Colonnade Map\n" +
                "--------\n" +
                "Map Tier: 16\n" +
                "Item Quantity: +6% (augmented)\n" +
                "Quality: +6% (augmented)\n" +
                "--------\n" +
                "Item Level: 85\n" +
                "--------\n" +
                "Unidentified\n" +
                "--------\n" +
                "Travel to this Map by using it in a personal Map Device. Maps can only be used once.\n"
        );

        Assertions.assertSame(ItemRarity.RARE, item.getRarity());
        Assertions.assertEquals("Colonnade Map", item.getBase());
        Assertions.assertTrue(item.getType() instanceof MapItem);
        Assertions.assertFalse(item.getProps().isIdentified());
    }

    @Test
    void parsesPartsForChaosOrbCurrency() {
        Item item = parse("Rarity: Currency\n" +
                "Chaos Orb\n" +
                "--------\n" +
                "Stack Size: 10/10\n" +
                "--------\n" +
                "Reforges a rare item with new random modifiers\n" +
                "--------\n" +
                "Right click this item then left click a rare item to apply it.\n" +
                "Shift click to unstack.\n"
        );

        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Chaos Orb", item.getBase());
        Assertions.assertTrue(item.getType() instanceof CurrencyItem);
        Assertions.assertEquals(1, item.getAffixes().size());
    }

    @Test
    void parsesPartsForMagicFlaskWithTwoAffixes() {
        Item item = parse("Rarity: Magic\n" +
                "Perpetual Quicksilver Flask of Adrenaline\n" +
                "--------\n" +
                "Quality: +20% (augmented)\n" +
                "Lasts 4.80 (augmented) Seconds\n" +
                "Consumes 20 of 50 Charges on use\n" +
                "Currently has 30 Charges\n" +
                "40% increased Movement Speed\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 4\n" +
                "--------\n" +
                "Item Level: 42\n" +
                "--------\n" +
                "21% increased Charge Recovery\n" +
                "30% increased Movement Speed during Flask effect\n" +
                "--------\n" +
                "Right click to drink. Can only hold charges while in belt. Refills as you kill monsters.\n"
        );

        Assertions.assertSame(ItemRarity.MAGIC, item.getRarity());
        Assertions.assertEquals("Quicksilver Flask", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.FLASK, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(2, item.getAffixes().size());
    }

    @Test
    void parsesPartsForMagicFlaskWithOnlySuffix() {
        Item item = parse("Rarity: Magic\n" +
                "Quicksilver Flask of Adrenaline\n" +
                "--------\n" +
                "Quality: +20% (augmented)\n" +
                "Lasts 4.80 (augmented) Seconds\n" +
                "Consumes 20 of 50 Charges on use\n" +
                "Currently has 30 Charges\n" +
                "40% increased Movement Speed\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 4\n" +
                "--------\n" +
                "Item Level: 42\n" +
                "--------\n" +
                "30% increased Movement Speed during Flask effect\n" +
                "--------\n" +
                "Right click to drink. Can only hold charges while in belt. Refills as you kill monsters.\n"
        );

        Assertions.assertSame(ItemRarity.MAGIC, item.getRarity());
        Assertions.assertEquals("Quicksilver Flask", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.FLASK, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(1, item.getAffixes().size());
    }

    @Test
    void parsesPartsForMagicFlaskWithOnlyPrefix() {
        Item item = parse("Rarity: Magic\n" +
                "Perpetual Quicksilver Flask\n" +
                "--------\n" +
                "Quality: +20% (augmented)\n" +
                "Lasts 4.80 (augmented) Seconds\n" +
                "Consumes 20 of 50 Charges on use\n" +
                "Currently has 30 Charges\n" +
                "40% increased Movement Speed\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 4\n" +
                "--------\n" +
                "Item Level: 42\n" +
                "--------\n" +
                "21% increased Charge Recovery\n" +
                "--------\n" +
                "Right click to drink. Can only hold charges while in belt. Refills as you kill monsters.\n"
        );

        Assertions.assertSame(ItemRarity.MAGIC, item.getRarity());
        Assertions.assertEquals("Quicksilver Flask", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.FLASK, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(1, item.getAffixes().size());
    }

    @Test
    void parsesPartsForUniqueFlask() {
        Item item = parse("Rarity: Unique\n" +
                "Witchfire Brew\n" +
                "Stibnite Flask\n" +
                "--------\n" +
                "Lasts 5.00 Seconds\n" +
                "Consumes 15 (augmented) of 30 Charges on use\n" +
                "Currently has 30 Charges\n" +
                "100% increased Evasion Rating\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 48\n" +
                "--------\n" +
                "Item Level: 73\n" +
                "--------\n" +
                "Creates a Smoke Cloud on Use (implicit)\n" +
                "--------\n" +
                "50% increased Charges used\n" +
                "34% increased Damage Over Time during Flask Effect\n" +
                "Grants Level 21 Despair Curse Aura during Flask Effect\n" +
                "--------\n" +
                "\"Think of those that cursed us, judged us, \n" +
                "and burned our sisters upon the pyre. \n" +
                "Think of their names as you drink, \n" +
                "and even their children will feel what we do to them today.\" \n" +
                "-Vadinya, to her coven\n" +
                "--------\n" +
                "Right click to drink. Can only hold charges while in belt. Refills as you kill monsters.\n"
        );

        Assertions.assertSame(ItemRarity.UNIQUE, item.getRarity());
        Assertions.assertEquals("Stibnite Flask", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.FLASK, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(3, item.getAffixes().size());
    }

    @Test
    void parsesPartsForRareAmuletWithEnchantAndInfluence() {
        Item item = parse("Rarity: Rare\n" +
                "Dire Scarab\n" +
                "Jade Amulet\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 65\n" +
                "--------\n" +
                "Item Level: 83\n" +
                "--------\n" +
                "Allocates Freedom of Movement\n" +
                "--------\n" +
                "+24 to Dexterity (implicit)\n" +
                "--------\n" +
                "+13 to Intelligence\n" +
                "6% increased Evasion Rating\n" +
                "+12% to all Elemental Resistances\n" +
                "+18% to Fire Resistance\n" +
                "+1 to Level of all Chaos Skill Gems\n" +
                "+50 to maximum Life (crafted)\n" +
                "--------\n" +
                "Hunter Item\n"
        );

        Assertions.assertSame(ItemRarity.RARE, item.getRarity());
        Assertions.assertEquals("Jade Amulet", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.AMULET, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(ItemInfluence.HUNTER, item.getProps().getInfluence());
        Assertions.assertEquals(6, item.getAffixes().size());
        //TODO: Check enchant
    }

    @Test
    void parsesPartsForCorruptedMirroredRareJewel() {
        Item item = parse("Rarity: Rare\n" +
                "Fulgent Blood\n" +
                "Crimson Jewel\n" +
                "--------\n" +
                "Item Level: 82\n" +
                "--------\n" +
                "+6 to all Attributes\n" +
                "+14% to Melee Critical Strike Multiplier\n" +
                "+18% to Critical Strike Multiplier with Fire Skills\n" +
                "14% increased Mana Regeneration Rate\n" +
                "--------\n" +
                "Place into an allocated Jewel Socket on the Passive Skill Tree. Right click to remove from the Socket.\n" +
                "--------\n" +
                "Mirrored\n" +
                "--------\n" +
                "Corrupted\n"
        );

        Assertions.assertSame(ItemRarity.RARE, item.getRarity());
        Assertions.assertEquals("Crimson Jewel", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.JEWEL, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(4, item.getAffixes().size());
        Assertions.assertTrue(item.getProps().isCorrupted());
        Assertions.assertTrue(item.getProps().isMirrored());
    }

    @Test
    void parsesPartsForMagicJewel() {
        Item item = parse("Rarity: Magic\n" +
                "Deflecting Cobalt Jewel of Berserking\n" +
                "--------\n" +
                "Item Level: 72\n" +
                "--------\n" +
                "+1% Chance to Block Attack Damage while wielding a Staff\n" +
                "4% increased Attack Speed\n" +
                "--------\n" +
                "Place into an allocated Jewel Socket on the Passive Skill Tree. Right click to remove from the Socket.\n"
        );

        Assertions.assertSame(ItemRarity.MAGIC, item.getRarity());
        Assertions.assertEquals("Cobalt Jewel", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.JEWEL, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(2, item.getAffixes().size());
    }

    @Test
    void parsePartsForRareSmallClusterJewel() {
        Item item = parse("Rarity: Rare\n" +
                "Luminous Desire\n" +
                "Small Cluster Jewel\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 58\n" +
                "--------\n" +
                "Item Level: 83\n" +
                "--------\n" +
                "Adds 3 Passive Skills (enchant)\n" +
                "Added Small Passive Skills grant: 1% chance to Dodge Attack Hits (enchant)\n" +
                "--------\n" +
                "Added Small Passive Skills also grant: +24 to Armour\n" +
                "Added Small Passive Skills also grant: +7 to Dexterity\n" +
                "1 Added Passive Skill is Darting Movements\n" +
                "--------\n" +
                "Place into an allocated Small, Medium or Large Jewel Socket on the Passive Skill Tree. Added passives do not interact with jewel radiuses. Right click to remove from the Socket.\n"
                );

        Assertions.assertSame(ItemRarity.RARE, item.getRarity());
        Assertions.assertEquals("Small Cluster Jewel", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.JEWEL, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(3, item.getAffixes().size());
    }

    @Test
    void parsePartsForMagicLargeClusterJewel() {
        Item item = parse("Rarity: Magic\n" +
                "Notable Large Cluster Jewel of Possibility\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 60\n" +
                "--------\n" +
                "Item Level: 77\n" +
                "--------\n" +
                "Adds 10 Passive Skills (enchant)\n" +
                "Added Small Passive Skills grant: 10% increased Spell Damage (enchant)\n" +
                "--------\n" +
                "1 Added Passive Skill is Practiced Caster\n" +
                "2 Added Passive Skills are Jewel Sockets\n" +
                "--------\n" +
                "Place into an allocated Large Jewel Socket on the Passive Skill Tree. Added passives do not interact with jewel radiuses. Right click to remove from the Socket.\n"
        );

        Assertions.assertSame(ItemRarity.MAGIC, item.getRarity());
        Assertions.assertEquals("Large Cluster Jewel", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.JEWEL, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(2, item.getAffixes().size());
    }

    @Test
    void parsesPartsForRareBootsWithInfluenceForSale() {
        Item item = parse("Rarity: Rare\n" +
                "Sorrow League\n" +
                "Two-Toned Boots\n" +
                "--------\n" +
                "Quality: +5% (augmented)\n" +
                "Armour: 132 (augmented)\n" +
                "Energy Shield: 25 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Str: 62\n" +
                "Int: 62\n" +
                "--------\n" +
                "Sockets: R-R B \n" +
                "--------\n" +
                "Item Level: 80\n" +
                "--------\n" +
                "+12% to Fire and Lightning Resistances (implicit)\n" +
                "--------\n" +
                "Socketed Gems are Supported by Level 18 Spell Totem\n" +
                "+62 to maximum Life\n" +
                "+41% to Cold Resistance\n" +
                "+31% to Lightning Resistance\n" +
                "20% increased Movement Speed\n" +
                "8% increased Totem Placement speed\n" +
                "Unaffected by Shocked Ground\n" +
                "--------\n" +
                "Elder Item\n" +
                "--------\n" +
                "Note: ~price 1 exa\n"
        );

        Assertions.assertSame(ItemRarity.RARE, item.getRarity());
        Assertions.assertEquals("Two-Toned Boots", item.getBase());
        Assertions.assertTrue(item.getType() instanceof EquipmentItem);
        Assertions.assertNotNull(((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(EquipmentSlot.BOOTS, ((EquipmentItem) item.getType()).getSlot());
        Assertions.assertEquals(7, item.getAffixes().size());
    }

    @Test
    void parsesLargeStackSize() {
        Item item = parse("Rarity: Currency\n" +
                "Jeweller's Orb\n" +
                "--------\n" +
                "Stack Size: 5,000/20\n" +
                "--------\n" +
                "Reforges the number of sockets on an item\n" +
                "--------\n" +
                "Right click this item then left click a socketed item to apply it. The item's quality increases the chances of obtaining more sockets.\n" +
                "Shift click to unstack.\n"
        );

        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Jeweller's Orb", item.getBase());
        Assertions.assertTrue(item.getType() instanceof CurrencyItem);
        Assertions.assertEquals(1, item.getAffixes().size());
        Assertions.assertEquals(5000, item.getProps().getStackSize());
    }

    @Test
    void parseNotEquippableItem() {
        Item item = parse("Rarity: Rare\n" +
                "You cannot use this item. Its stats will be ignored\n" +
                "--------\n" +
                "Beast Cloak\n" +
                "Sadist Garb\n" +
                "--------\n" +
                "Quality: +20% (augmented)\n" +
                "Evasion Rating: 594 (augmented)\n" +
                "Energy Shield: 122 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 68\n" +
                "Dex: 103 (unmet)\n" +
                "Int: 109\n" +
                "--------\n" +
                "Sockets: B-B-B-G-G \n" +
                "--------\n" +
                "Item Level: 76\n" +
                "--------\n" +
                "19% increased Evasion and Energy Shield\n" +
                "+63 to maximum Life\n" +
                "+61 to maximum Mana\n" +
                "+40% to Fire Resistance\n" +
                "+21% to Cold Resistance (crafted)"
        );

        Assertions.assertSame(ItemRarity.RARE, item.getRarity());
        Assertions.assertEquals("Sadist Garb", item.getBase());
    }

    @Test
    void parseVaalGem() {
        Item item = parse("Rarity: Gem\n" +
                "Righteous Fire\n" +
                "--------\n" +
                "Vaal, Spell, AoE, Fire, Duration\n" +
                "Level: 1\n" +
                "Cooldown Time: 0.30 sec\n" +
                "Cast Time: Instant\n" +
                "Experience: 1/49,725\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 16\n" +
                "Str: 18\n" +
                "Int: 26\n" +
                "--------\n" +
                "Engulfs you in magical fire that rapidly burns you and nearby enemies. Your spell damage is substantially increased while under this effect. The effect ends when you have 1 life remaining.\n" +
                "--------\n" +
                "Deals 35.8 Base Fire Damage per second\n" +
                "Deals 20% of your Maximum Life as Base Fire Damage per second\n" +
                "Deals 20% of your Maximum Energy Shield as Base Fire Damage per second\n" +
                "You Burn for 90% of your Maximum Life per second as Fire Damage\n" +
                "You Burn for 70% of your Maximum Energy Shield per second as Fire Damage\n" +
                "Grants 20% more Spell Damage\n" +
                "--------\n" +
                "Vaal Righteous Fire\n" +
                "--------\n" +
                "Cooldown Time: 0.50 sec\n" +
                "Souls Per Use: 40\n" +
                "Can Store 1 Use\n" +
                "Soul Gain Prevention: 12 sec\n" +
                "Cast Time: Instant\n" +
                "--------\n" +
                "Sacrifices a portion of your Life and Energy Shield to engulf you in magical fire that rapidly burns nearby enemies for a duration. Your spell damage is substantially increased while under this effect.\n" +
                "--------\n" +
                "Base duration is 4.00 seconds\n" +
                "Sacrifices 30% of your total Energy Shield and Life\n" +
                "Deals 161% of Sacrificed Energy Shield and Life as Fire Damage per second\n" +
                "Modifiers to Skill Effect Duration also apply to this Skill's Soul Gain Prevention\n" +
                "Grants 15% more Spell Damage\n" +
                "--------\n" +
                "Place into an item socket of the right colour to gain this skill. Right click to remove from a socket.\n" +
                "--------\n" +
                "Corrupted\n"
        );

        Assertions.assertSame(ItemRarity.NORMAL, item.getRarity());
        Assertions.assertEquals("Vaal Righteous Fire", item.getBase());
        Assertions.assertTrue(item.getType() instanceof GemItem);
        Assertions.assertSame(1, ((GemItem) item.getType()).getLevel());
        Assertions.assertSame(0, item.getProps().getQuality());
        Assertions.assertTrue(item.getProps().isCorrupted());
    }
}
