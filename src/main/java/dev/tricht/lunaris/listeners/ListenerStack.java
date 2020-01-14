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

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

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
        ArrayList<KeyCombo> combos = new ArrayList<>();
        for(Map.Entry<String, String> property : PropertiesManager.getAllPropertiesMatching("keybinds(.*)").entrySet()) {
            combos.add(new KeyCombo(property.getValue()));
        }
        handler.setRespondTo(combos);

        ItemInfoListener infoListener = new ItemInfoListener(new KeyCombo(PropertiesManager.getProperty("keybinds.item_info", "Alt+A")));
        infoListener.addInfoListener(new MapInfoListener());
        infoListener.addInfoListener(new CurrencyStackListener());

        ClipboardListenerStack clipboardListenerStack = new ClipboardListenerStack(itemGrabber, robot);
        clipboardListenerStack.addListener(infoListener);
        clipboardListenerStack.addListener(new WikiListener(new KeyCombo(PropertiesManager.getProperty("keybinds.wiki", "Alt+W"))));

        priceListener = new ItemPriceListener(
                new KeyCombo(PropertiesManager.getProperty("keybinds.price_check", "Alt+D")),
                new KeyCombo(PropertiesManager.getProperty("keybinds.search_trade", "Alt+Q")),
                pathOfExileAPI
        );
        clipboardListenerStack.addListener(priceListener);
        if (priceListener != null) {
            GlobalScreen.removeNativeMouseListener(priceListener);
        }
        GlobalScreen.addNativeMouseListener(priceListener);


        handler.addListener(new HideoutListener(new KeyCombo(PropertiesManager.getProperty("keybinds.hideout", "F5")), robot));
        handler.addListener(new KickSelfListener(new KeyCombo(PropertiesManager.getProperty("keybinds.kick", "F4")), robot));
        handler.addListener(new MouseScrollCombo(NativeInputEvent.CTRL_L_MASK), new StashScrollListener(robot));
        handler.addListener(clipboardListenerStack);


    }
}
