package dev.tricht.lunaris.item.parser;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AffixPart {


    private ArrayList<String> affixes;

    public AffixPart(ArrayList<String> affixes) {
        this.affixes = affixes;
    }


    public ArrayList<Affix> getAffixes() {
        ArrayList<Affix> affixList = new ArrayList<>();
        for(String affix : affixes) {
            affixList.add(new Affix(affix));
        }

        return affixList;
    }

    public static class Affix {
        private String affixText;
        public Affix(String affixText){
            this.affixText = affixText;
        }

        public boolean isCrafted() {
            return affixText.contains("(crafted)");
        }

        public boolean isFractured() {
            return affixText.contains("(fractured)");
        }

        public String getText() {
            return affixText;
        }
    }
}
