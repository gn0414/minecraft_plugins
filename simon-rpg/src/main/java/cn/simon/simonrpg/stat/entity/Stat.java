package cn.simon.simonrpg.stat.entity;

import java.util.HashMap;
import java.util.Map;

public class Stat {
    private String physicalDefense;

    private String fireDefense;

    private String waterDefense;

    private String poisonDefense;

    private String windDefense;

    private String magicDefense;

    private String pvpDefense;

    private String pvpDamage;

    private String pveDefense;

    private String pveDamage;

    private String physicalDamage;

    private String missChance;

    private String blockChance;

    private String damageReduce;

    private String armorPenetration;

    private String criticalChance;

    private String lifeSteal;

    private Map<String,Integer> runes;

    @Override
    public String toString() {
        return "Stat{" +
                "physicalDefense='" + physicalDefense + '\'' +
                ", fireDefense='" + fireDefense + '\'' +
                ", waterDefense='" + waterDefense + '\'' +
                ", poisonDefense='" + poisonDefense + '\'' +
                ", windDefense='" + windDefense + '\'' +
                ", magicDefense='" + magicDefense + '\'' +
                ", pvpDefense='" + pvpDefense + '\'' +
                ", pvpDamage='" + pvpDamage + '\'' +
                ", pveDefense='" + pveDefense + '\'' +
                ", pveDamage='" + pveDamage + '\'' +
                ", physicalDamage='" + physicalDamage + '\'' +
                ", missChance='" + missChance + '\'' +
                ", blockChance='" + blockChance + '\'' +
                ", damageReduce='" + damageReduce + '\'' +
                ", armorPenetration='" + armorPenetration + '\'' +
                ", criticalChance='" + criticalChance + '\'' +
                ", lifeSteal='" + lifeSteal + '\'' +
                ", runes=" + runes +
                '}';
    }

    public Stat(){
        this.physicalDefense = "0.0";
        this.fireDefense = "0.0";
        this.poisonDefense = "0.0";
        this.windDefense = "0.0";
        this.magicDefense = "0.0";
        this.waterDefense = "0.0";
        this.pveDamage = "0.0";
        this.pveDefense = "0.0";
        this.pvpDamage = "0.0";
        this.pvpDefense = "0.0";
        this.missChance = "0.0";
        this.blockChance = "0.0";
        this.armorPenetration = "0.0";
        this.criticalChance = "0.0";
        this.damageReduce = "0.0";
        this.physicalDamage = "0.0";
        this.lifeSteal = "0.0";
        this.runes = new HashMap<>();
    }

    public void setRunes(Map<String, Integer> runes) {
        this.runes = runes;
    }

    public Map<String, Integer> getRunes() {
        return runes;
    }

    public String getLifeSteal() {
        return lifeSteal;
    }

    public String getCriticalChance() {
        return criticalChance;
    }

    public String getArmorPenetration() {
        return armorPenetration;
    }

    public String getBlockChance() {
        return blockChance;
    }

    public String getDamageReduce() {
        return damageReduce;
    }

    public String getFireDefense() {
        return fireDefense;
    }

    public String getMagicDefense() {
        return magicDefense;
    }

    public String getMissChance() {
        return missChance;
    }

    public String getPhysicalDamage() {
        return physicalDamage;
    }

    public String getPhysicalDefense() {
        return physicalDefense;
    }

    public String getPoisonDefense() {
        return poisonDefense;

    }

    public void setLifeSteal(String lifeSteal) {
        this.lifeSteal = lifeSteal;
    }

    public void setCriticalChance(String criticalChance) {
        this.criticalChance = criticalChance;
    }

    public String getPveDamage() {
        return pveDamage;
    }

    public String getPveDefense() {
        return pveDefense;
    }

    public String getPvpDamage() {
        return pvpDamage;
    }

    public String getPvpDefense() {
        return pvpDefense;
    }

    public String getWaterDefense() {
        return waterDefense;
    }

    public String getWindDefense() {
        return windDefense;
    }

    public void setArmorPenetration(String armorPenetration) {
        this.armorPenetration = armorPenetration;
    }

    public void setBlockChance(String blockChance) {
        this.blockChance = blockChance;
    }

    public void setDamageReduce(String damageReduce) {
        this.damageReduce = damageReduce;
    }

    public void setFireDefense(String fireDefense) {
        this.fireDefense = fireDefense;
    }

    public void setMagicDefense(String magicDefense) {
        this.magicDefense = magicDefense;
    }

    public void setMissChance(String missChance) {
        this.missChance = missChance;
    }

    public void setPhysicalDamage(String physicalDamage) {
        this.physicalDamage = physicalDamage;
    }

    public void setPhysicalDefense(String physicalDefense) {
        this.physicalDefense = physicalDefense;
    }

    public void setPoisonDefense(String poisonDefense) {
        this.poisonDefense = poisonDefense;
    }

    public void setPveDamage(String pveDamage) {
        this.pveDamage = pveDamage;
    }

    public void setPveDefense(String pveDefense) {
        this.pveDefense = pveDefense;
    }

    public void setPvpDamage(String pvpDamage) {
        this.pvpDamage = pvpDamage;
    }

    public void setPvpDefense(String pvpDefense) {
        this.pvpDefense = pvpDefense;
    }

    public void setWaterDefense(String waterDefense) {
        this.waterDefense = waterDefense;
    }

    public void setWindDefense(String windDefense) {
        this.windDefense = windDefense;
    }


}
