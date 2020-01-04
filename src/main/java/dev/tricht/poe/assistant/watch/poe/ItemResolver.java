package dev.tricht.poe.assistant.watch.poe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemResolver {

    private OkHttpClient client;
    private Map<String, Integer> items = new HashMap<>();
    private Map<Integer, Integer> prices = new HashMap<>();

    public ItemResolver() throws IOException {
        this.client = new OkHttpClient();
        getItems();
        getPrices();
    }

    private void getItems() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.poe.watch/itemdata")
                .build();
        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RuntimeException("No data from https://api.poe.watch/itemdata");
            }
            List<Item> items = objectMapper.readValue(responseBody.string(), new TypeReference<List<Item>>(){});
            for (Item item : items) {
                this.items.put(item.getName(), item.getId());
            }
        }
    }

    private void getPrices() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.poe.watch/compact?league=Metamorph")
                .build();
        try (Response response = client.newCall(request).execute()) {
            ObjectMapper objectMapper = new ObjectMapper();
            ResponseBody responseBody = response.body();
            if (responseBody == null) {
                throw new RuntimeException("No data from https://api.poe.watch/compact?league=Metamorph");
            }
            List<ItemPrice> prices = objectMapper.readValue(responseBody.string(), new TypeReference<List<ItemPrice>>(){});
            for (ItemPrice price : prices) {
                this.prices.put(price.getId(), (int) price.getMean());
            }
        }
    }

    public Integer resolve(String item) {
        return items.get(item);
    }

    public Integer appraise(Integer id) {
        return prices.get(id);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Item {
        private String name;
        private int id;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ItemPrice {
        private int id;
        private double mean;
    }
}
