package dev.tricht.lunaris.settings;

import javafx.geometry.Bounds;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JFrame {

    public SettingsWindow() {
        setTitle("Lunaris | Settings");
        setResizable(false);
    }

    public void showSettings(Bounds layoutBounds) {
        int width = (int) Math.ceil(layoutBounds.getWidth());
        int height = (int) Math.ceil(layoutBounds.getHeight());

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        setLocation(screenSize.width / 2 - (width / 2), screenSize.height / 2 - (height / 2));
        setSize(width, height);
        setVisible(true);
    }
}
