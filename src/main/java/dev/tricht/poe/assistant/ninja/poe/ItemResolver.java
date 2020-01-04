package dev.tricht.poe.assistant.ninja.poe;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemResolver {

    File dataDirectory = new File(System.getenv("APPDATA") + "\\PoEAssistant\\data");
    Map<String, Integer> prices = new HashMap<>();

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
        System.out.println("Loading " + file);
        if (!file.exists()) {
            // TODO: Retry downloading?
            throw new RuntimeException(String.format("File %s does not exist", file.getAbsolutePath()));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Root root;
        try {
            root = objectMapper.readValue(file, Root.class);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Unable to parse %s", file.getAbsolutePath()));
        }
        for (Item item : root.getItems()) {
            prices.put(item.getName(), item.getPrice());
        }
    }

    public Integer appraise(String name) {
        return prices.get(name);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Root {
        @JsonProperty("lines")
        private List<Item> items;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Item {
        @JsonAlias({"currencyTypeName", "name"})
        private String name;
        @JsonAlias({"chaosEquivalent", "chaosValue"})
        private int price;
    }

}
