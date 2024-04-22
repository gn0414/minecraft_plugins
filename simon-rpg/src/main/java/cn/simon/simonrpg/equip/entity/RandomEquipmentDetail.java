package cn.simon.simonrpg.equip.entity;


import java.util.List;
import java.util.Map;

public class RandomEquipmentDetail {
    //武器类型+概率
    private Map<String,String> type;

    //武器类型+名称列表
    private Map<String, List<String>> name;

    //等级范围
    private String level;

    //主词条数量概率
    private Map<String,List<String>> main;
    //副词条数量概率
    private Map<String,List<String>>  bonus;
    //主属性出现概率
    private Map<String,List<String>>  mainAttribute;

    private Map<String,List<String>> bonusAttribute;

    private Map<String,List<String>>  mainAttributeValue;

    private Map<String,List<String>> bonusAttributeValue;

    //gem出现范围
    private String gem;

    private String enchant;

    private String rune;

    private String skill;


    public String getSkill() {
        return skill;
    }

    public String getLevel() {
        return level;
    }



    public Map<String, List<String>> getName() {
        return name;
    }

    public String getEnchant() {
        return enchant;
    }

    public String getGem() {
        return gem;
    }

    public String getRune() {
        return rune;
    }






    public void setMain(Map<String, List<String>> main) {
        this.main = main;
    }

    public void setBonus(Map<String, List<String>> bonus) {
        this.bonus = bonus;
    }

    public Map<String, List<String>> getBonus() {
        return bonus;
    }

    public Map<String, List<String>> getMain() {
        return main;
    }


    public void setMainAttributeValue(Map<String, List<String>> mainAttributeValue) {
        this.mainAttributeValue = mainAttributeValue;
    }

    public void setBonusAttributeValue(Map<String, List<String>> bonusAttributeValue) {
        this.bonusAttributeValue = bonusAttributeValue;
    }

    public void setBonusAttribute(Map<String, List<String>> bonusAttribute) {
        this.bonusAttribute = bonusAttribute;
    }

    public void setMainAttribute(Map<String, List<String>> mainAttribute) {
        this.mainAttribute = mainAttribute;
    }

    public Map<String, List<String>> getBonusAttribute() {
        return bonusAttribute;
    }

    public Map<String, List<String>> getBonusAttributeValue() {
        return bonusAttributeValue;
    }

    public Map<String, List<String>> getMainAttribute() {
        return mainAttribute;
    }

    public Map<String, List<String>> getMainAttributeValue() {
        return mainAttributeValue;
    }

    public Map<String, String> getType() {
        return type;
    }

    public void setName(Map<String, List<String>> name) {
        this.name = name;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }



    public void setLevel(String level) {
        this.level = level;
    }



    public void setGem(String gem) {
        this.gem = gem;
    }

    public void setRune(String rune) {
        this.rune = rune;
    }

    public void setEnchant(String enchant) {
        this.enchant = enchant;
    }



    public void setType(Map<String, String> type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RandomEquipmentDetail{" +
                "type=" + type +
                ", name=" + name +
                ", level='" + level + '\'' +
                ", main=" + main +
                ", bonus=" + bonus +
                ", mainAttribute=" + mainAttribute +
                ", bonusAttribute=" + bonusAttribute +
                ", mainAttributeValue=" + mainAttributeValue +
                ", bonusAttributeValue=" + bonusAttributeValue +
                ", gem='" + gem + '\'' +
                ", enchant='" + enchant + '\'' +
                ", rune='" + rune + '\'' +
                ", skill='" + skill + '\'' +
                '}';
    }
}
