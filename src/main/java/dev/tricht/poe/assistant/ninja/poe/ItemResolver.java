package dev.tricht.poe.assistant.ninja.poe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.poe.assistant.item.Item;
import dev.tricht.poe.assistant.item.ItemInfluence;
import dev.tricht.poe.assistant.item.types.EquipmentItem;
import dev.tricht.poe.assistant.item.types.MapItem;
import dev.tricht.poe.assistant.item.types.WeaponItem;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ItemResolver {

    File dataDirectory = new File(System.getenv("APPDATA") + "\\PoEAssistant\\data");
    Map<String, ArrayList<RemoteItem>> items = new HashMap<>();

    public ItemResolver() {
        loadFiles();
    }

    private void loadFiles() {
        for (String type : Types.currencyTypes) {
            loadFile(new File(dataDirectory + "\\" + type + ".json"));
        }
        for (String type : Types.itemTypes)
            loadFile(new File(dataDirectory + "\\" + type + ".json"));
        {
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
        for (RemoteItem item : root.getItems()) {
            if (!items.containsKey(item.getName())) {
                items.put(item.getName(), new ArrayList<>());
            }
            items.get(item.getName()).add(item);
        }

        if (root.getCurrencyDetails() != null) {
            for (CurrencyDetail currencyDetail : root.getCurrencyDetails()) {
                if (items.containsKey(currencyDetail.getName())) {
                    for (RemoteItem item : items.get(currencyDetail.getName())) {
                        item.setIconUrl(currencyDetail.getIconUrl());
                    }
                }
            }
        }
    }

    public boolean hasItem(Item item) {
        return items.containsKey(item.getBase());
    }

    public Integer appraise(Item item) {
        return getItem(item).getPrice();
    }

    public RemoteItem getItem(Item item) {
        ArrayList<RemoteItem> remoteItemList = items.get(item.getBase());
        if (remoteItemList.size() == 1) {
            return remoteItemList.get(0);
        }

        if (item.getType() instanceof MapItem) {
            for (RemoteItem remoteItem : remoteItemList) {
                if (remoteItem.getMapTier() == ((MapItem) item.getType()).getTier()) {
                    return remoteItem;
                }
            }
        }

        if (item.getType() instanceof EquipmentItem) {
            for (RemoteItem remoteItem : remoteItemList) {
                if (remoteItem.getItemLevel() == Math.min(86, Math.max(82, ((EquipmentItem) item.getType()).getItemLevel()))) {
                    if(remoteItem.getInfluence() == null && item.getProps().getInfluence() == ItemInfluence.NONE) {
                        return remoteItem;
                    }

                    if(remoteItem.getInfluence().toLowerCase().equals(item.getProps().getInfluence().name().toLowerCase())) {
                        return remoteItem;
                    }
                }
            }
        }

        return remoteItemList.get(0);
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Root {
        @JsonProperty("lines")
        private List<RemoteItem> items;
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
