package dev.tricht.lunaris.com.pathofexile.middleware;

import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.com.pathofexile.request.StatFilter;
import dev.tricht.lunaris.com.pathofexile.request.DoubleValue;
import dev.tricht.lunaris.item.Item;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class ModValueRangeMiddleware implements TradeMiddleware {

    private int percentage;
    private boolean setMax;

    public ModValueRangeMiddleware(int percentage, boolean setMax) {
        this.percentage = percentage;
        this.setMax = setMax;
    }
    
    @Override
    public void handle(Item item, Query query) {
        if (query.getStats().size() < 1) {
            return;
        }

        for(StatFilter filter : query.getStats().get(0).getFilters()) {
            if (filter.getDoubleValue() == null) {
                continue;
            }

            if (filter.getDoubleValue().getMin() == null) {
                continue;
            }

            DoubleValue doubleValue = filter.getDoubleValue();

            Double originalValue = doubleValue.getMin();

            if (Math.abs(originalValue) <= 3.0) {
                doubleValue.setMin(originalValue * (1.0 * (100 - percentage) / 100));
                if (setMax) {
                    doubleValue.setMax(originalValue * (1.0 * (100 + percentage) / 100));
                }
                continue;
            }

            doubleValue.setMin((double) (int) (originalValue * (1.0 * (100 - percentage) / 100)));
            if(setMax) {
                doubleValue.setMax((double) (int) (originalValue * (1.0 * (100 + percentage) / 100)));
            }
        }
    }


}
