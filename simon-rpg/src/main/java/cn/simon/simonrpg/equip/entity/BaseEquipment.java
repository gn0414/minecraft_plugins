package cn.simon.simonrpg.equip.entity;




import java.util.LinkedHashMap;
import java.util.List;


public abstract class BaseEquipment {

    private String name;

    private String type;

    private String category;

    private String soulBind;

    private String level;

    private LinkedHashMap<String,String> mainAttribute;

    private LinkedHashMap<String,String> bonusAttribute;

    private List<String> gem;

    private List<String> enchant;

    private List<String> rune;


    public void setRune(List<String> rune) {
        this.rune = rune;
    }

    public void setGem(List<String> gem) {
        this.gem = gem;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMainAttribute(LinkedHashMap<String, String> mainAttribute) {
        this.mainAttribute = mainAttribute;
    }

    public void setBonusAttribute(LinkedHashMap<String, String> bonusAttribute) {
        this.bonusAttribute = bonusAttribute;
    }


    public void setLevel(String level) {
        this.level = level;
    }

    public void setCategory(String category) {
        this.category = category;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setEnchant(List<String> enchant) {
        this.enchant = enchant;
    }

    public void setSoulBind(String soulBind) {
        this.soulBind = soulBind;
    }

    public List<String> getRune() {
        return rune;
    }

    public List<String> getGem() {
        return gem;
    }

    public String getType() {
        return type;
    }

    public String getLevel() {
        return level;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", category='" + category + '\'' +
                ", soulBind='" + soulBind + '\'' +
                ", level='" + level + '\'' +
                ", mainAttribute=" + mainAttribute +
                ", bonusAttribute=" + bonusAttribute +
                ", gem=" + gem +
                ", enchant=" + enchant +
                ", rune=" + rune +
                '}';
    }


    public LinkedHashMap<String, String> getBonusAttribute() {
        return bonusAttribute;
    }

    public LinkedHashMap<String, String> getMainAttribute() {
        return mainAttribute;
    }

    public String getSoulBind() {
        return soulBind;
    }

    public List<String> getEnchant() {
        return enchant;
    }

}
