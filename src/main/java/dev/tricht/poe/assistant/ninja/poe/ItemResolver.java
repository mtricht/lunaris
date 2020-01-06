package dev.tricht.poe.assistant.ninja.poe;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.poe.assistant.item.Item;
import dev.tricht.poe.assistant.item.ItemInfluence;
import dev.tricht.poe.assistant.item.ItemRarity;
import dev.tricht.poe.assistant.item.types.EquipmentItem;
import dev.tricht.poe.assistant.item.types.HasItemLevel;
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
        for (String type : Types.itemTypes) {
            loadFile(new File(dataDirectory + "\\" + type + ".json"));
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
            log.error(String.format("Unable to parse %s", file.getAbsolutePath()), e);
            return;
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
        String itemName = item.getRarity() == ItemRarity.UNIQUE ? item.getName() : item.getBase();
        log.debug(itemName);
        return items.containsKey(itemName);
    }

    public Price appraise(RemoteItem item) {
        Price price = new Price();
        price.setPrice(item.getPrice());

        if (item.isLowConfidence()) {
            price.setLowConfidence(true);
        }

        if (item.getReason() != null) {
            price.setReason(item.getReason());
        }

        return price;
    }

    public RemoteItem getItem(Item item) {

        String itemName = item.getRarity() == ItemRarity.UNIQUE ? item.getName() : item.getBase();

        ArrayList<RemoteItem> remoteItemList = items.get(itemName);

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

        if (item.getType() instanceof HasItemLevel && item.getRarity() != ItemRarity.UNIQUE) {
            RemoteItem chosenRemoteItem = null;

            for (RemoteItem remoteItem : remoteItemList) {
                int ilvlDiff = Math.abs(remoteItem.getItemLevel() - item.getProps().getItemLevel());
                String remoteItemInfluence = "none";
                if (remoteItem.getInfluence() != null) {
                    remoteItemInfluence = remoteItem.getInfluence().toLowerCase();
                }

                if (remoteItemInfluence.equals(item.getProps().getInfluence().name().toLowerCase())) {
                    if (chosenRemoteItem == null) {
                        chosenRemoteItem = remoteItem;
                    }

                    if (ilvlDiff < Math.abs(chosenRemoteItem.getItemLevel() - item.getProps().getItemLevel())) {
                        chosenRemoteItem = remoteItem;
                    }
                }
            }

            if (chosenRemoteItem != null) {
                boolean isExactlySameIlvl = chosenRemoteItem.getItemLevel() == item.getProps().getItemLevel();

                chosenRemoteItem.setReason(String.format(
                        "%silvl %s %s",
                        !isExactlySameIlvl ? "closest to " : "",
                        chosenRemoteItem.getItemLevel(), chosenRemoteItem.getInfluence() != null
                                ? ", " + chosenRemoteItem.getInfluence().toLowerCase() + " base"
                                : ""
                ));

                return chosenRemoteItem;
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
