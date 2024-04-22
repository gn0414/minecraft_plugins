package cn.simon.simonrpg.bottle.builder;

import cn.simon.simonrpg.bottle.entity.Bottle;
import cn.simon.simonrpg.constants.ColorConst;
import cn.simon.simonrpg.nbt.data.BottleType;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BottleBuilder {


    public static ConcurrentHashMap<String,ItemStack> makeBottles(ConcurrentHashMap<String,Bottle> map){
        //首先获取一个附魔瓶
        ConcurrentHashMap<String,ItemStack> bottles = new ConcurrentHashMap<>();

        Set<Map.Entry<String, Bottle>> entries = map.entrySet();

        for (Map.Entry<String, Bottle> entry : entries) {
            ItemStack bottleItem = new ItemStack(Material.EXPERIENCE_BOTTLE);

            ItemMeta bottleItemMeta = bottleItem.getItemMeta();

            setLore(bottleItemMeta,entry.getValue());

            setNBT(bottleItemMeta,entry.getValue());


            bottleItem.setItemMeta(bottleItemMeta);

            bottles.put(entry.getKey(), bottleItem);
        }
        return bottles;

    }
    public static void setLore(ItemMeta itemMeta,Bottle bottle) {
        itemMeta.setDisplayName(addITALIC(bottle.getName()));
        List<String> lore = itemMeta.getLore();
        if (lore == null)lore = new ArrayList<>();
        lore.add(ChatColor.GRAY+"无限 X");
        lore.add(ColorConst.TITLE_COLOR+ChatColor.ITALIC.toString()+"种类: "+ColorConst.WORD_COLOR+ChatColor.ITALIC.toString()+"珍藏");
        lore.add(ColorConst.TITLE_COLOR+ChatColor.ITALIC.toString()+"品质: "+addITALIC(bottle.getCategory()));
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + ChatColor.ITALIC.toString() + "这件珍藏包含以下物品之一: ");
        lore.add("");
        Set<String> reward = bottle.getReward().keySet();
        for (String rewordOne : reward) {
            lore.add(addITALIC(rewordOne));
        }
        lore.add("");
        String[] split = bottle.getDescription().split(",");
        for (String des : split) {
            lore.add(ChatColor.DARK_GRAY + ChatColor.ITALIC.toString()+des);
        }

        lore.add("");
        itemMeta.setLore(lore);
    }
    private static String addITALIC(String line){
        String color = line.substring(0, 2);
        color += "§o";
        color+= line.substring(2);
        return color;
    }
    private static void setNBT(ItemMeta itemMeta,Bottle bottle){
        itemMeta.getPersistentDataContainer().set(NameSpaceKeyBuilder.getBottleNameSpaceKey(),new BottleType(),bottle);
    }
}
