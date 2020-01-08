package dev.tricht.lunaris.com.pathofexile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.com.pathofexile.request.*;
import dev.tricht.lunaris.com.pathofexile.response.*;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.ItemRarity;
import dev.tricht.lunaris.item.types.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
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
    @Setter
    private String league;
    private CookieManager cookieManager;

    public PathOfExileAPI() {
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.client = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();
        this.objectMapper = new ObjectMapper();
        getStats();
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

    public SearchResponse find(Item item) {
        TradeRequest tradeRequest = new TradeRequest();
        Query query = new Query();
        tradeRequest.setQuery(query);
        populateQuery(item, tradeRequest, query);
        return search(tradeRequest);
    }

    private void populateQuery(Item item, TradeRequest tradeRequest, Query query) {
        if (item.getRarity() == ItemRarity.UNIQUE) {
            query.setName(item.getName());
            query.setType(item.getBase());
        }
        if (item.getType() instanceof UnknownItem) {
            // What is this?
            return;
        }
        if (item.getType() instanceof CurrencyItem) {
            // TODO bulk exchange?
            throw new NotYetImplementedException("Not implemented yet");
        }
        if (item.getType() instanceof DivinitationCardItem || item.getType() instanceof MapItem
            || item.getType() instanceof FragmentItem || item.getType() instanceof ScarabItem) {
            if (query.getName() == null) {
                query.setTerm(item.getBase());
            }
            return;
        }
        if (item.getType() instanceof GemItem) {
            // TODO not supported yet by parser
            throw new NotYetImplementedException("Not implemented yet");
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
            if (affixWithoutDigitsMatcher.find() && affixWithoutDigitsMatcher.groupCount() == 1) {
                minimal = Integer.parseInt(affixWithoutDigitsMatcher.group(1));
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

        if (item.getProps().isCorrupted()) {
            Option corrupted = new Option();
            deeperMiscFilters.setCorrupted(corrupted);
            corrupted.setOption(true);
        }
        if (item.getProps().getItemLevel() != 1) {
            Value ilvl = new Value();
            ilvl.setMin(item.getProps().getItemLevel());
            deeperMiscFilters.setIlvl(ilvl);
        }
        if (item.getProps().isMirrored()) {
            Option mirrored = new Option();
            deeperMiscFilters.setMirrored(mirrored);
            mirrored.setOption(true);
        }
        if (item.getProps().getQuality() > 0 && item.getProps().getQuality() <= 20) {
            Value quality = new Value();
            deeperMiscFilters.setQuality(quality);
            quality.setMin(item.getProps().getQuality());
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

    private SearchResponse search(TradeRequest tradeRequest) {
        String requestBody;
        try {
            requestBody = objectMapper.writeValueAsString(tradeRequest);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize trade request", e);
            return null;
        }
        Request request = new Request.Builder()
                .url("https://www.pathofexile.com/api/trade/search/" + league)
                .post(RequestBody.create(MediaType.parse("application/json"), requestBody.getBytes()))
                .build();
        try {
            Response response = client.newCall(request).execute();
            SearchResponse searchResponse = objectMapper.readValue(response.body().string(), SearchResponse.class);
            searchResponse.setLeague(league);
            return searchResponse;
        } catch (IOException e) {
            throw new RateLimitMostLikelyException("Failed to search", e);
        }
    }

    public List<ListingResponse.Item> getItemListings(SearchResponse searchResponse) {
        log.debug(searchResponse.getId());
        String ids = String.join(",", searchResponse.getResult().subList(
                0,
                (searchResponse.getTotal() < 10 ? (searchResponse.getTotal() - 1) : 9)
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
