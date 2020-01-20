package dev.tricht.lunaris.com.pathofexile.itemtransformer;

import dev.tricht.lunaris.com.pathofexile.PathOfExileAPI;
import dev.tricht.lunaris.com.pathofexile.itemtransformer.strategy.AffixFindingStrategies;
import dev.tricht.lunaris.com.pathofexile.itemtransformer.strategy.AffixFindingStrategy;
import dev.tricht.lunaris.com.pathofexile.request.Query;
import dev.tricht.lunaris.com.pathofexile.request.Stat;
import dev.tricht.lunaris.com.pathofexile.request.StatFilter;
import dev.tricht.lunaris.com.pathofexile.request.Value;
import dev.tricht.lunaris.com.pathofexile.response.Affix;
import dev.tricht.lunaris.item.Item;
import dev.tricht.lunaris.item.parser.AffixPart;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class StatFilterSetter {


    public static void set(Item item, Query query) {
        Stat stat = new Stat();
        query.getStats().add(stat);

        List<StatFilter> statFilters = new ArrayList<>();

        setImplicits(item, statFilters);
        setExplicits(item, statFilters);

        stat.setFilters(statFilters);
    }

    private static void setImplicits(Item item, List<StatFilter> statFilters) {
        for (String implicit : item.getImplicits()) {
            Affix apiAffix = findAffix(implicit, PathOfExileAPI.getImplicits());
            if (apiAffix != null) {
                statFilters.add(createStatFilter(apiAffix.getId(), apiAffix.getValue()));
                continue;
            }

            apiAffix = findAffix(implicit, PathOfExileAPI.getEnchants());
            if (apiAffix != null) {
                statFilters.add(createStatFilter(apiAffix.getId(), apiAffix.getValue()));
            }
        }
    }

    private static void setExplicits(Item item, List<StatFilter> statFilters) {
        ArrayList<AffixPart.Affix> itemAffixes = item.getAffixes();
        ArrayList<Affix> apiAffixes = new ArrayList<>();

        for (AffixPart.Affix affix : itemAffixes) {
            if (affix.isCrafted()) {
                if (item.hasLocalMods() && addTo(findAffix(affix.getText().replace(" (crafted)", "") + " (Local)", PathOfExileAPI.getCraftedAffixes()), apiAffixes)) {
                    continue;
                }
                if (addTo(findAffix(affix.getText().replace(" (crafted)", ""), PathOfExileAPI.getCraftedAffixes()), apiAffixes)) {
                   continue;
                }
            }

            if (affix.isFractured()) {
                if (item.hasLocalMods() && addTo(findAffix(affix.getText().replace(" (fractured)", "") + " (Local)", PathOfExileAPI.getFracturedAffixes()), apiAffixes)) {
                    continue;
                }
                if (addTo(findAffix(affix.getText().replace(" (fractured)", ""), PathOfExileAPI.getFracturedAffixes()), apiAffixes)) {
                    continue;
                }
            }

            if (item.hasLocalMods() && addTo(findAffix(affix.getText() + " (Local)", PathOfExileAPI.getBaseAffixes()), apiAffixes)) {
                continue;
            }

            if (!addTo(findAffix(affix.getText(), PathOfExileAPI.getBaseAffixes()), apiAffixes)) {
                log.debug("Can't find affix with text " + affix.getText());
            }
        }

        for (Affix apiAffix : apiAffixes) {
            statFilters.add(createStatFilter(apiAffix.getId(), apiAffix.getValue()));
        }
    }

    private static boolean addTo(Affix affix, ArrayList<Affix> apiAffixes) {
        if (affix != null) {
            apiAffixes.add(affix);
            return true;
        }
        return false;
    }

    private static Affix findAffix(String affixText, Map<String, Affix> apiAffixList) {
        for (AffixFindingStrategy strategy : AffixFindingStrategies.all()) {
            Affix affix = strategy.find(affixText, apiAffixList);
            if (affix != null) {
                return affix;
            }
        }
        return null;
    }

    private static StatFilter createStatFilter(String apiStatId, Double apiStatValue) {
        StatFilter statFilter = new StatFilter();
        statFilter.setId(apiStatId);
        if (apiStatValue != null) {
            Value value = new Value();
            value.setMin(apiStatValue);
            statFilter.setValue(value);
        }
        log.debug(String.format("Affix found as %s", apiStatId));
        return statFilter;
    }
}
