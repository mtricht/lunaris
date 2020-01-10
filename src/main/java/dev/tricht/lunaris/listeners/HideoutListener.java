package dev.tricht.lunaris.listeners;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class HideoutListener implements GameListener {

    private Robot robot;

    public HideoutListener(Robot robot) {
        this.robot = robot;
    }

    @SneakyThrows
    @Override
    public void onEvent(GameEvent event) {
        this.pressAndRelease(KeyEvent.VK_ENTER);
        KeyboardUtil.type(robot, "/hideout");
        this.pressAndRelease(KeyEvent.VK_ENTER);
    }

    private void pressAndRelease(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }
}
