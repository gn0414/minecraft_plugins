package cn.simon.simonrpg.mob.entity;

import java.util.HashMap;

public class MobItem {

    private String name;

    private boolean isBoss = false;

    public void setIsBoss(boolean boss) {
        isBoss = boss;
    }

    public boolean getIsBoss(){
        return isBoss;
    }

    public void setItemNum(HashMap<String, String> itemNum) {
        this.itemNum = itemNum;
    }

    public HashMap<String, String> getItemNum() {
        return itemNum;
    }

    private HashMap<String,String> itemNum;

    private HashMap<String,String> item;

    public void setName(String name) {
        this.name = name;
    }

    public void setItem(HashMap<String, String> item) {
        this.item = item;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, String> getItem() {
        return item;
    }
}
