package dev.tricht.poe.assistant.item;

import dev.tricht.poe.assistant.item.types.*;
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
    void parsesPartsForUniqueBodyArmour() {
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
}
