package dev.tricht.lunaris.listeners;

import lombok.Getter;

public class KeyCombo {

    @Getter
    private int modifier = 0;

    @Getter
    private int key;

    public KeyCombo(int key) {
        this.key = key;
    }

    public KeyCombo(int key, int modifier) {
        this.modifier = modifier;
        this.key = key;
    }

    public int toInt() {
        return key + (modifier * 1000);
    }
}
