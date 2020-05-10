package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.util.Platform;
import dev.tricht.lunaris.tooltip.TooltipCreator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.awt.*;
import java.lang.annotation.Native;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HotKeyHandler implements NativeKeyListener, NativeMouseInputListener, NativeMouseWheelListener {

    @Setter
    private static boolean isPaused = false;

    private Point position;

    private ArrayList<GameListener> keyListeners = new ArrayList<>();
    private HashMap<MouseScrollCombo, GameListener> scrollListeners = new HashMap<>();

    private ArrayList<KeyCombo> listenCombos;

    public void addListener(GameListener listener) {
        keyListeners.add(listener);
    }

    public void addListener(MouseScrollCombo combo, GameListener listener) {
        scrollListeners.put(combo, listener);
    }

    /**
     * For performance reasons, prefill the handler with combos it should listen to,
     * so we don't have to create GameEvent and check every listener for every nativeKeyPressed
     */
    public void setRespondTo(ArrayList<KeyCombo> combos) {
        listenCombos = combos;
    }

    public boolean shouldRespond(NativeKeyEvent event) {
        if (isPaused) {
            return false;
        }
        for (KeyCombo combo : listenCombos) {
            if (combo.matches(event)) {
                return true;
            }
        }
        return false;
    }

    public void removeListeners() {
        this.keyListeners = new ArrayList<>();
        this.scrollListeners = new HashMap<>();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (!Platform.INSTANCE.isPoeActive()) {
            return;
        }

        if (!shouldRespond(event)) {
            return;
        }

        GameEvent gameEvent = new GameEvent();
        gameEvent.setMousePos(position);
        gameEvent.setOriginalEvent(event);

        for (GameListener listenerEntry : keyListeners) {
            if (listenerEntry.supports(gameEvent)) {
                VoidDispatchService.consume(event);
                listenerEntry.onEvent(gameEvent);
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
        if (!Platform.INSTANCE.isPoeActive()) {
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
