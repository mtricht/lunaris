package dev.tricht.lunaris.com.pathofexile.itemtransformer.strategy;

import dev.tricht.lunaris.com.pathofexile.response.Affix;
import lombok.Data;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceNumbersStrategy implements AffixFindingStrategy {

    private static final Pattern digitPattern = Pattern.compile("(-?\\d+\\.?\\d*)");

    @Override
    public Affix find(String affixText, Map<String, Affix> affixIdList) {
        NumericAffix numericAffix = getNumericAffix(affixText);
        if (affixIdList.containsKey(numericAffix.getAffixName())) {
            Affix apiAffix = affixIdList.get(numericAffix.getAffixName());
            apiAffix.setValue(numericAffix.getVal());
            return apiAffix;
        }

        if (affixIdList.containsKey(numericAffix.getAffixNameWithoutSigns())) {
            Affix apiAffix = affixIdList.get(numericAffix.getAffixNameWithoutSigns());
            apiAffix.setValue(numericAffix.getVal());
            return apiAffix;
        }

        return null;
    }

    private static NumericAffix getNumericAffix(String affixText) {
        Matcher affixWithoutDigitsMatcher = digitPattern.matcher(affixText);

        Double value = null;

        ArrayList<Double> digits = new ArrayList<Double>();
        while (affixWithoutDigitsMatcher.find()) {
            digits.add(Double.parseDouble(affixWithoutDigitsMatcher.group(1)));
        }

        if (digits.size() > 1) {
            value = (digits.get(0) + digits.get(1)) / 2;
        } else if (digits.size() > 0) {
            value = digits.get(0);
        }


        String affixWithoutDigits = affixWithoutDigitsMatcher.replaceAll("#");

        return new NumericAffix(affixWithoutDigits, value);
    }


    @Data
    private static class NumericAffix {
        private String affixName;
        private Double val;

        public NumericAffix(String affixName, Double val) {
            this.affixName = affixName;
            this.val = val;
        }

        private String getAffixNameWithoutSigns() {
            return affixName.replaceAll("[+\\-]", "");
        }
    }
}
