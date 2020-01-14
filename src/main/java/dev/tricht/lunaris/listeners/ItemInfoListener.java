package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.item.Item;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
public class ItemInfoListener implements GameListener {
    private final KeyCombo combo;
    private ArrayList<GameListener> listeners = new ArrayList<>();

    public void addInfoListener(GameListener listener) {
        listeners.add(listener);
    }

    public ItemInfoListener(KeyCombo combo) {
        this.combo = combo;
    }

    @Override
    public void onEvent(GameEvent event) {
        Item item = event.getItem();
        if (item == null) {
            return;
        }

        event.setItem(item);
        for(GameListener listener : listeners) {
            if (listener.supports(event)) {
                listener.onEvent(event);
            }
        }
    }

    @Override
    public boolean supports(GameEvent event) {
        return combo.matches(event.getOriginalEvent());
    }
}
