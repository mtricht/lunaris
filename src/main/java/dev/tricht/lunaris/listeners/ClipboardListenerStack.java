package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.util.Platform;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class ClipboardListenerStack implements GameListener, ClipboardOwner {

    private final ItemGrabber itemGrabber;
    private final Robot robot;

    private static boolean isOwner = false;
    private static ClipboardCallback callback;

    public ClipboardListenerStack(ItemGrabber itemGrabber, Robot robot) {
        this.itemGrabber = itemGrabber;
        this.robot = robot;
        registerClipboardListener();
    }

    private ArrayList<GameListener> keyListeners = new ArrayList<>();

    public void addListener(GameListener listener) {
        keyListeners.add(listener);
    }

    @Override
    public void onEvent(GameEvent event) {
        for (GameListener listenerEntry : keyListeners) {
            if (listenerEntry.supports(event)) {
                if (!isOwner) {
                    flushClipboard();
                }
                callback = new ClipboardCallback() {
                    private boolean fired = false;
                    @Override
                    public void onContentChange(String clipboard) {
                        if (clipboard.isEmpty() || fired) {
                            return;
                        }
                        event.setItem(itemGrabber.grab(clipboard));
                        listenerEntry.onEvent(event);
                        fired = true;
                    }
                };
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

    private void registerClipboardListener() {
        Toolkit.getDefaultToolkit().getSystemClipboard().addFlavorListener(flavorEvent -> {
            if (!Platform.INSTANCE.isPoeActive()) {
                return;
            }
            String clipboardText = getClipboardText();
            log.debug("Got clipboard text:" + clipboardText);
            if (clipboardText != null && callback != null) {
                callback.onContentChange(clipboardText);
            }
        });
    }

    private void flushClipboard() {
        try {
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(""), this);
            isOwner = true;
            log.debug("Regained ownership");
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
            if (e instanceof UnsupportedFlavorException) {
                return "";
            }
            log.error("Clipboard error, trying again", e);
            // TODO: Wait a bit? Have max retries?
            return getClipboardText();
        }
    }

    private void pressControlC(GameEvent event) {
        HotKeyHandler.setPaused(true);
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
        HotKeyHandler.setPaused(false);
    }

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable contents) {
        isOwner = false;
        log.debug("Lost ownership");
    }

    private interface ClipboardCallback {
        void onContentChange(String clipboard);
    }
}
