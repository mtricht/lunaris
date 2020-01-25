package dev.tricht.lunaris.com.pathofexile.itemtransformer.strategy;

import dev.tricht.lunaris.com.pathofexile.response.Affix;

import java.util.Map;

public class ExactMatchStrategy implements AffixFindingStrategy {
    @Override
    public Affix find(String affixText, Map<String, Affix> affixIdList) {
        if (!affixIdList.containsKey(affixText)) {
            return null;
        }
        return affixIdList.get(affixText);
    }
}
