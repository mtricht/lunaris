package dev.tricht.poe.assistant.item;

import lombok.Data;

@Data
public class ItemProps {
    private boolean isCorrupted = false;
    private boolean isMirrored = false;
    private boolean isIdentified = true;
    private ItemInfluence influence = null;
    private String note;

    public boolean isInfluenced() {
        return influence != null;
    }
}
