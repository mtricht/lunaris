package dev.tricht.venarius.item.parser;

import dev.tricht.venarius.item.ItemInfluence;
import dev.tricht.venarius.item.ItemProps;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ItemPropsParts {

    private ArrayList<ArrayList<String>> parts;

    private Pattern notePattern = Pattern.compile("Note:(.*)");

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
                if (line.matches("Item Level: (.*)")) {
                    props.setItemLevel(Integer.parseInt(line.replace("Item Level: ", "")));
                    continue;
                }
            }
        }
        return props;
    }
}
