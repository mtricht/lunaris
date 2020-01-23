package dev.tricht.lunaris.com.pathofexile.middleware;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.com.pathofexile.request.StatFilter;
import dev.tricht.lunaris.com.pathofexile.request.DoubleValue;
import dev.tricht.lunaris.item.Item;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class PseudoModsMiddleware implements TradeMiddleware {

    private static List<PseudoMod> pseudoMods = null;

    public PseudoModsMiddleware() {
        if (pseudoMods != null) {
            return;
        }

        JsonFactory f = new JsonFactory();
        f.enable(JsonParser.Feature.ALLOW_COMMENTS);
        ObjectMapper mapper = new ObjectMapper(f);
        try {
            pseudoMods = mapper.readValue(PseudoModsMiddleware.class.getResourceAsStream("/data/pseudo_mods.json"), new TypeReference<List<PseudoMod>>() {
            });
        } catch (IOException e) {
            log.error("Failed to load pseudo_mods.json", e);
        }
    }

    @Override
    public void handle(Item item, Query query) {
        if (query.getStats().size() < 1) {
            return;
        }

        while (replaceMods(query)) ;
    }

    private boolean replaceMods(Query query) {

        List<StatFilter> statFilters = query.getStats().get(0).getFilters();

        Map<String, StatFilter> pseudoStatFilters = new HashMap<>();
        List<String> filtersToRemove = new ArrayList<>();

        for (StatFilter filter : statFilters) {
            for (PseudoMod pseudoMod : pseudoMods) {
                if (pseudoMod.getInclude() == null) {
                    continue;
                }

                if (pseudoMod.getInclude().contains(filter.getId())) {
                    if (!pseudoStatFilters.containsKey(pseudoMod.getId())) {
                        StatFilter pseudoFilter = new StatFilter();
                        pseudoFilter.setId(pseudoMod.getId());

                        DoubleValue val = new DoubleValue();
                        val.setMin(0.0);
                        pseudoFilter.setDoubleValue(val);
                        pseudoStatFilters.put(pseudoMod.getId(), pseudoFilter);
                    }

                    log.debug("Got pseudo (" + pseudoMod.getId() + "), combining with " + filter.getId() + " (val:" + filter.getDoubleValue() + ")");
                    StatFilter pseudoStat = pseudoStatFilters.get(pseudoMod.getId());
                    pseudoStat.getDoubleValue().setMin(pseudoStat.getDoubleValue().getMin() + filter.getDoubleValue().getMin());
                    filtersToRemove.add(filter.getId());
                }
            }
        }

        if (pseudoStatFilters.size() < 1) {
            return false;
        }

        List<StatFilter> newStatFilters = new ArrayList<>();
        for (StatFilter filter : statFilters) {
            if (filtersToRemove.contains(filter.getId())) {
                continue;
            }
            newStatFilters.add(filter);
        }
        newStatFilters.addAll(pseudoStatFilters.values());
        query.getStats().get(0).setFilters(newStatFilters);
        return true;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PseudoMod {
        private String id;
        private ArrayList<String> include;
    }
}
