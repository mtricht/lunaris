package dev.tricht.poe.assistant.listeners;

import dev.tricht.poe.assistant.WindowsAPI;
import dev.tricht.poe.assistant.item.Item;
import dev.tricht.poe.assistant.item.ItemGrabber;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class WikiListener implements NativeKeyListener {

    private ItemGrabber itemGrabber;

    public WikiListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent event) {
        if (WindowsAPI.isPoeActive() && event.getKeyCode() == NativeKeyEvent.VC_W && event.getModifiers() == NativeInputEvent.ALT_L_MASK) {
            try {
                Item item = this.itemGrabber.grab();
                URI uri = URI.create(
                        "https://pathofexile.gamepedia.com/"
                        + URLEncoder.encode(
                                item.getName().replace(" ", "_"),
                                StandardCharsets.UTF_8.toString()
                        )
                );
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + uri);
            } catch (IOException | UnsupportedFlavorException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent event) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent event) {
    }
}
