package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.tooltip.elements.Element;
import dev.tricht.lunaris.tooltip.elements.Icon;
import dev.tricht.lunaris.tooltip.elements.ItemName;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.types.WeaponItem;
import dev.tricht.lunaris.tooltip.TooltipCreator;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class WeaponInfoListener implements GameListener {

    DecimalFormat df = new DecimalFormat("#.##");

    public WeaponInfoListener() { }

    @Override
    public void onEvent(GameEvent event) {
        //build and display GUI that shows weapon dps

        Item item = event.getItem();
        double totalDPS = ((WeaponItem)item.getType()).getTotalDPS();
        double physDPS = ((WeaponItem) item.getType()).getPhysDPS();
        double elemDPS = ((WeaponItem) item.getType()).getElemDPS();
        double chaosDPS = ((WeaponItem) item.getType()).getChaosDPS();


        log.info("Displaying weapon dps..");
        log.info("Total dps: " + totalDPS);
        log.info("PhysDPS: "+ physDPS);
        log.info("ElemDPS: "+ elemDPS);
        log.info("ChaosDPS: "+ chaosDPS);

        Map<Element, int[]> elements = new LinkedHashMap<>();
        elements.put(new Icon(item, 48), new int[]{0, 0});
        elements.put(new ItemName(item, 48 + Icon.PADDING), new int[]{1, 0});

        int row = 1;
        if(physDPS!=0)elements.put(new dev.tricht.lunaris.tooltip.elements.Label("Physical DPS: "+ df.format(physDPS)), new int[]{1, row++});
        if(elemDPS!=0)elements.put(new dev.tricht.lunaris.tooltip.elements.Label("Elemental DPS: "+ df.format(elemDPS)), new int[]{1, row++});
        if(chaosDPS!=0)elements.put(new dev.tricht.lunaris.tooltip.elements.Label("Chaos DPS: "+ df.format(chaosDPS)), new int[]{1, row++});
        elements.put(new dev.tricht.lunaris.tooltip.elements.Label("Total DPS: " + df.format(totalDPS)), new int[]{1, row++});

        TooltipCreator.create(event.getMousePos(), elements);
    }

    @Override
    public boolean supports(GameEvent event) {
        return event.getItem().getType() instanceof WeaponItem;
    }
}
