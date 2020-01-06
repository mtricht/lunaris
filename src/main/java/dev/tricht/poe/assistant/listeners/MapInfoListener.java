package dev.tricht.poe.assistant.listeners;

import dev.tricht.poe.assistant.WindowsAPI;
import dev.tricht.poe.assistant.data.MapInfo;
import dev.tricht.poe.assistant.data.MapInfoResolver;
import dev.tricht.poe.assistant.elements.*;
import dev.tricht.poe.assistant.elements.Image;
import dev.tricht.poe.assistant.elements.Label;
import dev.tricht.poe.assistant.item.Item;
import dev.tricht.poe.assistant.item.ItemGrabber;
import dev.tricht.poe.assistant.item.types.MapItem;
import dev.tricht.poe.assistant.tooltip.TooltipCreator;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class MapInfoListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private MapInfoResolver mapInfoResolver;
    private Point position;

    private Pattern damageAffix = Pattern.compile("(?i:Monsters deal .*% extra damage as .*)");
    private Pattern elementalAffix = Pattern.compile("(?i:Monsters reflect .*% of Elemental Damage)");
    private Pattern physicalAffix = Pattern.compile("(?i:Monsters reflect .*% of Physical Damage)");
    private Pattern recoveryAffix = Pattern.compile("(?i:Players have .*% less Recovery Rate of Life and Energy Shield)");

    public MapInfoListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
        this.mapInfoResolver = new MapInfoResolver();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        try {
            if (event.getKeyCode() == NativeKeyEvent.VC_A && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
                log.debug("Trying map info");
                Item item = this.itemGrabber.grab();
                if (item == null || !(item.getType() instanceof MapItem)) {
                    log.debug("Not a map!");
                    return;
                }

                MapInfo mapInfo = this.mapInfoResolver.getMapInfo(item.getBase());

                log.debug("Got a map, creating UI");

                Map<Element, int[]> elements = new LinkedHashMap<>();
                elements.put(new Icon(item, 48), new int[]{0, 0});
                elements.put(new ItemName(item, 48 + Icon.PADDING), new int[]{1, 0});
                List<String> warnings = this.getMapModWarnings(item);
                int column = 1;
                if (!warnings.isEmpty()) {
                    elements.put(new Label("Warning: " + String.join("; ", warnings),
                            new javafx.scene.paint.Color(1, 0.33, 0.33, 1)), new int[]{1, column++});
                }
                if (mapInfo != null) {
                    if (mapInfo.getBosses().size() == 1) {
                        String imageFileName = "/boss_images/" + mapInfo.getBosses().get(0).replace(" ", "_") + ".png";
                        URL imageUrl = MapInfoListener.class.getResource(imageFileName);
                        if (imageUrl != null) {
                            elements.put(new Image(imageFileName), new int[]{1, column++});
                        }
                    }
                    if (mapInfo.getBosses().size() > 0) {
                        elements.put(new Label("Boss(es): " + String.join("; ", mapInfo.getBosses())), new int[]{1, column++});
                    }
                    if (mapInfo.getRegion() != null && !mapInfo.getRegion().isEmpty()) {
                        elements.put(new Label("Region: " + mapInfo.getRegion()), new int[]{1, column++});
                    }
                    if (mapInfo.getPantheon() != null && !mapInfo.getPantheon().isEmpty()) {
                        elements.put(new Label("Pantheon: " + mapInfo.getPantheon()), new int[]{1, column++});
                    }
                }

                TooltipCreator.create(position, elements);
            }
        } catch (Exception e) {
            log.error("Exception while displaying map", e);
        }
    }

    private List<String> getMapModWarnings(Item item) {
        List<String> warnings = new ArrayList<>();
        int damageMods = 0;
        for (String affix : item.getAffixes()) {
            if (damageAffix.matcher(affix).matches()) {
                damageMods++;
            }
            if (elementalAffix.matcher(affix).matches()) {
                warnings.add("Reflects elemental");
            }
            if (physicalAffix.matcher(affix).matches()) {
                warnings.add("Reflects physical");
            }
            if (affix.equals("Players are Cursed with Temporal Chains")) {
                warnings.add("Temporal chains");
            }
            if (affix.equals("Cannot Leech Life from Monsters")) {
                warnings.add("No life leech");
            }
            if (affix.equals("Cannot Leech Life from Monsters")) {
                warnings.add("No life leech");
            }
            if (recoveryAffix.matcher(affix).matches()) {
                warnings.add("Less recovery of Life and ES");
            }
        }
        if (damageMods >= 2) {
            warnings.add(String.format("Multi (%d) extra damage", damageMods));
        }
        return warnings;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent event) {
        position = event.getPoint();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent event) {
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent event) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent event) {
        TooltipCreator.hide();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent event) {
    }
}
