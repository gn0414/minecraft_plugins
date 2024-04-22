package cn.simon.simonrpg.nbt.builder;

import cn.simon.simonrpg.SimonRpg;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;


public class NameSpaceKeyBuilder {
    public static NamespacedKey getEquipmentNameSpaceKey(){
        String nameKey = "equipmentinfo";
        return new NamespacedKey(JavaPlugin.getPlugin(SimonRpg.class),nameKey);
    }



    public static NamespacedKey getBottleNameSpaceKey(){
        String nameKey = "bottleinfo";
        return new NamespacedKey(JavaPlugin.getPlugin(SimonRpg.class),nameKey);
    }

    public static NamespacedKey getJewelKey(){
        String nameKey = "jewelinfo";
        return new NamespacedKey(JavaPlugin.getPlugin(SimonRpg.class),nameKey);
    }
}
