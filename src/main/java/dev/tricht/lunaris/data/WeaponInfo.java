package dev.tricht.lunaris.data;

import lombok.Data;

@Data
public class WeaponInfo {
    private float atkSpeed;
    private int physMin, physMax, fireMin, fireMax, coldMin, coldMax, lightningMin, lightningMax, chaosMin, chaosMax;

    public float calcTotalDPS(){
        return calcPhysDPS()+calcElemDPS()+calcChaosDPS();
    }
    public float calcPhysDPS(){
        return (physMax+physMin)/2*atkSpeed;
    }
    public float calcChaosDPS(){
        return (chaosMin+chaosMax)/2*atkSpeed;
    }
    public float calcElemDPS(){
        return calcFireDPS()+calcColdDPS()+calcLightningDPS();
    }
    public float calcFireDPS(){
        return (fireMin+fireMax)/2*atkSpeed;
    }
    public float calcColdDPS(){
        return (coldMin+coldMax)/2*atkSpeed;
    }
    public float calcLightningDPS(){
        return (lightningMin+lightningMax)/2*atkSpeed;
    }

}
