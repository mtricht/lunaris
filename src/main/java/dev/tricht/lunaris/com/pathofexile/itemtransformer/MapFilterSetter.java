package dev.tricht.lunaris.com.pathofexile.itemtransformer;

import dev.tricht.lunaris.com.pathofexile.request.Filters;
import dev.tricht.lunaris.com.pathofexile.request.Option;
import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.com.pathofexile.request.Value;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.GemItem;
import dev.tricht.lunaris.item.types.MapItem;

public class MapFilterSetter {
    public static void set(Item item, Query query) {
        Filters filters = new Filters();
        query.setFilters(filters);
        Filters.NestedFilters mapFilters = new Filters.NestedFilters();
        filters.setNestedFilters(mapFilters);
        Filters.DeeperFilters deeperMapFilters = new Filters.DeeperFilters();
        mapFilters.setFilters(deeperMapFilters);

        if (item.getType() instanceof MapItem) {
            Value mapTier = new Value();
            mapTier.setMin((double) ((MapItem) item.getType()).getTier());
            deeperMapFilters.setMapTier(mapTier);
        }
    }
}
