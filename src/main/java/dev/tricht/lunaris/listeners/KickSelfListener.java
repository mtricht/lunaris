package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.util.KeyboardUtil;
import dev.tricht.lunaris.util.PropertiesManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class KickSelfListener implements GameListener {

    private KeyCombo combo;
    private Robot robot;

    public KickSelfListener(KeyCombo combo, Robot robot) {
        this.combo = combo;
        this.robot = robot;
    }

    @SneakyThrows
    @Override
    public void onEvent(GameEvent event) {
        this.pressAndRelease(KeyEvent.VK_ENTER);
        KeyboardUtil.type(robot, "/kick " + PropertiesManager.getProperty(PropertiesManager.CHARACTER_NAME));
        this.pressAndRelease(KeyEvent.VK_ENTER);
    }

    @Override
    public boolean supports(GameEvent event) {
        return combo.matches(event.getOriginalEvent());
    }

    private void pressAndRelease(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }
}
