package dev.tricht.lunaris.listeners;

import lombok.extern.slf4j.Slf4j;
import org.jnativehook.keyboard.NativeKeyEvent;

@Slf4j
public class KeyCombo {
    private String combo;

    public KeyCombo(String combo) {
        this.combo = combo;
    }

    public boolean matches(NativeKeyEvent event) {
        String modifiers = NativeKeyEvent.getModifiersText(event.getModifiers());
        String keyText = NativeKeyEvent.getKeyText(event.getKeyCode());

        if(!modifiers.equals("")) {
            modifiers += "+";
        }
        keyText = modifiers + keyText;
        return combo.equals(keyText);
    }
}
