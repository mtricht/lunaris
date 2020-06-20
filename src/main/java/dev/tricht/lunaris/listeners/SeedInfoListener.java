package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.data.MapInfoResolver;
import dev.tricht.lunaris.data.seeds.SeedInfo;
import dev.tricht.lunaris.data.seeds.SeedResolver;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.types.SeedItem;
import dev.tricht.lunaris.item.types.WeaponItem;
import dev.tricht.lunaris.tooltip.TooltipCreator;
import dev.tricht.lunaris.tooltip.elements.Element;
import dev.tricht.lunaris.tooltip.elements.Icon;
import dev.tricht.lunaris.tooltip.elements.ItemName;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class SeedInfoListener implements GameListener {

    private SeedResolver seedInfo;

    public SeedInfoListener() { this.seedInfo = new SeedResolver();}

    @Override
    public void onEvent(GameEvent event) {

        Item item = event.getItem();
        log.info("Trying seed: " + item.getName());
        SeedInfo seed = seedInfo.getSeedInfo(item.getBase());

        if (seed == null) {
            log.info("Can't find seed: " + item.getBase());
            return;
        }

        Map<Element, int[]> elements = new LinkedHashMap<>();
        elements.put(new Icon(item, 48), new int[]{0, 0});
        elements.put(new ItemName(item, 48 + Icon.PADDING), new int[]{1, 0});

        elements.put(new dev.tricht.lunaris.tooltip.elements.Label(seed.getEffect()), new int[]{1, 1});
        elements.put(new dev.tricht.lunaris.tooltip.elements.Label(String.join("\n", seed.getEffectList())), new int[]{1, 2});

        TooltipCreator.create(event.getMousePos(), elements);
    }

    @Override
    public boolean supports(GameEvent event) {
        return event.getItem().getType() instanceof SeedItem;
    }
}
