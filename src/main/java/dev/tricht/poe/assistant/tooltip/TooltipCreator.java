package dev.tricht.poe.assistant.tooltip;

import dev.tricht.poe.assistant.elements.Element;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TooltipCreator {

    static Window window;
    static Tooltip tooltip;

    public static void create(Point position, Map<Element, int[]> elements) {
        hide();
        tooltip = new Tooltip();
        if (window == null) {
            window = new Window();
        }
        window.add(tooltip);
        Platform.runLater(() -> {
            tooltip.init(elements);
            tooltip.setPreferredSize(tooltip.getPreferredSize());
            window.show(position, tooltip.getLayoutBounds());
        });
    }

    public static void hide() {
        if (window != null) {
            if (tooltip != null) {
                window.remove(tooltip);
                tooltip = null;
            }
            if (window.isVisible()) {
                window.setVisible(false);
            }
        }
    }

}
