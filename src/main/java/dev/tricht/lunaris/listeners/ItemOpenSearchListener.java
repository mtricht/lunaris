package dev.tricht.lunaris.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.WindowsAPI;
import dev.tricht.lunaris.com.pathofexile.NotYetImplementedException;
import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.com.pathofexile.RateLimitMostLikelyException;
import dev.tricht.lunaris.com.pathofexile.response.ListingResponse;
import dev.tricht.lunaris.com.pathofexile.response.SearchResponse;
import dev.tricht.lunaris.elements.Label;
import dev.tricht.lunaris.elements.*;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemGrabber;
import dev.tricht.lunaris.tooltip.TooltipCreator;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.mouse.NativeMouseEvent;
import org.ocpsoft.prettytime.PrettyTime;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class ItemOpenSearchListener implements GameListener {

    private ItemGrabber itemGrabber;
    private PathOfExileAPI pathOfExileAPI;
    private ObjectMapper objectMapper;

    public ItemOpenSearchListener(ItemGrabber itemGrabber, PathOfExileAPI pathOfExileAPI) {
        this.itemGrabber = itemGrabber;
        this.pathOfExileAPI = pathOfExileAPI;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onEvent(GameEvent event) {
        try {
            TooltipCreator.hide();
            log.debug("pathofexile.com/trade");
            Item item = this.itemGrabber.grab();
            if (item == null || !item.hasPrice()) {
                log.debug("No item selected.");
                return;
            }
            log.debug("Got item, translating to pathofexile.com");
            try {
                this.pathOfExileAPI.find(item, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        log.debug("Failed to search", e);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        try {
                            SearchResponse searchResponse = objectMapper.readValue(response.body().string(), SearchResponse.class);
                            if (searchResponse != null && searchResponse.getId() != null) {
                                WindowsAPI.browse(searchResponse.getUrl(pathOfExileAPI.getLeague()));
                            }
                            log.debug(searchResponse.toString());
                        } catch (IOException e) {
                            log.debug("Failed to parse response", e);
                        }
                    }
                });
            } catch (NotYetImplementedException e) {
                log.error("Item not yet implemented", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
