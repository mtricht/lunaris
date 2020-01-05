package dev.tricht.poe.assistant.item;

import lombok.Data;

import java.awt.*;

@Data
public class Item {
    private Point mousePosition;
    private String name;
    private Integer meanPrice;
    private String iconUrl;

    public String toString() {
        if (meanPrice == null) {
            return String.format("Item '%s' does not exist on poe.ninja\nTODO: open browser tab to pathofexile.com?", name);
        }
        return String.format("%s\nChaos equivalent: %d\nSource: poe.ninja", name, meanPrice);
    }
}
