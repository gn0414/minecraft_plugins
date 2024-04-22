package cn.simon.simonrpg.equip.entity;

import java.util.List;

public class WeaponSouvenir extends Weapon{

    private List<String> description;

    private String CustomizeColor;

    public void setCustomizeColor(String customColor) {
        CustomizeColor = customColor;
    }

    public String getCustomizeColor() {
        return CustomizeColor;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public List<String> getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return super.toString()+getDescription();
    }
}
