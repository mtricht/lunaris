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
        destroy();
        window = new Window();
        tooltip = new Tooltip();
        window.add(tooltip);
        Platform.runLater(() -> {
            tooltip.init(elements);
            tooltip.setPreferredSize(tooltip.getPreferredSize());
            window.show(position, tooltip.getLayoutBounds());
        });
    }

    public static void destroy() {
        if (window != null) {
            SwingUtilities.invokeLater(() -> {
                window.dispose();
            });
        }
    }

}
