package cn.simon.simonrpg.equip.entity;


import java.util.LinkedHashMap;
import java.util.List;

public class Weapon extends BaseEquipment{

    private LinkedHashMap<String,String> damageType;


    private List<String> skill;



    public void setSkill(List<String> skill) {
        this.skill = skill;
    }

    public List<String> getSkill() {
        return skill;
    }

    public void setDamageType(LinkedHashMap<String, String> damageType) {
        this.damageType = damageType;
    }

    public LinkedHashMap<String, String> getDamageType() {
        return damageType;
    }

    @Override
    public String toString() {
        return super.toString()+"{" +
                "damageType=" + damageType +
                ", skill='" + skill + '\'' +
                '}';
    }
}
