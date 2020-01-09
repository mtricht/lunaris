package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.WindowsAPI;
import dev.tricht.lunaris.data.MapInfo;
import dev.tricht.lunaris.data.MapInfoResolver;
import dev.tricht.lunaris.elements.*;
import dev.tricht.lunaris.elements.Label;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.item.types.CurrencyItem;
import dev.tricht.lunaris.item.types.MapItem;
import dev.tricht.lunaris.tooltip.TooltipCreator;
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
public class CurrencyStackListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private Point position;

    public CurrencyStackListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        try {
            if (event.getKeyCode() == NativeKeyEvent.VC_A && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
                VoidDispatchService.consume(event);
                log.debug("Trying currency stack");
                Item item = this.itemGrabber.grab();
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

                TooltipCreator.create(position, elements);
            }
        } catch (Exception e) {
            log.error("Exception while displaying map", e);
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
        TooltipCreator.hide();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent event) {
    }
}
