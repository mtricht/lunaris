package dev.tricht.lunaris.listeners;

public class MouseScrollCombo extends KeyCombo {

    public static int MOUSE_MODIFIER = 999999;

    public MouseScrollCombo(int modifier) {
        super(0, modifier);
    }

    public int toInt() {
        return MOUSE_MODIFIER + (this.getModifier() * 1000);
    }
}
