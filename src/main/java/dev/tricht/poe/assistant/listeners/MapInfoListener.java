package dev.tricht.poe.assistant.listeners;

import dev.tricht.poe.assistant.WindowsAPI;
import dev.tricht.poe.assistant.elements.*;
import dev.tricht.poe.assistant.elements.Image;
import dev.tricht.poe.assistant.elements.Label;
import dev.tricht.poe.assistant.item.Item;
import dev.tricht.poe.assistant.item.ItemGrabber;
import dev.tricht.poe.assistant.item.types.MapItem;
import dev.tricht.poe.assistant.tooltip.TooltipCreator;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;

public class MapInfoListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private Point position;

    public MapInfoListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        if (event.getKeyCode() == NativeKeyEvent.VC_A && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
            try {
                System.out.println("Trying map info");
                Item item = this.itemGrabber.grab();
                if (!(item.getType() instanceof MapItem)) {
                    System.out.println("Not a map!");
                    return;
                }
                System.out.println("Got a map, creating UI");
                Map<Element, int[]> elements = Map.ofEntries(
                        new AbstractMap.SimpleEntry<Element, int[]>(new Icon(item, 48), new int[]{0, 0}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new ItemName(item,48 + Icon.PADDING), new int[]{1, 0}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new Image("desert_spring.png"), new int[]{1, 1}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new Label("Boss: Terror of the Infinite Drifts"), new int[]{1, 2}),
                        new AbstractMap.SimpleEntry<Element, int[]>(new Label("Pantheon: Immune to poison"), new int[]{1, 3})
                );

                TooltipCreator.create(position, elements);
            } catch (IOException | UnsupportedFlavorException e) {
                e.printStackTrace();
            }
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
