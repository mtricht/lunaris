package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.info.poeprices.PoePricesAPI;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.util.Properties;
import kotlin.Unit;
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

    public void startListeners(ItemGrabber itemGrabber, Robot robot, PathOfExileAPI pathOfExileAPI, PoePricesAPI poePricesAPI) {
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

        Properties.INSTANCE.addPropertyListener("keybinds(.*)", (String) -> {
            log.debug("Restarting listeners after keybind change");
            handler.removeListeners();
            if (priceListener != null) {
                GlobalScreen.removeNativeMouseListener(priceListener);
            }

            startListeners(handler, itemGrabber, robot, pathOfExileAPI, poePricesAPI);
            return Unit.INSTANCE;
        });

        startListeners(handler, itemGrabber, robot, pathOfExileAPI, poePricesAPI);
    }

    private void startListeners(HotKeyHandler handler, ItemGrabber itemGrabber, Robot robot, PathOfExileAPI pathOfExileAPI,
                                PoePricesAPI poePricesAPI) {
        ArrayList<KeyCombo> combos = new ArrayList<>();
        for(Map.Entry<String, String> property : Properties.INSTANCE.getAllPropertiesMatching("keybinds(.*)").entrySet()) {
            combos.add(new KeyCombo(property.getValue()));
        }
        handler.setRespondTo(combos);

        ItemInfoListener infoListener = new ItemInfoListener(new KeyCombo(Properties.INSTANCE.getProperty("keybinds.item_info")));
        infoListener.addInfoListener(new MapInfoListener());
        infoListener.addInfoListener(new CurrencyStackListener());
        infoListener.addInfoListener(new WeaponInfoListener());

        ClipboardListenerStack clipboardListenerStack = new ClipboardListenerStack(itemGrabber, robot);
        clipboardListenerStack.addListener(infoListener);
        clipboardListenerStack.addListener(new WikiListener(new KeyCombo(Properties.INSTANCE.getProperty("keybinds.wiki"))));

        priceListener = new ItemPriceListener(
                new KeyCombo(Properties.INSTANCE.getProperty("keybinds.price_check")),
                new KeyCombo(Properties.INSTANCE.getProperty("keybinds.search_trade")),
                pathOfExileAPI,
                poePricesAPI
        );
        clipboardListenerStack.addListener(priceListener);
        if (priceListener != null) {
            GlobalScreen.removeNativeMouseListener(priceListener);
        }
        GlobalScreen.addNativeMouseListener(priceListener);


        handler.addListener(new HideoutListener(new KeyCombo(Properties.INSTANCE.getProperty("keybinds.hideout" )), robot));
        handler.addListener(new KickSelfListener(new KeyCombo(Properties.INSTANCE.getProperty("keybinds.kick")), robot));
        handler.addListener(new InviteLastWhisperListener(new KeyCombo(Properties.INSTANCE.getProperty("keybinds.invite_last_whisper")), robot));

        if(Properties.INSTANCE.getProperty("keybinds.enable_stash_scroll", "1").equals("1")) {
            handler.addListener(new MouseScrollCombo(NativeInputEvent.CTRL_L_MASK), new StashScrollListener(robot));
        }

        handler.addListener(clipboardListenerStack);
    }
}
