package dev.tricht.lunaris.listeners;

import dev.tricht.lunaris.util.Platform;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.item.ItemRarity;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class WikiListener implements GameListener {

    private ItemGrabber itemGrabber;

    public WikiListener(ItemGrabber itemGrabber) {
        this.itemGrabber = itemGrabber;
    }

    @Override
    public void onEvent(GameEvent event) {
        try {
            Item item = this.itemGrabber.grab();
            if (item == null) {
                return;
            }
            String url = "https://pathofexile.gamepedia.com/" + URLEncoder.encode(
                    (item.getRarity() == ItemRarity.UNIQUE ? item.getName() : item.getBase()).replace(" ", "_"),
                    StandardCharsets.UTF_8.toString()
            );
            Platform.browse(url);
        } catch (Exception e) {
            log.error("Failed to browse to wiki", e);
        }
    }
}
