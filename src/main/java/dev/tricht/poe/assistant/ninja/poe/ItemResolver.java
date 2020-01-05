package dev.tricht.poe.assistant.ninja.poe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ItemResolver {

    File dataDirectory = new File(System.getenv("APPDATA") + "\\PoEAssistant\\data");
    Map<String, Item> items = new HashMap<>();

    public ItemResolver() {
        loadFiles();
    }

    private void loadFiles() {
        for (String type : Types.currencyTypes) {
            loadFile(new File(dataDirectory + "\\" + type + ".json"));
        }
        for (String type : Types.itemTypes)
            loadFile(new File(dataDirectory + "\\" + type + ".json"));{
        }
    }

    private void loadFile(File file) {
        log.debug("Loading " + file);
        if (!file.exists()) {
            throw new RuntimeException(String.format("File %s does not exist", file.getAbsolutePath()));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Root root;
        try {
            root = objectMapper.readValue(file, Root.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to parse %s", file.getAbsolutePath()), e);
        }
        for (Item item : root.getItems()) {
            items.put(item.getName(), item);
        }
        if (root.getCurrencyDetails() != null) {
            for (CurrencyDetail currencyDetail : root.getCurrencyDetails()) {
                if (items.containsKey(currencyDetail.getName())) {
                    items.get(currencyDetail.getName()).setIconUrl(currencyDetail.getIconUrl());
                }
            }
        }
    }

    public boolean hasItem(String name) {
        return items.containsKey(name);
    }

    public Integer appraise(String name) {
        return items.get(name).getPrice();
    }

    public Item getItem(String name) { return items.get(name); }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Root {
        @JsonProperty("lines")
        private List<Item> items;
        @JsonProperty("currencyDetails")
        private List<CurrencyDetail> currencyDetails;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CurrencyDetail {
        @JsonProperty("name")
        private String name;
        @JsonProperty("icon")
        private String iconUrl;
    }
}
