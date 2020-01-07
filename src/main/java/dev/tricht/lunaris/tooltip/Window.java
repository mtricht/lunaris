package dev.tricht.lunaris.tooltip;

import javafx.geometry.Bounds;

import javax.swing.*;
import java.awt.*;

class Window extends JFrame {

    Window() {
        setTitle("Lunaris item information");
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
        setResizable(false);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
    }

    void show(Point position, Bounds bounds) {
        int width = (int) Math.ceil(bounds.getWidth());
        int height = (int) Math.ceil(bounds.getHeight());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if ((position.getX() + width) > screenSize.getWidth()) {
            position.setLocation(screenSize.getWidth() - width, position.getY());
        }
        if ((position.getY() + height) > screenSize.getHeight()) {
            position.setLocation(position.getX(), screenSize.getHeight() - height);
        }
        setSize(width, height);
        setLocation(position);
        setVisible(true);
    }
}
