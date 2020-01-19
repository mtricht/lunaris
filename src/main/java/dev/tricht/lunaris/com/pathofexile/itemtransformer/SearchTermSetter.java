package dev.tricht.lunaris.com.pathofexile.itemtransformer;

import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.GemItem;
import dev.tricht.lunaris.item.types.MapItem;

public class SearchTermSetter {
    public static void set(Item item, Query query) {
        // Since normal item's don't have affixes, always include the item name in the search
        // This also happens to include currency, div cards, fragments, scarabs
        if (item.getRarity() == ItemRarity.NORMAL) {
            query.setTerm(item.getBase());
        }

        // For unique items, also set the complete name since we're only interested in this specific item
        if (item.getRarity() == ItemRarity.UNIQUE) {
            query.setName(item.getName());
            query.setType(item.getBase());
        }

        // Maps are also searched by name
        if (item.getType() instanceof MapItem) {
            query.setTerm(item.getBase());
        }

        // And so are gems
        if ((item.getType() instanceof GemItem)) {
            query.setTerm(item.getBase());
        }
    }
}
