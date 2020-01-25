package dev.tricht.lunaris.item.types;

import lombok.Data;

@Data
public class WeaponItem implements ItemType, HasItemLevel {
    private WeaponType type;

    private double physDPS;
    private double elemDPS;
    private double chaosDPS;

    private double atkSpeed;
    private double physMin, physMax, fireMin, fireMax, coldMin, coldMax, lightningMin, lightningMax, chaosMin, chaosMax;


    public WeaponItem(){}
    public WeaponItem(WeaponType type) {
        this.type = type;
    }

    public WeaponType getType() {
        return this.type;
    }
    public void setType(WeaponType weaponType){this.type = weaponType;}

    public double getTotalDPS() { return physDPS+elemDPS+chaosDPS;}

    public void calcTotalDPS(){
        physDPS = (physMax+physMin)/2*atkSpeed;
        elemDPS = (fireMin+fireMax)/2*atkSpeed + (coldMin+coldMax)/2*atkSpeed + (lightningMin+lightningMax)/2*atkSpeed;
        chaosDPS = (chaosMin+chaosMax)/2*atkSpeed;
    }

}
