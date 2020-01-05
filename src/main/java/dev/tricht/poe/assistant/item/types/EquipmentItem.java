package dev.tricht.poe.assistant.item.types;

public class EquipmentItem implements ItemType {

    private EquipmentSlot slot;
    private int itemLevel;

    public EquipmentItem(EquipmentSlot slot) {
        this.slot = slot;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    public int getItemLevel() {
        return itemLevel;
    }
}
