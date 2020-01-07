package dev.tricht.lunaris.item;

import lombok.Data;

@Data
public class ItemProps {
    private boolean isCorrupted = false;
    private boolean isMirrored = false;
    private boolean isIdentified = true;
    private int itemLevel = 1;
    private ItemInfluence influence = ItemInfluence.NONE;
    private String note;

    public boolean isInfluenced() {
        return influence != ItemInfluence.NONE;
    }
}
