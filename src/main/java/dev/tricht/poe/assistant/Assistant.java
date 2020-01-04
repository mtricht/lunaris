package dev.tricht.poe.assistant;

import dev.tricht.poe.assistant.tooltip.ItemRequest;
import dev.tricht.poe.assistant.tooltip.Tooltip;
import dev.tricht.poe.assistant.tooltip.Window;
import javafx.application.Platform;
import lc.kra.system.keyboard.GlobalKeyboardHook;
import lc.kra.system.mouse.GlobalMouseHook;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class Assistant {

    private Window window;
    private Tooltip tooltip;
    private ItemParser itemParser;

    public static void main(String[] args) {
        new Assistant();
    }

    private Assistant() {
        try {
            itemParser = new ItemParser();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return;
        }
        startListeners();
        Platform.setImplicitExit(false);
        System.out.println("Ready!");
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
        Item item;
        try {
            item = itemParser.parse(itemRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return;
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
            tooltip.init(item.toString());
            tooltip.setPreferredSize(tooltip.getPreferredSize());
            window.show(itemRequest.position, tooltip.getTextLayoutBounds());
        });
    }

}