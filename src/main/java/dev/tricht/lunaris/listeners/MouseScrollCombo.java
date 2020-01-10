package dev.tricht.lunaris.listeners;

import lombok.Getter;
import org.jnativehook.mouse.NativeMouseWheelEvent;

public class MouseScrollCombo {

    @Getter
    protected int modifier = 0;

    public MouseScrollCombo(int modifier) {
        this.modifier = modifier;
    }

    public boolean matches(NativeMouseWheelEvent event) {
        return (event.getModifiers() & this.modifier) != 0;
    }
}
