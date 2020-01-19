package dev.tricht.lunaris.com.pathofexile.itemtransformer;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.com.pathofexile.middleware.PseudoModsMiddleware;
import dev.tricht.lunaris.com.pathofexile.middleware.TradeMiddleware;
import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.com.pathofexile.request.StatFilter;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class PseudoMiddlewareTest {

    @BeforeAll
    public static void initApi() {
        new PathOfExileAPI();
        ArrayList<TradeMiddleware> middlewares = new ArrayList<>();
        middlewares.add(new PseudoModsMiddleware());
        ItemTransformer.setMiddleware(middlewares);
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
                "30% increased Effect of non-Damaging Ailments on Enemies (crafted)\n"));

        List<StatFilter> filters = query.getStats().get(0).getFilters();

        assertStatFilter(filters.get(0), "enchant.stat_4291115328", 0.6); // dmg leech
        assertStatFilter(filters.get(1), "explicit.stat_1062208444", 68); // Armour (local)
        assertStatFilter(filters.get(2), "explicit.stat_3299347043", 74); // Max life
        assertStatFilter(filters.get(3), "explicit.stat_2250533757", 30); // Movespeed
        assertStatFilter(filters.get(4), "crafted.stat_782230869", 30); // effect of ailments (crafted)
        assertStatFilter(filters.get(5), "pseudo.pseudo_total_elemental_resistance", 62); // total ele res (pseudo)
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
        assertStatFilter(filters.get(1), "explicit.stat_4080418644", 42); // Strength
        assertStatFilter(filters.get(2), "explicit.stat_3299347043", 48); //Life
        assertStatFilter(filters.get(3), "crafted.stat_2866361420", 41); // inc armour %
        assertStatFilter(filters.get(4), "pseudo.pseudo_total_elemental_resistance", 45 + 42); // total ele res (pseudo)
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
        assertStatFilter(filters.get(3), "explicit.stat_2511217560", 21); // Stun and block
        assertStatFilter(filters.get(4), "pseudo.pseudo_total_elemental_resistance",  42); // total ele res (pseudo)
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

        Assertions.assertTrue(query.getFilters().getNestedFilters().getFilters().getCorrupted().isOption());
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

        Assertions.assertTrue(query.getFilters().getNestedFilters().getFilters().getCorrupted().isOption());
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
        ItemParser parser = new ItemParser(
                lines.split("\n")
        );
        return parser.parse();
    }
}
