package dev.tricht.lunaris.com.pathofexile.itemtransformer;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.com.pathofexile.request.StatFilter;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ItemTransformerTest {

    @BeforeAll
    public static void initApi() {
        new PathOfExileAPI();
    }

    @Test
    public void testWithLocalAndCraftedMods() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Rare\n" +
                "Nemesis Goad\n" +
                "Titan Greaves\n" +
                "--------\n" +
                "Quality: +20% (augmented)\n" +
                "Armour: 453 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 68\n" +
                "Str: 120\n" +
                "Dex: 37\n" +
                "Int: 83\n" +
                "--------\n" +
                "Sockets: R-B-R-B \n" +
                "--------\n" +
                "Item Level: 83\n" +
                "--------\n" +
                "0.6% of Damage Leeched as Life if you've Killed Recently\n" +
                "--------\n" +
                "68% increased Armour\n" +
                "+74 to maximum Life\n" +
                "+42% to Cold Resistance\n" +
                "+20% to Lightning Resistance\n" +
                "30% increased Movement Speed\n" +
                "30% increased Effect of Non-Damaging Ailments (crafted)\n" +
                "70% increased Energy Shield (crafted)\n"));

        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "enchant.stat_4291115328", 0.6); // dmg leech
        assertStatFilter(filters.get(1), "explicit.stat_1062208444", 68); // Armour (local)
        assertStatFilter(filters.get(2), "explicit.stat_3299347043", 74); // Max life
        assertStatFilter(filters.get(3), "explicit.stat_4220027924", 42); // Cold res
        assertStatFilter(filters.get(4), "explicit.stat_1671376347", 20); // Lightning res
        assertStatFilter(filters.get(5), "explicit.stat_2250533757", 30); // Movespeed
        assertStatFilter(filters.get(6), "crafted.stat_782230869", 30); // effect of ailments (crafted)
        assertStatFilter(filters.get(7), "crafted.stat_4015621042", 70); // %inc es (Local) crafted)
    }

    @Test
    public void testUniqueHelmetWithLocalAndGlobalMods() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Unique\n" +
                "Devoto's Devotion\n" +
                "Nightmare Bascinet\n" +
                "--------\n" +
                "Armour: 480 (augmented)\n" +
                "Evasion Rating: 690 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 67\n" +
                "Str: 62\n" +
                "Dex: 85\n" +
                "--------\n" +
                "Sockets: B \n" +
                "--------\n" +
                "Item Level: 80\n" +
                "--------\n" +
                "+56 to Dexterity\n" +
                "10% reduced Global Physical Damage\n" +
                "16% increased Attack Speed\n" +
                "196% increased Armour and Evasion\n" +
                "+18% to Chaos Resistance\n" +
                "20% increased Movement Speed\n" +
                "Mercury Footprints\n" +
                "--------\n" +
                "Swift as thought are the actions of Man \n" +
                "when borne on wings of divine providence."));

        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "explicit.stat_3261801346", 56); // Dexterity
        assertStatFilter(filters.get(1), "explicit.stat_681332047", 16); // increased Attack Speed
        assertStatFilter(filters.get(2), "explicit.stat_2451402625", 196); // increased Armour and Evasion (Local)
        assertStatFilter(filters.get(3), "explicit.stat_2923486259", 18); // Chaos Resistance
        assertStatFilter(filters.get(4), "explicit.stat_2250533757", 20); // increased Movement Speed
        assertStatFilter(filters.get(5), "explicit.stat_3970396418"); // Mercury Footprints
    }

    @Test
    public void testUniqueClawWithLocalAndGlobalMods() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Unique\n" +
                "Advancing Fortress\n" +
                "Gut Ripper\n" +
                "--------\n" +
                "Claw\n" +
                "Physical Damage: 44-117 (augmented)\n" +
                "Critical Strike Chance: 6.30%\n" +
                "Attacks per Second: 1.50\n" +
                "Weapon Range: 11\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 46\n" +
                "Dex: 80\n" +
                "Int: 80\n" +
                "--------\n" +
                "Sockets: G \n" +
                "--------\n" +
                "Item Level: 79\n" +
                "--------\n" +
                "+44 Life gained for each Enemy hit by Attacks (implicit)\n" +
                "--------\n" +
                "Socketed Gems are Supported by Level 12 Fortify\n" +
                "15% Chance to Block Attack Damage\n" +
                "120% increased Physical Damage\n" +
                "+110 to Evasion Rating\n" +
                "+35 to maximum Energy Shield\n" +
                "+30 to maximum Life\n" +
                "Reflects 78 Physical Damage to Melee Attackers\n" +
                "--------\n" +
                "\"A man cowers behind his walls.\n" +
                "A woman carries her fortress with her.\n" +
                "In heart, in mind, in hand.\"\n" +
                "- Sekhema Deshret"));

        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "implicit.stat_821021828", 44); // Life gained for each Enemy hit by Attacks
        assertStatFilter(filters.get(1), "explicit.stat_107118693", 12); // Supported by Level 12 Fortify
        assertStatFilter(filters.get(2), "explicit.stat_2530372417", 15); // Chance to Block Attack Damage
        assertStatFilter(filters.get(3), "explicit.stat_1509134228", 120); // increased Physical Damage (Local)
        assertStatFilter(filters.get(4), "explicit.stat_2144192055", 110); // Evasion Rating
        assertStatFilter(filters.get(5), "explicit.stat_3489782002", 35); // maximum Energy Shield
        assertStatFilter(filters.get(6), "explicit.stat_3299347043", 30); // maximum Life
        assertStatFilter(filters.get(7), "explicit.stat_3767873853", 78); // Reflects Physical Damage to Melee Attackers
    }

    @Test
    @Disabled
    public void testWithMultiLineMod() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Unique\n" +
                "Inpulsa's Broken Heart\n" +
                "Sadist Garb\n" +
                "--------\n" +
                "Quality: +20% (augmented)\n" +
                "Evasion Rating: 512 (augmented)\n" +
                "Energy Shield: 106 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Dex: 111\n" +
                "Int: 155\n" +
                "--------\n" +
                "Sockets: W-B-B-B-B-G \n" +
                "--------\n" +
                "Item Level: 74\n" +
                "--------\n" +
                "+75 to maximum Life\n" +
                "49% increased Damage if you have Shocked an Enemy Recently\n" +
                "32% increased Effect of Shock\n" +
                "Shocked Enemies you Kill Explode, dealing 5% of\n" +
                "their Maximum Life as Lightning Damage which cannot Shock\n" +
                "Unaffected by Shock\n" +
                "--------\n" +
                "Don't hesitate; bring death to all, conclusively and swiftly,\n" +
                "or they will give you the same treatment.\n" +
                "--------\n" +
                "Corrupted\n"));

        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "explicit.stat_3299347043", 75); // Max life
        assertStatFilter(filters.get(1), "explicit.stat_908650225", 49); // Inc damage after shock
        assertStatFilter(filters.get(2), "explicit.stat_2527686725", 32); // Shock eff.
        assertStatFilter(filters.get(3), "explicit.stat_1473289174"); // Unaff by shock

        // TODO: Broken
        assertStatFilter(filters.get(4), "explicit.stat_2706994884"); // Shocked enemies explode
    }

    @Test
    public void testWithImplicit() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Unique\n" +
                "Essence Worm\n" +
                "Unset Ring\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 72\n" +
                "Int: 159\n" +
                "--------\n" +
                "Sockets: W \n" +
                "--------\n" +
                "Item Level: 84\n" +
                "--------\n" +
                "Has 1 Socket (implicit)\n" +
                "--------\n" +
                "+2 to Level of Socketed Aura Gems\n" +
                "Socketed Gems Reserve No Mana\n" +
                "40% increased Mana Reserved\n" +
                "--------\n" +
                "\"This thing is not a pet. It is a parasite that feeds on the very will of its host.\n" +
                "Like any part of nightmare, it has found a way to make its price... acceptable.\"\n" +
                "- Malachai the Soulless\n" +
                "--------\n" +
                "Corrupted\n"));


        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "implicit.stat_4077843608"); // Has socket
        assertStatFilter(filters.get(1), "explicit.stat_2452998583", 2); // Aura gems
        assertStatFilter(filters.get(2), "explicit.stat_1237038225"); // No mana reserved
        assertStatFilter(filters.get(3), "explicit.stat_2227180465", 40); // Inc mana reserved
    }

    @Test
    public void testStatFiltersWithFracturedMods() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Rare\n" +
                "Woe Knuckle\n" +
                "Titan Gauntlets\n" +
                "--------\n" +
                "Armour: 341 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 69\n" +
                "Str: 98\n" +
                "--------\n" +
                "Sockets: G-R R-R \n" +
                "--------\n" +
                "Item Level: 83\n" +
                "--------\n" +
                "Adds 2 to 23 Lightning Damage to Attacks (fractured)\n" +
                "+45% to Cold Resistance (fractured)\n" +
                "+42 to Strength\n" +
                "+48 to maximum Life\n" +
                "+42% to Lightning Resistance\n" +
                "41% increased Armour (crafted)\n" +
                "--------\n" +
                "Fractured Item\n"));

        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "fractured.stat_1754445556", (2+23) / 2); // lightning damage
        assertStatFilter(filters.get(1), "fractured.stat_4220027924", 45); // Cold res
        assertStatFilter(filters.get(2), "explicit.stat_4080418644", 42); // Strength
        assertStatFilter(filters.get(3), "explicit.stat_3299347043", 48); //Life
        assertStatFilter(filters.get(4), "explicit.stat_1671376347", 42); //lightning res
        assertStatFilter(filters.get(5), "crafted.stat_2866361420", 41); // inc armour %
    }

    @Test
    public void testStatFiltersWithFracturedLocalMods() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Rare\n" +
                "Death League\n" +
                "Conjurer Boots\n" +
                "--------\n" +
                "Quality: +5% (augmented)\n" +
                "Energy Shield: 46 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 57\n" +
                "Int: 94\n" +
                "--------\n" +
                "Sockets: B-B \n" +
                "--------\n" +
                "Item Level: 78\n" +
                "--------\n" +
                "30% increased Energy Shield (fractured)\n" +
                "+30 to maximum Mana (fractured)\n" +
                "+16 to maximum Life\n" +
                "+42% to Fire Resistance\n" +
                "21% increased Stun and Block Recovery\n" +
                "--------\n" +
                "Fractured Item\n"));

        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "fractured.stat_4015621042", 30); // Local ES
        assertStatFilter(filters.get(1), "fractured.stat_1050105434", 30); // Mana
        assertStatFilter(filters.get(2), "explicit.stat_3299347043", 16); // Life
        assertStatFilter(filters.get(3), "explicit.stat_3372524247", 42); // Fire res
        assertStatFilter(filters.get(4), "explicit.stat_2511217560", 21); // Stun and block
    }


    @Test
    public void testIfUniqueHasFullNameInQuery() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Unique\n" +
                "Malachai's Artifice\n" +
                "Unset Ring\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Str: 98\n" +
                "Int: 68\n" +
                "--------\n" +
                "Sockets: W \n" +
                "--------\n" +
                "Item Level: 76\n" +
                "--------\n" +
                "Has 1 Socket (implicit)\n" +
                "--------\n" +
                "Socketed Gems have Elemental Equilibrium\n" +
                "-20% to all Elemental Resistances\n" +
                "+100% to Fire Resistance when Socketed with a Red Gem\n" +
                "+96% to Cold Resistance when Socketed with a Green Gem\n" +
                "+93% to Lightning Resistance when Socketed with a Blue Gem\n" +
                "All Sockets are White\n" +
                "--------\n" +
                "When the wind blows, \n" +
                "Know which way to bend\n" +
                "and watch the others break.\n"));

        Assertions.assertEquals("Malachai's Artifice", query.getName());
        Assertions.assertEquals("Unset Ring", query.getType());
    }

    @Test
    public void testIfCurrencyHasFullNameInQuery() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Currency\n" +
                "Exalted Orb\n" +
                "--------\n" +
                "Stack Size: 151/10\n" +
                "--------\n" +
                "Enchants a rare item with a new random modifier\n" +
                "--------\n" +
                "Right click this item then left click a rare item to apply it. Rare items can have up to six random modifiers. Removes 20% Quality applied by Catalysts on use.\n" +
                "Shift click to unstack.\n"));

        Assertions.assertEquals("Exalted Orb", query.getTerm());
    }

    @Test
    public void testIfMapHasBaseNameInQuery() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Rare\n" +
                "Cinder Point\n" +
                "Canyon Map\n" +
                "--------\n" +
                "Map Tier: 15\n" +
                "Item Quantity: +127% (augmented)\n" +
                "Item Rarity: +64% (augmented)\n" +
                "Monster Pack Size: +41% (augmented)\n" +
                "Quality: +20% (augmented)\n" +
                "--------\n" +
                "Item Level: 82\n" +
                "--------\n" +
                "20% more Magic Monsters\n" +
                "Players are Cursed with Temporal Chains\n" +
                "Players are Cursed with Elemental Weakness\n" +
                "-10% maximum Player Resistances\n" +
                "+40% Monster Physical Damage Reduction\n" +
                "Monsters deal 108% extra Damage as Lightning\n" +
                "Magic Monster Packs each have a Bloodline Mod\n" +
                "Slaying Enemies close together has a 13% chance to attract monsters from Beyond\n" +
                "Monsters have a 20% chance to cause Elemental Ailments on Hit\n" +
                "--------\n" +
                "Travel to this Map by using it in a personal Map Device. Maps can only be used once.\n" +
                "--------\n" +
                "Corrupted\n"));

        Assertions.assertEquals("Canyon Map", query.getTerm());
    }

    @Test
    public void testIfGemHasFullNameInQuery() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Gem\n" +
                "Curse On Hit Support\n" +
                "--------\n" +
                "Curse, Trigger, Support\n" +
                "Level: 20 (Max)\n" +
                "Quality: +20% (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Int: 111\n" +
                "--------\n" +
                "Must support both a skill that hits enemies, and a curse skill to work. The curse will be applied when enemies are hit by the other skill. Cannot support totems, traps, or mines. Minions cannot apply curses this way.\n" +
                "--------\n" +
                "Supported Skills apply supported Curses on Hit\n" +
                "You cannot Cast Supported Curses\n" +
                "10% increased Effect of Supported Curses\n" +
                "Supported Skills have 12% reduced Curse Duration\n" +
                "--------\n" +
                "This is a Support Gem. It does not grant a bonus to your character, but to skills in sockets connected to it. Place into an item socket connected to a socket containing the Active Skill Gem you wish to augment. Right click to remove from a socket.\n" +
                "--------\n" +
                "Corrupted\n"));

        Assertions.assertEquals("Curse On Hit Support", query.getTerm());
    }
    @Test
    public void testIfNormalItemHasFullNameInQuery() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Currency\n" +
                "Exalted Orb\n" +
                "--------\n" +
                "Stack Size: 151/10\n" +
                "--------\n" +
                "Enchants a rare item with a new random modifier\n" +
                "--------\n" +
                "Right click this item then left click a rare item to apply it. Rare items can have up to six random modifiers. Removes 20% Quality applied by Catalysts on use.\n" +
                "Shift click to unstack.\n"));

        Assertions.assertEquals("Exalted Orb", query.getTerm());
    }


    @Test
    public void testCorruptedGemFilters() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Gem\n" +
                "Curse On Hit Support\n" +
                "--------\n" +
                "Curse, Trigger, Support\n" +
                "Level: 20 (Max)\n" +
                "Quality: +20% (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Int: 111\n" +
                "--------\n" +
                "Must support both a skill that hits enemies, and a curse skill to work. The curse will be applied when enemies are hit by the other skill. Cannot support totems, traps, or mines. Minions cannot apply curses this way.\n" +
                "--------\n" +
                "Supported Skills apply supported Curses on Hit\n" +
                "You cannot Cast Supported Curses\n" +
                "10% increased Effect of Supported Curses\n" +
                "Supported Skills have 12% reduced Curse Duration\n" +
                "--------\n" +
                "This is a Support Gem. It does not grant a bonus to your character, but to skills in sockets connected to it. Place into an item socket connected to a socket containing the Active Skill Gem you wish to augment. Right click to remove from a socket.\n" +
                "--------\n" +
                "Corrupted\n"));

        Assertions.assertTrue((Boolean) query.getFilters().getNestedFilters().getFilters().getCorrupted().getOption());
        Assertions.assertEquals(20, query.getFilters().getNestedFilters().getFilters().getGemLevel().getMin());
        Assertions.assertEquals(20, query.getFilters().getNestedFilters().getFilters().getQuality().getMin());
        Assertions.assertNull(query.getFilters().getNestedFilters().getFilters().getIlvl());
        Assertions.assertNull(query.getFilters().getNestedFilters().getFilters().getMapTier());
    }

    @Test
    public void testNoIlvlOnUniques() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Unique\n" +
                "Essence Worm\n" +
                "Unset Ring\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 72\n" +
                "Int: 159\n" +
                "--------\n" +
                "Sockets: W \n" +
                "--------\n" +
                "Item Level: 84\n" +
                "--------\n" +
                "Has 1 Socket (implicit)\n" +
                "--------\n" +
                "+2 to Level of Socketed Aura Gems\n" +
                "Socketed Gems Reserve No Mana\n" +
                "40% increased Mana Reserved\n" +
                "--------\n" +
                "\"This thing is not a pet. It is a parasite that feeds on the very will of its host.\n" +
                "Like any part of nightmare, it has found a way to make its price... acceptable.\"\n" +
                "- Malachai the Soulless\n" +
                "--------\n" +
                "Corrupted\n"));

        Assertions.assertTrue((Boolean) query.getFilters().getNestedFilters().getFilters().getCorrupted().getOption());
        Assertions.assertNull(query.getFilters().getNestedFilters().getFilters().getIlvl());
    }

    @Test
    public void testQualityFilterOnHighQualItems() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Rare\n" +
                "Golem Thirst\n" +
                "Opal Wand\n" +
                "--------\n" +
                "Wand\n" +
                "Quality: +28% (augmented)\n" +
                "Physical Damage: 45-83 (augmented)\n" +
                "Critical Strike Chance: 7.00%\n" +
                "Attacks per Second: 1.30\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 70\n" +
                "Int: 212\n" +
                "--------\n" +
                "Sockets: B-B-B \n" +
                "--------\n" +
                "Item Level: 85\n" +
                "--------\n" +
                "42% increased Spell Damage (implicit)\n" +
                "--------\n" +
                "Gain 20% of Elemental Damage as Extra Chaos Damage\n" +
                "Can have up to 3 Crafted Modifiers (crafted)\n" +
                "50% increased Spell Damage (crafted)\n" +
                "Adds 5 to 109 Lightning Damage to Spells (crafted)\n" +
                "17% increased Cast Speed (crafted)\n" +
                "4% chance to deal Double Damage (crafted)\n" +
                "Gain 4% of Non-Chaos Damage as extra Chaos Damage (crafted)\n" +
                "--------\n" +
                "Shaper Item\n"));

        Assertions.assertEquals(28, query.getFilters().getNestedFilters().getFilters().getQuality().getMin());
    }

    @Test
    public void testWithBothImplicitsAndEnchant() {
        Query query = ItemTransformer.createQuery(parse("Rarity: Rare\n" +
                "Phoenix Dash\n" +
                "Synthesised Slink Boots\n" +
                "--------\n" +
                "Quality: +24% (augmented)\n" +
                "Evasion Rating: 305 (augmented)\n" +
                "--------\n" +
                "Requirements:\n" +
                "Level: 69\n" +
                "Dex: 120\n" +
                "--------\n" +
                "Sockets: W-B-G-G \n" +
                "--------\n" +
                "Item Level: 82\n" +
                "--------\n" +
                "10% increased Movement Speed if you haven't been Hit Recently\n" +
                "--------\n" +
                "+13 to maximum Life (implicit)\n" +
                "8% increased Movement Speed (implicit)\n" +
                "--------\n" +
                "+80 to maximum Life\n" +
                "2% increased maximum Life\n" +
                "Regenerate 9.3 Life per second\n" +
                "30% increased Movement Speed\n" +
                "Regenerate 1% of Life per second\n" +
                "6% increased Movement Speed if you've Hit an Enemy Recently\n" +
                "--------\n" +
                "Corrupted\n" +
                "--------\n" +
                "Synthesised Item\n" +
                "--------\n" +
                "Note: ~b/o 20 exa\n"));

        Assertions.assertEquals(24, query.getFilters().getNestedFilters().getFilters().getQuality().getMin());

        List<StatFilter> filters = query.getStats().get(0).getFilters();
        assertStatFilter(filters.get(0), "implicit.stat_3299347043", 13); // Max life implicit
        assertStatFilter(filters.get(1), "implicit.stat_2250533757", 8); // Movespeed implicit
        assertStatFilter(filters.get(2), "enchant.stat_308396001", 10); // Movespeed enchant
        assertStatFilter(filters.get(3), "explicit.stat_3299347043", 80); // Max life
        assertStatFilter(filters.get(4), "explicit.stat_983749596", 2); // % max life
        assertStatFilter(filters.get(5), "explicit.stat_3325883026", 9.3); //Life per sec
        assertStatFilter(filters.get(6), "explicit.stat_2250533757", 30); //  Movespeed
        assertStatFilter(filters.get(7), "explicit.stat_836936635", 1); // Life % regen per sec
        assertStatFilter(filters.get(8), "explicit.stat_3178542354", 6); // Movespeed if hit
    }


    void assertStatFilter(StatFilter filter, String statId, Double minValue) {
        Assertions.assertEquals(statId, filter.getId());
        if(minValue != null) {
            Assertions.assertEquals(minValue, filter.getValue().getMin());
        }
    }

    void assertStatFilter(StatFilter filter, String statId, Integer minValue) {
        Assertions.assertEquals(statId, filter.getId());
        if(minValue != null) {
            Assertions.assertEquals(minValue, filter.getValue().getMin().intValue());
        }
    }

    private void assertStatFilter(StatFilter filter, String statId) {
        Assertions.assertEquals(statId, filter.getId());
    }

    Item parse(String lines) {
        return ItemParser.parse(
                lines.split("\n")
        );
    }
}
