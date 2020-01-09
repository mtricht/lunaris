package dev.tricht.lunaris.listeners;

import lombok.SneakyThrows;

import java.awt.*;
import java.awt.event.KeyEvent;

public class HideoutListener implements GameListener {

    private Robot robot;

    public HideoutListener(Robot robot) {
        this.robot = robot;
    }

    @SneakyThrows
    @Override
    public void onEvent(GameEvent event) {
        this.pressAndRelease(KeyEvent.VK_ENTER);
        this.pressAndRelease(KeyEvent.VK_SLASH);
        this.pressAndRelease(KeyEvent.VK_H);
        this.pressAndRelease(KeyEvent.VK_I);
        this.pressAndRelease(KeyEvent.VK_D);
        this.pressAndRelease(KeyEvent.VK_E);
        this.pressAndRelease(KeyEvent.VK_O);
        this.pressAndRelease(KeyEvent.VK_U);
        this.pressAndRelease(KeyEvent.VK_T);
        this.pressAndRelease(KeyEvent.VK_ENTER);
    }

    private void pressAndRelease(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }
}
