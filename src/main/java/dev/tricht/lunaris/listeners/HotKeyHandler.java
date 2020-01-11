package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.util.Platform;
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
import java.util.Map;

@Slf4j
public class HotKeyHandler implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {

    private Point position;
    private HashMap<KeyCombo, GameListener> keyListeners = new HashMap<>();
    private HashMap<MouseScrollCombo, GameListener> scrollListeners = new HashMap<>();

    public void addListener(KeyCombo combo, GameListener listener) {
        keyListeners.put(combo, listener);
    }

    public void addListener(MouseScrollCombo combo, GameListener listener) {
        scrollListeners.put(combo, listener);
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!Platform.isPoeActive()) {
            return;
        }
        for (Map.Entry<KeyCombo, GameListener> listenerEntry : keyListeners.entrySet()) {
            if (listenerEntry.getKey().matches(event)) {
                VoidDispatchService.consume(event);
                GameEvent gameEvent = new GameEvent();
                gameEvent.setMousePos(position);
                gameEvent.setOriginalEvent(event);
                listenerEntry.getValue().onEvent(gameEvent);
                return;
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
        TooltipCreator.hide();
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent event) {
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent event) {
        if (!Platform.isPoeActive()) {
            return;
        }
        for (Map.Entry<MouseScrollCombo, GameListener> listenerEntry : scrollListeners.entrySet()) {
            if (listenerEntry.getKey().matches(event)) {
                VoidDispatchService.consume(event);
                GameEvent gameEvent = new GameEvent();
                gameEvent.setMousePos(position);
                gameEvent.setMouseWheelRotation(event.getWheelRotation());
                listenerEntry.getValue().onEvent(gameEvent);
                return;
            }
        }
    }
}
