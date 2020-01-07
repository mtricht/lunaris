package dev.tricht.venarius.listeners;

import dev.tricht.venarius.WindowsAPI;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class StashListener implements NativeMouseWheelListener, NativeKeyListener {

    private Robot robot;
    private boolean ctrlPressed = false;

    public StashListener(Robot robot) {
        GlobalScreen.setEventDispatcher(new VoidDispatchService());
        this.robot = robot;
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent event) {
        if (WindowsAPI.isPoeActive() && ctrlPressed) {
            try {
                Field f = NativeInputEvent.class.getDeclaredField("reserved");
                f.setAccessible(true);
                f.setShort(event, (short) 0x01);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (event.getWheelRotation() > 0) {
                robot.keyPress(KeyEvent.VK_RIGHT);
            } else {
                robot.keyPress(KeyEvent.VK_LEFT);
            }
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        ctrlPressed = event.getModifiers() == NativeInputEvent.CTRL_L_MASK;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
        ctrlPressed = event.getModifiers() == NativeInputEvent.CTRL_L_MASK;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }

    private static class VoidDispatchService extends AbstractExecutorService {
        private boolean running = false;

        public VoidDispatchService() {
            running = true;
        }

        public void shutdown() {
            running = false;
        }

        public List<Runnable> shutdownNow() {
            running = false;
            return new ArrayList<Runnable>(0);
        }

        public boolean isShutdown() {
            return !running;
        }

        public boolean isTerminated() {
            return !running;
        }

        public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
            return true;
        }

        public void execute(Runnable r) {
            r.run();
        }
    }
}
