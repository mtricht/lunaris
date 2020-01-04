package dev.tricht.poe.assistant.listeners;

import dev.tricht.poe.assistant.WindowsAPI;
import dev.tricht.poe.assistant.item.ItemGrabber;
import dev.tricht.poe.assistant.tooltip.Tooltip;
import dev.tricht.poe.assistant.tooltip.TooltipCreator;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.awt.*;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class ItemPriceListener implements NativeKeyListener, NativeMouseInputListener {

    private ItemGrabber itemGrabber;
    private Point position;

    public ItemPriceListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        if (event.getKeyCode() == NativeKeyEvent.VC_D && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
            try {
                TooltipCreator.create(position, this.itemGrabber.grab().toString());
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
