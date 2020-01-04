package dev.tricht.poe.assistant;

import lombok.Data;

@Data
public class Item {
    private int id;
    private String name;
    private String rarity;
    private int meanPrice;
    public String toString() {
        return String.format("ID: %s\nName: %s\nRarity: %s\nMean price: %d chaos", id, name, rarity, meanPrice);
    }
}
