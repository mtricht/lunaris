package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.tooltip.elements.*;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.types.CurrencyItem;
import dev.tricht.lunaris.tooltip.TooltipCreator;
import lombok.extern.slf4j.Slf4j;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class CurrencyStackListener implements GameListener {

    @Override
    public void onEvent(GameEvent event) {
        try {
            log.debug("Trying currency stack");
            Item item = event.getItem();
            if (item == null || !(item.getType() instanceof CurrencyItem)) {
                log.debug("Not a currency stack!");
                return;
            }

            int stackSize = item.getProps().getStackSize();

            item.getMeanPrice().setPrice(Math.round(item.getMeanPrice().getPrice() * stackSize * 100) / 100);
            item.setBase(stackSize + "x " + item.getBase());
            Map<Element, int[]> elements = new LinkedHashMap<>();
            elements.put(new Icon(item, 48), new int[]{0, 0});
            elements.put(new ItemName(item, 48 + Icon.PADDING), new int[]{1, 0});
            elements.put(new Price(item), new int[]{1, elements.size() - 1});
            elements.put(new Source("poe.ninja"), new int[]{1, elements.size() - 1});

            TooltipCreator.create(event.getMousePos(), elements);
        } catch (Exception e) {
            log.error("Exception while displaying currency stack", e);
        }
    }

    @Override
    public boolean supports(GameEvent event) {
        return event.getItem().getType() instanceof CurrencyItem;
    }
}
