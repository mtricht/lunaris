package dev.tricht.poe.assistant.tooltip;

import javafx.geometry.Bounds;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window() {
        setTitle("PoE Assistant");
        setUndecorated(true);
        setBackground(new Color(1.0f,1.0f,1.0f,0.0f));
        setResizable(false);
        setAlwaysOnTop(true);
        setFocusableWindowState(false);
    }

    public void show(Point position, Bounds bounds) {
        setSize((int) bounds.getWidth(), (int) bounds.getHeight());
        setLocation(position);
        setVisible(true);
    }
}
