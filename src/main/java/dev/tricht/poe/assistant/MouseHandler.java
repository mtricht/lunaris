package dev.tricht.poe.assistant;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class MouseHandler implements NativeMouseInputListener, NativeMouseWheelListener {

    static Point position;
    private Robot robot;

    public MouseHandler() {
        GlobalScreen.setEventDispatcher(new VoidDispatchService());
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nativeMouseClicked(NativeMouseEvent event) {
    }

    @Override
    public void nativeMousePressed(NativeMouseEvent event) {
    }

    @Override
    public void nativeMouseReleased(NativeMouseEvent event) {
    }

    @Override
    public void nativeMouseMoved(NativeMouseEvent event) {
        position = event.getPoint();
    }

    @Override
    public void nativeMouseDragged(NativeMouseEvent event) {
    }

    @Override
    public void nativeMouseWheelMoved(NativeMouseWheelEvent event) {
        if (!WindowsAPI.isPoeActive()) {
            return;
        }
        if (KeyHandler.ctrlPressed) {
            try {
                Field f = NativeInputEvent.class.getDeclaredField("reserved");
                f.setAccessible(true);
                f.setShort(event, (short) 0x01);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            if (event.getWheelRotation() > 0) {
                robot.keyPress(KeyEvent.VK_LEFT);
            } else {
                robot.keyPress(KeyEvent.VK_RIGHT);
            }
        }
    }

    private class VoidDispatchService extends AbstractExecutorService {
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
