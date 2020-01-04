package dev.tricht.poe.assistant;

import lombok.Data;

@Data
public class Item {
    private String name;
    private int meanPrice;
    public String toString() {
        return String.format("%s\nChaos equivalent: %d\nSource: poe.ninja", name, meanPrice);
    }
}
