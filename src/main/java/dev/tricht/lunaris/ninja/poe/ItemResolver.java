package dev.tricht.lunaris.ninja.poe;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.util.DirectoryManager;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.*;
import dev.tricht.lunaris.util.Properties;
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

    public ItemResolver() {
        client = new OkHttpClient();
        refresh();
    }

    public void refresh() {
        items = new HashMap<>();
        File leagueDirectory = getLeagueDataDirectory(Properties.getLeague());
        downloadFiles(leagueDirectory, Properties.getLeague());
        loadFiles(leagueDirectory);
    }

    private static void downloadFiles(File dataDirectory, String leagueName) {
        File lastUpdatedFile = new File(dataDirectory.getAbsolutePath() + File.separator + ".last-updated");
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
        Response response = client.newCall(request).execute();
        FileOutputStream output = new FileOutputStream(dataDirectory + File.separator + type + ".json");
        output.write(response.body().bytes());
        output.close();
    }

    private File getLeagueDataDirectory(String leagueName) {
        return DirectoryManager.INSTANCE.getDataDirectory("poe-ninja" + File.separator + leagueName);
    }

    private void loadFiles(File leagueDirectory) {
        for (String type : Types.currencyTypes) {
            loadFile(new File(leagueDirectory.getAbsolutePath() + File.separator + type + ".json"));
        }
        for (String type : Types.itemTypes) {
            loadFile(new File(leagueDirectory.getAbsolutePath() + File.separator + type + ".json"));
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

        if (item.getRarity() == ItemRarity.UNIQUE && item.getProps().getLinks() > 0) {
            RemoteItem zeroLinksItem = null;
            for (RemoteItem remoteItem : remoteItemList) {
               if (remoteItem.getLinks() == item.getProps().getLinks()) {
                   return remoteItem;
               }
               if (remoteItem.getLinks() == 0) {
                   zeroLinksItem = remoteItem;
               }
            }
            if (zeroLinksItem != null) {
                return zeroLinksItem;
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

        if (item.getType() instanceof GemItem) {
            RemoteItem chosenRemoteItem = null;
            GemItem gemItem = (GemItem) item.getType();

            // Match closest level
            int chosenGemLevel = 9999;
            for (RemoteItem remoteItem : remoteItemList) {
                int gemLvlDiff = Math.abs(remoteItem.getGemLevel() - gemItem.getLevel());

                if (gemLvlDiff < Math.abs(chosenGemLevel - gemItem.getLevel())) {
                    chosenGemLevel = remoteItem.getGemLevel();
                }
            }

            // Match closest quality
            int chosenGemQuality = 9999;
            for (RemoteItem remoteItem : remoteItemList) {
                if (remoteItem.getGemLevel() != chosenGemLevel) {
                    continue;
                }

                int gemQualDiff = Math.abs(remoteItem.getGemQuality() - item.getProps().getQuality());
                if (gemQualDiff < Math.abs(chosenGemQuality - item.getProps().getQuality())) {
                    chosenGemQuality = remoteItem.getGemQuality();
                }
            }

            // Match corruption
            for (RemoteItem remoteItem : remoteItemList) {
                if ((remoteItem.getGemLevel() != chosenGemLevel) || (remoteItem.getGemQuality() != chosenGemQuality)) {
                    continue;
                }
                if (chosenRemoteItem == null) {
                    chosenRemoteItem = remoteItem;
                }
                if (item.getProps().isCorrupted() == remoteItem.isCorrupted()) {
                    chosenRemoteItem = remoteItem;
                }
            }

            if (chosenRemoteItem != null) {
                boolean isExactMatch = (chosenRemoteItem.getGemQuality() == item.getProps().getQuality())
                        && (chosenRemoteItem.getGemLevel() == gemItem.getLevel()) && (chosenRemoteItem.isCorrupted() == item.getProps().isCorrupted());
                chosenRemoteItem.setReason(String.format(
                        "%sgem level %s, %s quality%s",
                        !isExactMatch ? "closest to " : "",
                        chosenRemoteItem.getGemLevel(), chosenRemoteItem.getGemQuality(), chosenRemoteItem.isCorrupted() ? ", corrupted" : ""
                ));

                return chosenRemoteItem;
            }
        }

        return remoteItemList.get(0);
    }

}
