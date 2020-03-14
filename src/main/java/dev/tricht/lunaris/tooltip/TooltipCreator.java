package dev.tricht.lunaris.tooltip;

import dev.tricht.lunaris.tooltip.elements.Element;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class TooltipCreator {

    static dev.tricht.lunaris.tooltip.Window window;
    static Tooltip tooltip;

    public static void create(Point position, Map<Element, int[]> elements) {
        SwingUtilities.invokeLater(() -> {
            if (window == null) {
                window = new Window();
            }
            if (tooltip == null) {
                tooltip = new Tooltip();
                window.add(tooltip);
            }
            Platform.runLater(() -> {
                tooltip.init(elements);
                tooltip.setPreferredSize(tooltip.getPreferredSize());
                window.show(position, tooltip.getLayoutBounds());
            });
        });
    }

    public static void hide() {
        SwingUtilities.invokeLater(() -> {
            if (window != null) {
                if (tooltip != null) {
                    window.remove(tooltip);
                    tooltip = null;
                }
                if (window.isVisible()) {
                    window.setVisible(false);
                }
            }
        });
    }

}
