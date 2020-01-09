package dev.tricht.lunaris.listeners;

import java.awt.*;
import java.awt.event.KeyEvent;

public class StashScrollListener implements GameListener {

    private Robot robot;

    public StashScrollListener(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (event.getMouseWheelRotation() > 0) {
            robot.keyPress(KeyEvent.VK_RIGHT);
        } else {
            robot.keyPress(KeyEvent.VK_LEFT);
        }
    }
}
