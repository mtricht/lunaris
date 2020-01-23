package dev.tricht.lunaris.com.pathofexile.itemtransformer.strategy;

import java.util.ArrayList;

public class AffixFindingStrategies {

    private static ArrayList<AffixFindingStrategy> strategies = null;

    public static ArrayList<AffixFindingStrategy> all() {
        if (strategies == null) {
            strategies = new ArrayList<>();
            strategies.add(new ExactMatchStrategy());
            strategies.add(new ReplaceNumbersStrategy());
        }

        return strategies;
    }
}
