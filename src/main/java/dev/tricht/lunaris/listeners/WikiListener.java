package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.WindowsAPI;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemGrabber;
import lombok.extern.slf4j.Slf4j;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
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
                if (item == null) {
                    return;
                }
                URI uri = URI.create(
                        "https://pathofexile.gamepedia.com/"
                        + URLEncoder.encode(
                                item.getBase().replace(" ", "_"),
                                StandardCharsets.UTF_8.toString()
                        )
                );
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + uri);
            } catch (Exception e) {
                log.error("Failed to browse to wiki", e);
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
