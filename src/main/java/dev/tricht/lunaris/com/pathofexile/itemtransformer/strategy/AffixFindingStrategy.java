package dev.tricht.lunaris.com.pathofexile.itemtransformer.strategy;

import dev.tricht.lunaris.com.pathofexile.response.Affix;

import java.util.Map;

public interface AffixFindingStrategy {
    public Affix find(String affixText, Map<String, Affix> affixIdList);
}
