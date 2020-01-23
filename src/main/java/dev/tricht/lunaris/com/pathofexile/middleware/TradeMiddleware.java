package dev.tricht.lunaris.com.pathofexile.middleware;

import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.item.Item;

public interface TradeMiddleware {
    public void handle(Item item, Query query);
}
