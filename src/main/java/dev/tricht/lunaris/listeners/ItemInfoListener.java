package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.data.MapInfoResolver;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemGrabber;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.util.HashMap;

@Slf4j
public class ItemInfoListener implements GameListener {

    private ItemGrabber itemGrabber;

    private HashMap<String, GameListener> listeners = new HashMap<>();

    public ItemInfoListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    public void addInfoListener(String itemType, GameListener listener) {
        listeners.put(itemType, listener);
    }

    @Override
    public void onEvent(GameEvent event) {
        Item item = itemGrabber.grab();

        if (item == null) {
            return;
        }

        if (listeners.containsKey(item.getType().getClass().getName())) {
            GameListener listener = listeners.get(item.getType().getClass().getName());

            event.setItem(item);
            listener.onEvent(event);
        }
    }
}
