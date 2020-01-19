package dev.tricht.lunaris.com.pathofexile.itemtransformer;

import dev.tricht.lunaris.com.pathofexile.request.Filters;
import dev.tricht.lunaris.com.pathofexile.request.Option;
import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.com.pathofexile.request.Value;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.GemItem;

public class RequirementFilterSetter {
    public static void set(Item item, Query query) {
        Filters.NestedFilters reqFilters = new Filters.NestedFilters();
        query.getFilters().setReqFilters(reqFilters);
        Filters.DeeperFilters deeperReqFilters = new Filters.DeeperFilters();
        reqFilters.setFilters(deeperReqFilters);
        // Dex, str and int include the gem requirements.
    }
}
