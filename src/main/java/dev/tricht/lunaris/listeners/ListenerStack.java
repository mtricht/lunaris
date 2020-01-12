package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.item.types.CurrencyItem;
import dev.tricht.lunaris.item.types.MapItem;
import dev.tricht.lunaris.util.PropertiesManager;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;

import java.awt.*;

@Slf4j
public class ListenerStack {

    private ItemPriceListener priceListener = null;

    public void startListeners(ItemGrabber itemGrabber, Robot robot, PathOfExileAPI pathOfExileAPI) {
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

        PropertiesManager.addPropertyListener("keybinds(.*)", () -> {
            log.debug("Restarting listeners after keybind change");
            handler.removeListeners();
            if (priceListener != null) {
                GlobalScreen.removeNativeMouseListener(priceListener);
            }

            startListeners(handler, itemGrabber, robot, pathOfExileAPI);
        });

        startListeners(handler, itemGrabber, robot, pathOfExileAPI);
    }

    private void startListeners(HotKeyHandler handler, ItemGrabber itemGrabber, Robot robot, PathOfExileAPI pathOfExileAPI) {
        ItemInfoListener infoListener = new ItemInfoListener(itemGrabber);
        infoListener.addInfoListener(MapItem.class.getName(), new MapInfoListener());
        infoListener.addInfoListener(CurrencyItem.class.getName(), new CurrencyStackListener());

        handler.addListener(new KeyCombo(PropertiesManager.getProperty("keybinds.item_info", "Alt+A")), infoListener);
        handler.addListener(new KeyCombo(PropertiesManager.getProperty("keybinds.hideout", "F5")), new HideoutListener(robot));
        handler.addListener(new KeyCombo(PropertiesManager.getProperty("keybinds.wiki", "Alt+W")), new WikiListener(itemGrabber));
        handler.addListener(new MouseScrollCombo(NativeInputEvent.CTRL_L_MASK), new StashScrollListener(robot));

        if (priceListener != null) {
            GlobalScreen.removeNativeMouseListener(priceListener);
        }

        priceListener = new ItemPriceListener(itemGrabber, pathOfExileAPI);
        handler.addListener(new KeyCombo(PropertiesManager.getProperty("keybinds.price_check", "Alt+D")), priceListener);
        handler.addListener(new KeyCombo(PropertiesManager.getProperty("keybinds.search_trade", "Alt+Q")), priceListener);
        GlobalScreen.addNativeMouseListener(priceListener);
    }
}
