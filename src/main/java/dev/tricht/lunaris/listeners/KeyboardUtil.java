package dev.tricht.lunaris.listeners;

import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.event.KeyEvent;

@Slf4j
public class KeyboardUtil {

    public static void type(Robot robot, String text) {
        text.chars().forEach(character -> {
            robot.keyPress(KeyEvent.VK_ALT);
            pressOnNumPad(robot, character);
            robot.keyRelease(KeyEvent.VK_ALT);
        });
    }

    private static void pressOnNumPad(Robot robot, int characterCode) {
        if (characterCode / 10 > 0) {
            pressOnNumPad(robot, characterCode / 10);
        }
        characterCode = numberToNumPad(characterCode % 10);
        robot.keyPress(characterCode);
        robot.keyRelease(characterCode);
    }

    private static int numberToNumPad(int number) {
        switch (number) {
            case 1: return KeyEvent.VK_NUMPAD1;
            case 2: return KeyEvent.VK_NUMPAD2;
            case 3: return KeyEvent.VK_NUMPAD3;
            case 4: return KeyEvent.VK_NUMPAD4;
            case 5: return KeyEvent.VK_NUMPAD5;
            case 6: return KeyEvent.VK_NUMPAD6;
            case 7: return KeyEvent.VK_NUMPAD7;
            case 8: return KeyEvent.VK_NUMPAD8;
            case 9: return KeyEvent.VK_NUMPAD9;
            default: return KeyEvent.VK_NUMPAD0;
        }
    }

}
