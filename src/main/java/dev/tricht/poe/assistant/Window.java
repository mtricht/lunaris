package dev.tricht.poe.assistant;

import javafx.geometry.Bounds;

import javax.swing.*;
import java.awt.*;

class Window extends JFrame {

    Window() {
        setTitle("PoE Assistant by mtricht");
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0.7f));
        setResizable(false);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
        setSize(1, 1);
    }

    void show(Point position, Bounds bounds) {
        setSize((int) bounds.getWidth(), (int) bounds.getHeight());
        setLocation(position);
        setVisible(true);
    }
}
