package dev.tricht.lunaris.com.pathofexile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.com.pathofexile.itemtransformer.ItemTransformer;
import dev.tricht.lunaris.util.Properties;
import dev.tricht.lunaris.com.pathofexile.request.*;
import dev.tricht.lunaris.com.pathofexile.response.*;
import dev.tricht.lunaris.item.Item;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class PathOfExileAPI {

    private OkHttpClient client;
    private ObjectMapper objectMapper;

    @Getter
    private static Map<String, Affix> implicits = new HashMap<>();
    @Getter
    private static Map<String, Affix> enchants = new HashMap<>();

    @Getter
    private static Map<String, Affix> craftedAffixes = new HashMap<>();

    @Getter
    private static Map<String, Affix> fracturedAffixes = new HashMap<>();

    @Getter
    private static Map<String, Affix> baseAffixes = new HashMap<>();

    private CookieManager cookieManager;
    private String sessionId;

    private static List<String> leagueCache = null;

    public PathOfExileAPI() {
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        this.objectMapper = new ObjectMapper();
        getStats();
        String sessionid = Properties.INSTANCE.getProperty(Properties.POESESSID);
        if (sessionid != null) {
            setSessionId(sessionid);
        }
        getLeagues();
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
        HttpCookie cookie = new HttpCookie("POESESSID", sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setDomain(".pathofexile.com");
        cookieManager.getCookieStore().add(URI.create("https://pathofexile.com"), cookie);
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public static List<String> getTradeLeagues() {
        return leagueCache;
    }

    public List<String> getLeagues() {
        if (leagueCache != null) {
            return leagueCache;
        }

        log.debug("Getting leagues from pathofexile.com");
        Request request = new Request.Builder()
                .url("http://api.pathofexile.com/leagues")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            List<League> leagues = objectMapper.readValue(response.body().string(), new TypeReference<List<League>>() {
            });
            leagueCache = leagues.stream().map(League::getId)
                    .filter(s -> !s.contains("SSF"))
                    .collect(Collectors.toList());
            return leagueCache;
        } catch (IOException e) {
            throw new RuntimeException("Failed to get leagues", e);
        }
    }

    private void getStats() {
        Request request = new Request.Builder()
                .url("http://api.pathofexile.com/trade/data/stats")
                .build();
        StatsResponse statsResponse;
        try {
            Response response = client.newCall(request).execute();
            statsResponse = objectMapper.readValue(response.body().string(), StatsResponse.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to get stats", e);
        }
        for (AffixGroup affixGroup : statsResponse.getAffixGroup()) {
            Map<String, Affix> affixGroupMap = new HashMap<>();
            for (Affix affixResponse : affixGroup.getAffixResponses()) {
                if (affixGroupMap.containsKey(affixResponse.getText()) && affixGroupMap.get(affixResponse.getText()).getIdLong() > affixResponse.getIdLong()) {
                    continue;
                }
                affixGroupMap.put(affixResponse.getText(), affixResponse);
            }

            if (affixGroup.getLabel().matches("Implicit")) {
                implicits = affixGroupMap;
            }
            if (affixGroup.getLabel().matches("Enchant")) {
                enchants = affixGroupMap;
            }

            // TODO: Veiled? Item text only gives "Veiled Prefix", "Veiled Suffix", probably not useful

            // TODO: Pseudo
            if (affixGroup.getLabel().matches("Crafted")) {
                log.debug("Saving crafted mods");
                craftedAffixes = affixGroupMap;
            }
            if (affixGroup.getLabel().equals("Explicit")) {
                log.debug("Saving explicit mods");
                baseAffixes = affixGroupMap;
            }
            if (affixGroup.getLabel().matches("Fractured")) {
                log.debug("Saving fractured mods");
                fracturedAffixes = affixGroupMap;
            }
        }
    }

    public void find(Item item, Callback callback) {
        TradeRequest tradeRequest = new TradeRequest();
        Query query = ItemTransformer.createQuery(item);

        tradeRequest.setQuery(query);
        search(tradeRequest, callback);
    }

    private void search(TradeRequest tradeRequest, Callback callback) {
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(tradeRequest);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize trade request", e);
            return;
        }
        Request request = new Request.Builder()
                .url("https://www.pathofexile.com/api/trade/search/" + Properties.getLeague())
                .post(RequestBody.create(MediaType.parse("application/json"), requestBody.getBytes()))
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public List<ListingResponse.Item> getItemListings(SearchResponse searchResponse) {
        String ids = String.join(",", searchResponse.getResult().subList(
                0,
                (Math.min(searchResponse.getTotal(), 10))
        ));
        Request request = new Request.Builder()
                .url("https://www.pathofexile.com/api/trade/fetch/" + ids + "?query=" + searchResponse.getId())
                .build();
        try {
            Response response = client.newCall(request).execute();
            ListingResponse itemListings = objectMapper.readValue(response.body().string(), ListingResponse.class);
            return itemListings.getResult();
        } catch (IOException e) {
            throw new RateLimitMostLikelyException("Failed to get item listings");
        }
    }

}
