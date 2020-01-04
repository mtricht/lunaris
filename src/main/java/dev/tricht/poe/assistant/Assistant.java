package dev.tricht.poe.assistant;

import dev.tricht.poe.assistant.tooltip.ItemRequest;
import dev.tricht.poe.assistant.tooltip.Tooltip;
import dev.tricht.poe.assistant.tooltip.Window;
import javafx.application.Platform;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Assistant {

    private Window window;
    private Tooltip tooltip;
    private ItemParser itemParser;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
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
        // For some reason the JavaFX thread will completely stop after closing
        // the first tooltip. Setting this will prevent that from happening.
        Platform.setImplicitExit(false);
        System.out.println("Ready!");
    }

    private void startListeners() {
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(new KeyHandler(this::createTooltip));
        MouseHandler mouseHandler = new MouseHandler();
        GlobalScreen.addNativeMouseListener(mouseHandler);
        GlobalScreen.addNativeMouseMotionListener(mouseHandler);
        GlobalScreen.addNativeMouseWheelListener(mouseHandler);
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