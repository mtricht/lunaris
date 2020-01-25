package dev.tricht.lunaris.info.poeprices;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.item.Item;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Base64;

@Slf4j
public class PoePricesAPI {

    private static OkHttpClient client;
    private static ObjectMapper objectMapper;
    @Setter
    private String leagueName;

    public PoePricesAPI(String leagueName) {
        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
        this.leagueName = leagueName;
    }

    public ItemPricePrediction getItem(Item item) {
        log.debug(item.toString());
        String itemEncoded = Base64.getEncoder().encodeToString(item.getClipboardText().getBytes());
        Request request = new Request.Builder().url(
                String.format("https://www.poeprices.info/api?l=%s&i=%s", leagueName, itemEncoded)
        ).build();
        try {
            Response response = client.newCall(request).execute();
            return objectMapper.readValue(response.body().string(), ItemPricePrediction.class);
        } catch (IOException e) {
            log.error("Failed to call poeprices.info", e);
            return null;
        }
    }
}
