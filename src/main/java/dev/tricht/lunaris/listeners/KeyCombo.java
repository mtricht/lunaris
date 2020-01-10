package dev.tricht.lunaris.listeners;

import lombok.Getter;
import org.jnativehook.keyboard.NativeKeyEvent;

public class KeyCombo {

    @Getter
    protected int modifier = 0;

    @Getter
    private int key;

    public KeyCombo(int key) {
        this.key = key;
    }

    public KeyCombo(int key, int modifier) {
        this.modifier = modifier;
        this.key = key;
    }

    public boolean matches(NativeKeyEvent event) {
        if (this.modifier == 0) {
            return event.getKeyCode() == key && this.modifier == 0;
        }
        return event.getKeyCode() == key && (event.getModifiers() & this.modifier) != 0;
    }
}
