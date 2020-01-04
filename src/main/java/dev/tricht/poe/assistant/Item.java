package dev.tricht.poe.assistant;

import lombok.Data;

@Data
public class Item {
    private String name;
    private Integer meanPrice;
    public String toString() {
        if (meanPrice == null) {
            return String.format("Item '%s' does not exist on poe.ninja\nTODO: open browser tab to pathofexile.com?", name);
        }
        return String.format("%s\nChaos equivalent: %d\nSource: poe.ninja", name, meanPrice);
    }
}
