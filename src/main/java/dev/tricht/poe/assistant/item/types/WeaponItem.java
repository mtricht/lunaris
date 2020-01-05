package dev.tricht.poe.assistant.item.types;

public class WeaponItem implements ItemType {
    private WeaponType type;

    public WeaponItem(WeaponType type) {
        this.type = type;
    }

    public WeaponType getType() {
        return this.type;
    }
}
