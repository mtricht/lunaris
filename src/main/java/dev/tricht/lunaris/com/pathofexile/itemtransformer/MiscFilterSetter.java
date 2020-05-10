package dev.tricht.lunaris.com.pathofexile.itemtransformer;

import dev.tricht.lunaris.com.pathofexile.request.*;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.DivinitationCardItem;
import dev.tricht.lunaris.item.types.GemItem;

public class MiscFilterSetter {
    public static void set(Item item, Query query) {
        Filters filters = new Filters();
        query.setFilters(filters);
        Filters.NestedFilters miscFilters = new Filters.NestedFilters();
        filters.setNestedFilters(miscFilters);
        Filters.DeeperFilters deeperMiscFilters = new Filters.DeeperFilters();
        miscFilters.setFilters(deeperMiscFilters);

        // We never want to see corrupted items, unless our own item is also corrupted
        Option corrupted = new Option();
        corrupted.setOption(false);
        if (item.getProps().isCorrupted() && !(item.getType() instanceof DivinitationCardItem)) {
            corrupted.setOption(true);
        }
        deeperMiscFilters.setCorrupted(corrupted);

        // Don't check ilvl on uniques since it's very situational
        if (item.getProps().getItemLevel() != 1 && item.getRarity() != ItemRarity.UNIQUE) {
            DoubleValue ilvl = new DoubleValue();
            ilvl.setMin((double) item.getProps().getItemLevel());
            deeperMiscFilters.setIlvl(ilvl);
        }

        // We never want to see mirrored items, unless our item is also mirrored
        Option mirrored = new Option();
        mirrored.setOption(false);
        if (item.getProps().isMirrored()) {
            mirrored.setOption(true);
        }
        deeperMiscFilters.setMirrored(mirrored);

        // Only quality on gems is expensive (some amulets/rings too, but we narrow our results too much by checking for that)
        // However if the quality is above >20, it might be worth something (hillock bench)
        if (item.getProps().getQuality() > 20 || item.getType() instanceof GemItem) {
            DoubleValue quality = new DoubleValue();
            deeperMiscFilters.setQuality(quality);
            quality.setMin((double) item.getProps().getQuality());
        }

        if (item.getType() instanceof GemItem) {
            DoubleValue gemLevel = new DoubleValue();
            gemLevel.setMin((double) ((GemItem) item.getType()).getLevel());
            deeperMiscFilters.setGemLevel(gemLevel);
        }

        if (item.getProps().getLinks() >= 5) {
            Filters.NestedFilters socketFilters = new Filters.NestedFilters();
            filters.setSocketFilters(socketFilters);
            Filters.DeeperFilters socketDeeperFilter = new Filters.DeeperFilters();
            Value links = new Value();
            links.setMin(item.getProps().getLinks());
            socketDeeperFilter.setLinks(links);
            socketFilters.setFilters(socketDeeperFilter);
        }
    }
}
