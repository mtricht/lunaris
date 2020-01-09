package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.WindowsAPI;
import dev.tricht.lunaris.tooltip.TooltipCreator;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.awt.*;
import java.util.HashMap;

@Slf4j
public class HotkeyHandler implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {

    private Point position;
    private HashMap<Integer, GameListener> listeners = new HashMap<>();
    private NativeKeyEvent currentKeyEvent;

    public void addListener(KeyCombo combo, GameListener listener) {
        listeners.put(combo.toInt(), listener);
    }


    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        currentKeyEvent = event;
        KeyCombo combo = new KeyCombo(event.getKeyCode(), event.getModifiers());
        if (!listeners.containsKey(combo.toInt())) {
            return;
        }

        VoidDispatchService.consume(event);

        GameEvent gameEvent = new GameEvent();
        gameEvent.setMousePos(position);
        listeners.get(combo.toInt()).onEvent(gameEvent);
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        currentKeyEvent = null;
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

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent nativeMouseWheelEvent) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }

        int combo = new MouseScrollCombo(currentKeyEvent.getModifiers()).toInt();
        if (!listeners.containsKey(combo)) {
            return;
        }

        VoidDispatchService.consume(nativeMouseWheelEvent);

        GameEvent gameEvent = new GameEvent();
        gameEvent.setMousePos(position);
        gameEvent.setMouseWheelRotation(nativeMouseWheelEvent.getWheelRotation());
        listeners.get(combo).onEvent(gameEvent);
    }
}
