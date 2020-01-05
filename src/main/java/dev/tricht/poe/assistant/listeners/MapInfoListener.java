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
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class MapInfoListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private MapInfoResolver mapInfoResolver;
    private Point position;

    public MapInfoListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
        this.mapInfoResolver = new MapInfoResolver();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
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
            elements.put(new Image("desert_spring.png"), new int[]{1, 1});
            if (mapInfo.getBosses().size() > 0) {
                elements.put(new Label("Boss(es): " + String.join(",", mapInfo.getBosses())), new int[]{1, 2});
            }
            if (mapInfo.getRegion() != null && !mapInfo.getRegion().isEmpty()) {
                elements.put(new Label("Region: " + mapInfo.getRegion()), new int[]{1, 3});
            }
            if (mapInfo.getPantheon() != null && !mapInfo.getPantheon().isEmpty()) {
                elements.put(new Label("Pantheon: " + mapInfo.getPantheon()), new int[]{1, 4});
            }

            TooltipCreator.create(position, elements);
        }
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
        TooltipCreator.destroy();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent event) {
    }
}
