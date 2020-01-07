package dev.tricht.venarius.item.types;

import lombok.Getter;
import lombok.Setter;

public class MapItem implements ItemType {

    @Getter
    @Setter
    private int tier;

    public MapItem() {

    }
}
