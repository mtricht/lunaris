package dev.tricht.poe.assistant.listeners;

import dev.tricht.poe.assistant.WindowsAPI;
import dev.tricht.poe.assistant.elements.*;
import dev.tricht.poe.assistant.elements.Label;
import dev.tricht.poe.assistant.item.Item;
import dev.tricht.poe.assistant.item.ItemGrabber;
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
public class ItemPriceListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private Point position;

    public ItemPriceListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        try {
            if (!WindowsAPI.isPoeActive()) {
                return;
            }

            if (event.getKeyCode() == NativeKeyEvent.VC_D && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
                log.debug("Running price checker");
                log.debug("Grabbing item");
                Item item = this.itemGrabber.grab();
                if (item == null || !item.hasPrice()) {
                    log.debug("No item selected.");
                    return;
                }

                log.debug("Got item, creating UI");


                Map<Element, int[]> elements = new LinkedHashMap<>();
                elements.put(new Icon(item, 48), new int[]{0, 0});
                elements.put(new ItemName(item,48 + Icon.PADDING), new int[]{1, 0});
                elements.put(new Price(item), new int[]{1, 1});

                if (item.getMeanPrice().getReason() != null) {
                    elements.put(new Label("Reason: " + item.getMeanPrice().getReason()), new int[]{1, 2});
                }

                elements.put(new Source("poe.ninja"), new int[]{1, elements.size() - 1});

                TooltipCreator.create(position, elements);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
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
