package dev.tricht.lunaris.com.pathofexile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.PropertiesManager;
import dev.tricht.lunaris.com.pathofexile.request.*;
import dev.tricht.lunaris.com.pathofexile.response.*;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class PathOfExileAPI {

    private OkHttpClient client;
    private ObjectMapper objectMapper;
    private Map<String, Map<String, Affix>> knownAffixes = new HashMap<>();
    private Map<String, Affix> explicitAffixes = new HashMap<>();
    private final Pattern digitPattern = Pattern.compile("(-?[0-9]+)");
    private final Pattern modTypePattern = Pattern.compile("\\s\\((implicit|crafted)\\)");
    @Getter
    @Setter
    private String league;
    private CookieManager cookieManager;
    private String sessionId;

    public PathOfExileAPI() {
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        this.objectMapper = new ObjectMapper();
        getStats();
        String sessionid = PropertiesManager.getProperty(PropertiesManager.POESESSID);
        if (sessionid != null) {
            setSessionId(sessionid);
        }
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

    public List<String> getTradeLeagues() {
        return getLeagues().stream().filter(s -> !s.contains("SSF")).collect(Collectors.toList());
    }

    public List<String> getLeagues() {
        log.debug("Getting leagues from pathofexile.com");
        Request request = new Request.Builder()
                .url("http://api.pathofexile.com/leagues")
                .build();
        Response response;
        try {
            response = client.newCall(request).execute();
            List<League> leagues = objectMapper.readValue(response.body().string(), new TypeReference<List<League>>(){});
            return leagues.stream().map(League::getId).collect(Collectors.toList());
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
            throw new RuntimeException("Failed to get leagues", e);
        }
        for (AffixGroup affixGroup : statsResponse.getAffixGroup()) {
            if (!affixGroup.getLabel().toLowerCase().matches("crafted|implicit|explicit")) {
                continue;
            }
            Map<String, Affix> affixGroupMap = new HashMap<>();
            for (Affix affixResponse : affixGroup.getAffixResponses()) {
                if (affixGroupMap.containsKey(affixResponse.getText()) && affixGroupMap.get(affixResponse.getText()).getIdLong() > affixResponse.getIdLong()) {
                    continue;
                }
                affixGroupMap.put(affixResponse.getText(), affixResponse);
            }
            // TODO: Fractured, Delve, Monster, Pseudo, Enchant and Veiled
            if (affixGroup.getLabel().matches("Crafted|Implicit")) {
                knownAffixes.put(affixGroup.getLabel().toLowerCase(), affixGroupMap);
            } else if (affixGroup.getLabel().equals("Explicit")) {
                explicitAffixes = affixGroupMap;
            }
        }
    }

    public void find(Item item, Callback callback) {
        TradeRequest tradeRequest = new TradeRequest();
        Query query = new Query();
        tradeRequest.setQuery(query);
        populateQuery(item, query);
        search(tradeRequest, callback);
    }

    private void populateQuery(Item item, Query query) {
        if (item.getRarity() == ItemRarity.UNIQUE) {
            query.setName(item.getName());
            query.setType(item.getBase());
        }
        if (item.getType() instanceof UnknownItem) {
            // What is this?
            return;
        }
        if (item.getType() instanceof CurrencyItem) {
            query.setTerm(item.getBase());
            return;
        }
        if (item.getType() instanceof DivinitationCardItem || item.getType() instanceof MapItem
            || item.getType() instanceof FragmentItem || item.getType() instanceof ScarabItem) {
            if (query.getName() == null) {
                query.setTerm(item.getBase());
            }
            return;
        }

        if (item.getType() instanceof GemItem && query.getName() == null) {
            query.setTerm(item.getBase());
        }

        setStatFilters(item, query);
        setMiscFilters(item, query);
        setRequirementFilters(item, query);
    }

    private void setStatFilters(Item item, Query query) {
        Stat stat = new Stat();
        query.getStats().add(stat);
        List<StatFilter> statFilters = new ArrayList<>();
        for (String affix : item.getAffixes()) {
            Matcher affixWithoutDigitsMatcher = digitPattern.matcher(affix);
            Integer minimal = null;
            if (affixWithoutDigitsMatcher.find()) {
                if (affixWithoutDigitsMatcher.groupCount() == 1) {
                    minimal = Integer.parseInt(affixWithoutDigitsMatcher.group(1));
                } else if (affixWithoutDigitsMatcher.groupCount() == 2) {
                    minimal = (Integer.parseInt(affixWithoutDigitsMatcher.group(1))
                            + Integer.parseInt(affixWithoutDigitsMatcher.group(2))) / 2;
                }
            }
            String affixWithoutDigits = affixWithoutDigitsMatcher.replaceAll("#");
            Matcher affixModTypeMatcher = modTypePattern.matcher(affixWithoutDigits);
            String modType = null;
            if (affixModTypeMatcher.find()) {
                modType = affixModTypeMatcher.group(1);
            }
            String affixWithoutModType = affixModTypeMatcher.replaceAll("");
            String foundAffix = findAffix(modType, affixWithoutModType);
            if (foundAffix == null) {
                foundAffix = findAffix(modType, affixWithoutModType.replaceAll("[+\\-]", ""));
            }
            if (foundAffix == null) {
                foundAffix = findAffix(modType, affixWithoutModType + " (Local)");
            }
            if (foundAffix == null) {
                foundAffix = findAffix(modType, affixWithoutModType.replaceAll("[+\\-]", "") + " (Local)");
            }
            if (foundAffix != null) {
                StatFilter statFilter = new StatFilter();
                statFilter.setId(foundAffix);
                if (minimal != null) {
                    Value value = new Value();
                    value.setMin(minimal);
                    statFilter.setValue(value);
                }
                log.debug(String.format("Affix %s found as %s", affix, foundAffix));
                statFilters.add(statFilter);
            } else {
                log.debug(String.format("Affix %s not found", affix));
            }
        }
        stat.setFilters(statFilters);
    }

    private void setMiscFilters(Item item, Query query) {
        Filters filters = new Filters();
        query.setFilters(filters);
        Filters.NestedFilters miscFilters = new Filters.NestedFilters();
        filters.setNestedFilters(miscFilters);
        Filters.DeeperFilters deeperMiscFilters = new Filters.DeeperFilters();
        miscFilters.setFilters(deeperMiscFilters);

        // We never want to see corrupted items, unless our own item is also corrupted
        Option corrupted = new Option();
        corrupted.setOption(false);
        if (item.getProps().isCorrupted()) {
            corrupted.setOption(true);
        }
        deeperMiscFilters.setCorrupted(corrupted);

        // Don't check ilvl on uniques since it's very situational
        if (item.getProps().getItemLevel() != 1 && item.getRarity() != ItemRarity.UNIQUE) {
            Value ilvl = new Value();
            ilvl.setMin(item.getProps().getItemLevel());
            deeperMiscFilters.setIlvl(ilvl);
        }

        // We never want to see mirrored items, unless our item is also mirrored
        Option mirrored = new Option();
        mirrored.setOption(false);
        if (item.getProps().isMirrored()) {
            mirrored.setOption(true);
        }
        deeperMiscFilters.setMirrored(mirrored);

        // Only quality on gems is expensive (some amulets/rings too, but we narrow our results too much by checking for that)
        // However if the quality is above >20, it might be worth something (hillock bench)
        if (item.getProps().getQuality() > 20 || item.getType() instanceof GemItem) {
            Value quality = new Value();
            deeperMiscFilters.setQuality(quality);
            quality.setMin(item.getProps().getQuality());
        }

        if (item.getType() instanceof GemItem) {
            Value gemLevel = new Value();
            gemLevel.setMin(((GemItem) item.getType()).getLevel());
            deeperMiscFilters.setGemLevel(gemLevel);
        }

        if (item.getProps().getLinks() >= 5) {
            Filters.NestedFilters socketFilters = new Filters.NestedFilters();
            filters.setSocketFilters(socketFilters);
            Filters.DeeperFilters socketDeeperFilter = new Filters.DeeperFilters();
            Value links = new Value();
            links.setMin(item.getProps().getLinks());
            socketDeeperFilter.setLinks(links);
            socketFilters.setFilters(socketDeeperFilter);
        }
    }

    private void setRequirementFilters(Item item, Query query) {
        Filters.NestedFilters reqFilters = new Filters.NestedFilters();
        query.getFilters().setReqFilters(reqFilters);
        Filters.DeeperFilters deeperReqFilters = new Filters.DeeperFilters();
        reqFilters.setFilters(deeperReqFilters);
        // Dex, str and int include the gem requirements.
    }

    private String findAffix(String modType, String affix) {
        if (modType != null) {
            if (knownAffixes.get(modType).containsKey(affix)) {
                return knownAffixes.get(modType).get(affix).getId();
            }
        }
        if (explicitAffixes.containsKey(affix)) {
            return explicitAffixes.get(affix).getId();
        }
        return null;
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
                .url("https://www.pathofexile.com/api/trade/search/" + league)
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
            throw new RateLimitMostLikelyException("Failed to get item listings", e);
        }
    }

}
