package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.WindowsAPI;
import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.com.pathofexile.response.SearchResponse;
import dev.tricht.lunaris.elements.Label;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.tooltip.TooltipCreator;
import dev.tricht.lunaris.elements.*;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ItemPriceListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private Point position;
    private PathOfExileAPI pathOfExileAPI;

    public ItemPriceListener(ItemGrabber itemGrabber, PathOfExileAPI pathOfExileAPI) {
        this.itemGrabber = itemGrabber;
        this.pathOfExileAPI = pathOfExileAPI;
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

            if (event.getKeyCode() == NativeKeyEvent.VC_F && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
                log.debug("pathofexile.com/trade");
                Item item = this.itemGrabber.grab();
                if (item == null || !item.hasPrice()) {
                    log.debug("No item selected.");
                    return;
                }
                log.debug("Got item, translating to pathofexile.com");
                SearchResponse searchResponse = this.pathOfExileAPI.find(item);
                if (searchResponse != null && searchResponse.getId() != null) {
                    if (event.getModifiers() == NativeInputEvent.SHIFT_L_MASK) {
                        WindowsAPI.browse(searchResponse.getUrl());
                        return;
                    } else {
                        // TODO: Request detailed information.
                        // TODO: Create tooltip
                    }
                }
                log.debug(searchResponse.toString());
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
        TooltipCreator.hide();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent event) {
    }
}
