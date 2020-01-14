package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.item.ItemGrabber;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class ClipboardListenerStack implements GameListener {

    private final ItemGrabber itemGrabber;
    private final Robot robot;

    private FlavorListener currentFlavorListener = null;

    public ClipboardListenerStack(ItemGrabber itemGrabber, Robot robot) {
        this.itemGrabber = itemGrabber;
        this.robot = robot;

        // Set clipboard owner to us so we can start catching events from POE
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), null);
    }

    private ArrayList<GameListener> keyListeners = new ArrayList<>();

    public void addListener(GameListener listener) {
        keyListeners.add(listener);
    }

    @Override
    public void onEvent(GameEvent event) {
        for (GameListener listenerEntry : keyListeners) {
            if (listenerEntry.supports(event)) {
                onClipboardChange((String clipboard) -> {
                    event.setItem(itemGrabber.grab(clipboard));
                    listenerEntry.onEvent(event);
                });
                log.debug("Registered clipboard listener for " + listenerEntry.getClass().getName());
                pressControlC(event);
                return;
            }
        }
    }

    @Override
    public boolean supports(GameEvent event) {
        for (GameListener listenerEntry : keyListeners) {
            if (listenerEntry.supports(event)) {
                return true;
            }
        }
        return false;
    }

    private void onClipboardChange(ClipboardCallback callback) {
        if (currentFlavorListener != null) {
            Toolkit.getDefaultToolkit().getSystemClipboard().removeFlavorListener(currentFlavorListener);
        }
        currentFlavorListener = new FlavorListener() {
            @Override
            public void flavorsChanged(FlavorEvent flavorEvent) {
                String clipboardText = getClipboardText();
                log.debug("Got clipboard text:" + clipboardText);
                if (clipboardText != null) {
                    callback.onContentChange(clipboardText);
                }

                Toolkit.getDefaultToolkit().getSystemClipboard().removeFlavorListener(this);
                log.debug("Unregistered clipboard listener");
                flushClipboard();
            }
        };

        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(currentFlavorListener);
    }

    private void flushClipboard() {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), null);
        } catch (IllegalStateException e) {
            log.error("Clipboard error, trying again", e);
            // TODO: Wait a bit? Have max retries?
            flushClipboard();
        }
    }
    private String getClipboardText() {
        try {
            return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (IllegalStateException | IOException | UnsupportedFlavorException e) {
            log.error("Clipboard error, trying again", e);
            // TODO: Wait a bit? Have max retries?
            return getClipboardText();
        }
    }

    private void pressControlC(GameEvent event) {
        boolean wasControlAlreadyPressed = (event.getOriginalEvent().getModifiers() & 34) != 0;

        if (!wasControlAlreadyPressed) {
            robot.keyPress(KeyEvent.VK_CONTROL);
        }
        robot.keyPress(KeyEvent.VK_C);
        log.debug("Pressed ctrl+c");

        if (!wasControlAlreadyPressed) {
            robot.keyRelease(KeyEvent.VK_CONTROL);
        }
        robot.keyRelease(KeyEvent.VK_C);
    }

    private interface ClipboardCallback {
        void onContentChange(String clipboard);
    }
}
