package dev.tricht.poe.assistant;

import dev.tricht.poe.assistant.item.ItemGrabber;
import dev.tricht.poe.assistant.listeners.*;
import javafx.application.Platform;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Assistant {

    private Robot robot;
    private ItemGrabber itemGrabber;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);

        new Assistant();
    }

    private Assistant() {
        try {
            robot = new Robot();
            itemGrabber = new ItemGrabber(robot);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
            return;
        }
        startListeners();
        // For some reason the JavaFX thread will completely stop after closing
        // the first tooltip. Setting this will prevent that from happening.
        Platform.setImplicitExit(false);
        System.out.println("Ready!");
    }

    private void startListeners() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            e.printStackTrace();
            System.exit(1);
            return;
        }

        System.out.println("Registering ItemPriceListener");
        ItemPriceListener itemPriceListener = new ItemPriceListener(itemGrabber);
        GlobalScreen.addNativeKeyListener(itemPriceListener);
        GlobalScreen.addNativeMouseMotionListener(itemPriceListener);
        GlobalScreen.addNativeMouseListener(itemPriceListener);

        System.out.println("Registering MapInfoListener");
        MapInfoListener mapInfoListener = new MapInfoListener(itemGrabber);
        GlobalScreen.addNativeKeyListener(mapInfoListener);
        GlobalScreen.addNativeMouseMotionListener(mapInfoListener);
        GlobalScreen.addNativeMouseListener(mapInfoListener);

        System.out.println("Registering StashListener");
        StashListener stashListener = new StashListener(robot);
        GlobalScreen.addNativeKeyListener(stashListener);
        GlobalScreen.addNativeMouseWheelListener(stashListener);

        System.out.println("Registering WikiListener");
        GlobalScreen.addNativeKeyListener(new WikiListener(itemGrabber));

        System.out.println("Registering HideoutListener");
        GlobalScreen.addNativeKeyListener(new HideoutListener(robot));
    }

}