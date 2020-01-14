package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.util.KeyboardUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class InviteLastWhisperListener implements GameListener {

    private KeyCombo combo;
    private Robot robot;

    public InviteLastWhisperListener(KeyCombo combo, Robot robot) {
        this.combo = combo;
        this.robot = robot;
    }

    @SneakyThrows
    @Override
    public void onEvent(GameEvent event) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        this.pressAndRelease(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        this.pressAndRelease(KeyEvent.VK_HOME);
        this.pressAndRelease(KeyEvent.VK_DELETE);
        KeyboardUtil.type(robot, "/invite ");
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
