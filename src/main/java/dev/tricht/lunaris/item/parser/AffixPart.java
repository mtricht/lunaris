package dev.tricht.lunaris.item.parser;

import dev.tricht.lunaris.item.LocalAffixResolver;
import dev.tricht.lunaris.item.types.ItemType;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AffixPart {

    private ArrayList<String> affixes;
    private ItemType itemType;

    public AffixPart(ArrayList<String> affixes, ItemType itemType) {
        this.affixes = affixes;
        this.itemType = itemType;
    }

    public ArrayList<Affix> getAffixes() {
        ArrayList<Affix> affixList = new ArrayList<>();
        for(String affix : affixes) {
            affixList.add(new Affix(LocalAffixResolver.resolve(affix, itemType)));
        }

        return affixList;
    }

    public static class Affix {
        private String affixText;
        private boolean isCrafted;
        private boolean isFractured;
        private boolean isLocal;

        public Affix(String affixText) {
            this.affixText = affixText;
            this.isCrafted = affixText.contains("(crafted)");
            this.isFractured = affixText.contains("(fractured)");
            this.isLocal = affixText.contains("(Local)");
        }

        public boolean isCrafted() {
            return isCrafted;
        }

        public boolean isFractured() {
            return isFractured;
        }

        public boolean isLocal() { return isLocal; }

        public String getText() {
            return affixText;
        }
    }
}
