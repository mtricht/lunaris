package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.util.Platform;

import java.awt.*;
import java.awt.event.KeyEvent;

public class StashScrollListener implements GameListener {

    private Robot robot;

    public StashScrollListener(Robot robot) {
        this.robot = robot;
    }

    @Override
    public void onEvent(GameEvent event) {
        if (Platform.INSTANCE.isLinux()) {
            return;
        }
        if (event.getMouseWheelRotation() > 0) {
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        } else {
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
        }
    }

    @Override
    public boolean supports(GameEvent event) {
        return event.getMouseWheelRotation() != 0;
    }
}
