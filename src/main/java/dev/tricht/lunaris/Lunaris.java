package dev.tricht.lunaris;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.util.DirectoryManager;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.item.types.CurrencyItem;
import dev.tricht.lunaris.item.types.MapItem;
import dev.tricht.lunaris.listeners.*;
import dev.tricht.lunaris.ninja.poe.ItemResolver;
import dev.tricht.lunaris.util.ErrorUtil;
import dev.tricht.lunaris.util.PropertiesManager;
import dev.tricht.lunaris.util.SystemTray;
import javafx.application.Platform;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class Lunaris {

    private Robot robot;
    private PathOfExileAPI pathOfExileAPI;
    private ItemGrabber itemGrabber;
    private ItemResolver itemResolver;

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        logger.setUseParentHandlers(false);
        new Lunaris();
    }

    private Lunaris() {
        DirectoryManager.getDirectory();
        PropertiesManager.load();
        try {
            pathOfExileAPI = new PathOfExileAPI();
        } catch (Exception e) {
            log.error("Failed talking to pathofexile.com", e);
            ErrorUtil.showErrorDialogAndExit("Couldn't talk with pathofexile.com, perhaps down for maintenance?");
        }
        String leagueName = SystemTray.create(pathOfExileAPI, this::changeLeague);
        try {
            robot = new Robot();
            itemResolver = new ItemResolver(leagueName);
            itemGrabber = new ItemGrabber(robot, itemResolver);
        } catch (IOException | AWTException e) {
            log.error("Failed to initialize robot", e);
            return;
        }
        startListeners();
        // For some reason the JavaFX thread will completely stop after closing
        // the first tooltip. Setting this will prevent that from happening.
        Platform.setImplicitExit(false);
        log.debug("Ready!");
    }

    private void startListeners() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            log.error("Failed to start native hooks", e);
            System.exit(1);
            return;
        }

        GlobalScreen.setEventDispatcher(new VoidDispatchService());

        HotKeyHandler handler = new HotKeyHandler();
        GlobalScreen.addNativeKeyListener(handler);
        GlobalScreen.addNativeMouseMotionListener(handler);
        GlobalScreen.addNativeMouseListener(handler);
        GlobalScreen.addNativeMouseWheelListener(handler);

        ItemInfoListener infoListener = new ItemInfoListener(itemGrabber);
        handler.addListener(new KeyCombo(NativeKeyEvent.VC_A, NativeInputEvent.ALT_L_MASK), infoListener);

        handler.addListener(new KeyCombo(NativeKeyEvent.VC_F5), new HideoutListener(robot));
        handler.addListener(new KeyCombo(NativeKeyEvent.VC_W, NativeInputEvent.ALT_L_MASK), new WikiListener(itemGrabber));
        handler.addListener(new MouseScrollCombo(NativeInputEvent.CTRL_L_MASK), new StashScrollListener(robot));

        ItemPriceListener priceListener = new ItemPriceListener(itemGrabber, pathOfExileAPI);
        handler.addListener(new KeyCombo(NativeKeyEvent.VC_D, NativeInputEvent.ALT_L_MASK), priceListener);
        handler.addListener(new KeyCombo(NativeKeyEvent.VC_Q, NativeInputEvent.ALT_L_MASK), priceListener);

        infoListener.addInfoListener(MapItem.class.getName(), new MapInfoListener());
        infoListener.addInfoListener(CurrencyItem.class.getName(), new CurrencyStackListener());

        GlobalScreen.addNativeMouseListener(priceListener);
    }

    private void changeLeague(String leagueName) {
        if (itemResolver != null) {
            itemResolver.refresh(leagueName);
        }
        pathOfExileAPI.setLeague(leagueName);
    }

}