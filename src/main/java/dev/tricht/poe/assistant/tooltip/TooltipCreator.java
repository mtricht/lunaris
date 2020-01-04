package dev.tricht.poe.assistant.tooltip;

import dev.tricht.poe.assistant.item.ItemGrabber;
import javafx.application.Platform;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TooltipCreator {

    static Window window;
    static Tooltip tooltip;

    public static void create(Point position, String message) {
        if (window != null) {
            window.dispose();
        }
        window = new Window();
        tooltip = new Tooltip();
        window.add(tooltip);
        Platform.runLater(() -> {
            tooltip.init(message);
            tooltip.setPreferredSize(tooltip.getPreferredSize());
            window.show(position, tooltip.getTextLayoutBounds());
        });
    }

    public static void destroy() {
        if (window != null) {
            window.dispose();
        }
    }

}
