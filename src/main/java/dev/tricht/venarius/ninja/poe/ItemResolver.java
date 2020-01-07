package dev.tricht.venarius.ninja.poe;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.venarius.data.DataDirectory;
import dev.tricht.venarius.item.Item;
import dev.tricht.venarius.item.ItemRarity;
import dev.tricht.venarius.item.types.HasItemLevel;
import dev.tricht.venarius.item.types.MapItem;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ItemResolver {

    private static OkHttpClient client;
    private Map<String, ArrayList<RemoteItem>> items;
    private static final int HOUR_IN_MILLI = 60 * 60 * 1000;

    public ItemResolver(String leagueName) {
        client = new OkHttpClient();
        refresh(leagueName);
    }

    public void refresh(String leagueName) {
        items = new HashMap<>();
        File leagueDirectory = getLeagueDataDirectory(leagueName);
        downloadFiles(leagueDirectory, leagueName);
        loadFiles(leagueDirectory);
    }

    private static void downloadFiles(File dataDirectory, String leagueName) {
        File lastUpdatedFile = new File(dataDirectory.getAbsolutePath() + "\\.last-updated");
        if (lastUpdatedFile.exists() && (System.currentTimeMillis() - lastUpdatedFile.lastModified()) < HOUR_IN_MILLI) {
            return;
        }
        for (String type : Types.currencyTypes) {
            downloadJson("https://poe.ninja/api/data/currencyoverview", type, dataDirectory, leagueName);
        }
        for (String type : Types.itemTypes) {
            downloadJson("https://poe.ninja/api/data/itemoverview", type, dataDirectory, leagueName);
        }
        try {
            lastUpdatedFile.createNewFile();
            lastUpdatedFile.setLastModified(System.currentTimeMillis());
        } catch (IOException e) {
            log.error("Failed saving data from poe.ninja", e);
        }
    }

    @SneakyThrows
    private static void downloadJson(String baseUrl, String type, File dataDirectory, String leagueName) {
        log.debug("Downloading " + type);
        Request request = new Request.Builder()
                .url(String.format("%s?type=%s&league=%s", baseUrl, type, leagueName))
                .build();
        Response response;
        response = client.newCall(request).execute();
        FileOutputStream output = new FileOutputStream(dataDirectory + "\\" + type + ".json");
        output.write(response.body().bytes());
        output.close();
    }

    private File getLeagueDataDirectory(String leagueName) {
        return DataDirectory.getDirectory("poe-ninja\\" + leagueName);
    }

    private void loadFiles(File leagueDirectory) {
        for (String type : Types.currencyTypes) {
            loadFile(new File(leagueDirectory.getAbsolutePath() + "\\" + type + ".json"));
        }
        for (String type : Types.itemTypes) {
            loadFile(new File(leagueDirectory.getAbsolutePath() + "\\" + type + ".json"));
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

}
