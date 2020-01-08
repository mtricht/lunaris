package dev.tricht.lunaris.listeners;

import org.jnativehook.NativeInputEvent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

public class VoidDispatchService extends AbstractExecutorService {
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

    public static void consume(NativeInputEvent event) {
        try {
            Field f = NativeInputEvent.class.getDeclaredField("reserved");
            f.setAccessible(true);
            f.setShort(event, (short) 0x01);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
