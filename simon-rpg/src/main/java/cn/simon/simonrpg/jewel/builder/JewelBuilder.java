package cn.simon.simonrpg.jewel.builder;


import cn.simon.simonrpg.constants.ColorConst;
import cn.simon.simonrpg.constants.JewelType;
import cn.simon.simonrpg.jewel.entity.Jewel;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JewelBuilder {

    public static ItemStack makeJewel(Jewel jewel){
        String type = jewel.getType();
        ItemStack itemStack = makeItem(type);
        if (itemStack != null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null){
                itemMeta.setDisplayName(jewel.getName());
                List<String> lore = itemMeta.getLore();
                if (lore== null)lore = new ArrayList<>();

                lore.add(ColorConst.TITLE_COLOR+"种类: "+jewel.getType());
                if (jewel.getInnerType() != null){
                    lore.add(ColorConst.TITLE_COLOR+"类型: "+jewel.getInnerType());
                }
                lore.add(ColorConst.TITLE_COLOR+"品质: "+jewel.getCategory());

                if (jewel.getLevel() != null){
                    lore.add(ColorConst.TITLE_COLOR+"等级: "+jewel.getLevel());
                }
                if (!jewel.getSuccessPercent().equalsIgnoreCase("")){
                    lore.add(ColorConst.TITLE_COLOR+"成功: "+ChatColor.GREEN+jewel.getSuccessPercent()+"%" + ColorConst.TITLE_COLOR+" | 失败: "+ ChatColor.RED+(100-Integer.parseInt(jewel.getSuccessPercent())+"%"));
                }
                lore.add("");

                String[] split = jewel.getDescription().split(",");
                lore.addAll(Arrays.asList(split));
                lore.add("");

                String[] split1 = jewel.getPrompt().split(",");

                lore.addAll(Arrays.asList(split1));

                itemMeta.setLore(lore);
                setNBT(itemMeta,jewel);
            }
            itemStack.setItemMeta(itemMeta);

        }
        return itemStack;
    }

    public static ItemStack makeItem(String type){
        ItemStack itemStack = null;
        switch (type.substring(2)){
            case JewelType.GEM ->{itemStack = new ItemStack(Material.ENDER_PEARL);}
            case JewelType.ENCHANT -> {itemStack = new ItemStack(Material.BOOK);}
            case JewelType.RUNE -> {itemStack = new ItemStack(Material.MAGMA_CREAM);}
            case JewelType.LUCKY -> {itemStack = new ItemStack(Material.GLOWSTONE_DUST);}
            case JewelType.SKILL -> {itemStack = new ItemStack(Material.NETHER_STAR);}
            case JewelType.SOUL -> {itemStack = new ItemStack(Material.GUNPOWDER);}
            default -> {}
        }
        return itemStack;
    }

    public static void setNBT(ItemMeta itemMeta,Jewel jewel){
        itemMeta.getPersistentDataContainer().set(NameSpaceKeyBuilder.getJewelKey(),new cn.simon.simonrpg.nbt.data.JewelType(),jewel);
    }
}
