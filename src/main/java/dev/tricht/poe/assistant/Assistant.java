package dev.tricht.poe.assistant;

import javafx.application.Platform;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.mouse.GlobalMouseHook;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Assistant {

    private Window window;
    private Tooltip tooltip;

    public static void main(String[] args) {
        new Assistant();
    }

    private Assistant() {
        startListeners();
        Platform.setImplicitExit(false);
    }

    private void startListeners() {
        GlobalKeyboardHook keyboardHook = new GlobalKeyboardHook();
        keyboardHook.addKeyListener(new KeyHandler((this::createTooltip)));
        GlobalMouseHook mouseHook = new GlobalMouseHook();
        mouseHook.addMouseListener(new MouseHandler());
    }

    private void createTooltip(ItemRequest itemRequest) {
        if (this.window != null) {
            this.window.dispose();
        }
        this.window = new Window();
        this.tooltip = new Tooltip();
        window.add(tooltip);
        tooltip.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                window.dispose();
            }
        });
        Platform.runLater(() -> {
            tooltip.init(itemRequest.itemText);
            window.show(itemRequest.position, tooltip.getTextLayoutBounds());
        });
    }

}