package dev.tricht.lunaris.item.parser;

import dev.tricht.lunaris.item.ItemInfluence;
import dev.tricht.lunaris.item.ItemProps;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ItemPropsParts {

    private ArrayList<ArrayList<String>> parts;

    private Pattern notePattern = Pattern.compile("Note:(.*)");
    private Pattern itemLevelPattern = Pattern.compile("Item Level: (.*)");
    private Pattern qualityPattern = Pattern.compile("Quality: \\+([0-9]+).*");
    private Pattern dexPattern = Pattern.compile("Dex: ([0-9]+).*");
    private Pattern intPattern = Pattern.compile("Int: ([0-9]+).*");
    private Pattern strPattern = Pattern.compile("Str: ([0-9]+).*");

    public ItemPropsParts(ArrayList<ArrayList<String>> parts) {
        this.parts = parts;
    }

    public ItemProps getProps() {
        ItemProps props = new ItemProps();

        //TODO: Items can have multiple influence types
        for (ArrayList<String> part : parts) {
            for (String line : part) {
                if (line.equals("Corrupted")) {
                    props.setCorrupted(true);
                    continue;
                }
                if (line.equals("Mirrored")) {
                    props.setMirrored(true);
                    continue;
                }
                if (line.equals("Unidentified")) {
                    props.setIdentified(false);
                    continue;
                }
                if (line.equals("Elder Item")) {
                    props.setInfluence(ItemInfluence.ELDER);
                    continue;
                }
                if (line.equals("Shaper Item")) {
                    props.setInfluence(ItemInfluence.SHAPER);
                    continue;
                }
                if (line.equals("Synthesised Item")) {
                    props.setInfluence(ItemInfluence.SYNTHESISED);
                    continue;
                }
                if (line.equals("Fractured Item")) {
                    props.setInfluence(ItemInfluence.FRACTURED);
                    continue;
                }
                if (line.equals("Crusader Item")) {
                    props.setInfluence(ItemInfluence.CRUSADER);
                    continue;
                }
                if (line.equals("Hunter Item")) {
                    props.setInfluence(ItemInfluence.HUNTER);
                    continue;
                }
                if (line.equals("Warlord Item")) {
                    props.setInfluence(ItemInfluence.WARLORD);
                    continue;
                }
                if (line.equals("Redeemer Item")) {
                    props.setInfluence(ItemInfluence.REDEEMER);
                    continue;
                }
                if (notePattern.matcher(line).matches()) {
                    props.setNote(line);
                    continue;
                }
                Matcher strMatcher = strPattern.matcher(line);
                if (strMatcher.matches()) {
                    props.setStrength(Integer.parseInt(strMatcher.group(1)));
                    continue;
                }
                Matcher dexMatcher = dexPattern.matcher(line);
                if (dexMatcher.matches()) {
                    props.setDexterity(Integer.parseInt(dexMatcher.group(1)));
                    continue;
                }
                Matcher intMatcher = intPattern.matcher(line);
                if (intMatcher.matches()) {
                    props.setIntelligence(Integer.parseInt(intMatcher.group(1)));
                    continue;
                }
                Matcher qualityMatcher = qualityPattern.matcher(line);
                if (qualityMatcher.matches()) {
                    props.setQuality(Integer.parseInt(qualityMatcher.group(1)));
                    continue;
                }
                if (itemLevelPattern.matcher(line).matches()) {
                    props.setItemLevel(Integer.parseInt(line.replace("Item Level: ", "")));
                    continue;
                }
            }
        }
        return props;
    }
}
