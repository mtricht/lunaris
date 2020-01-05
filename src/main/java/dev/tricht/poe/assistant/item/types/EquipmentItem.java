package dev.tricht.poe.assistant.item.types;

public class EquipmentItem implements ItemType, HasItemLevel {
    private EquipmentSlot slot;

    public EquipmentItem(EquipmentSlot slot) {
        this.slot = slot;
    }

    public EquipmentSlot getSlot() {
        return slot;
    }
}
