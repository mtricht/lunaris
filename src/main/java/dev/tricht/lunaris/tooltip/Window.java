package dev.tricht.lunaris.tooltip;

import dev.tricht.lunaris.util.Platform;
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
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();

        if ((position.getX() + width) > screenWidth) {
            position.setLocation(screenWidth - width, position.getY());
        }
        if ((position.getY() + height) > screenHeight) {
            position.setLocation(position.getX(), screenHeight - height);
        }

        if (Platform.INSTANCE.isLinux()) {
            GraphicsConfiguration config = getGraphicsConfiguration();
            GraphicsDevice device = config.getDevice();
            Rectangle rectangle = device.getDefaultConfiguration().getBounds();

            if (rectangle.x > position.x) {
                position = new Point(rectangle.x + position.x, position.y);
            }
        }

        if (!isVisible() || getWidth() != width || getHeight() != height || position.x != getLocation().x || position.y != getLocation().y) {
            setLocation(position);
            setSize(width, height);
            setVisible(true);
        }
    }
}
