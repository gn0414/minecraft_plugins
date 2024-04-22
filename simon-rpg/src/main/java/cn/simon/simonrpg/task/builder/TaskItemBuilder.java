package cn.simon.simonrpg.task.builder;

import cn.simon.simonrpg.constants.ColorConst;
import cn.simon.simonrpg.constants.TaskItemConst;
import cn.simon.simonrpg.task.entity.TaskItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskItemBuilder {
    public static ItemStack makeTaskItem(TaskItem item){

        ItemStack itemStack = getItemStack(item);

        if (itemStack != null){

            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null){

                itemMeta.setDisplayName(item.getName());

                List<String> lore = itemMeta.getLore();
                if(lore == null)lore = new ArrayList<>();
                lore.add(ColorConst.TITLE_COLOR+"种类: "+ ChatColor.WHITE+"任务");
                lore.add(ColorConst.TITLE_COLOR+"品质: "+ item.getCategory());
                lore.add("");
                String[] split = item.getDescription().split(",");
                lore.addAll(Arrays.asList(split));
                lore.add("");
                itemMeta.setLore(lore);
            }
            itemStack.setItemMeta(itemMeta);
        }
        return itemStack;


    }

    private static ItemStack getItemStack(TaskItem item) {
        String type = item.getType();

        ItemStack itemStack = null;

        switch (type){
            case TaskItemConst.IRON -> itemStack = new ItemStack(Material.IRON_INGOT);
            case TaskItemConst.GOLD -> itemStack = new ItemStack(Material.GOLD_INGOT);
            case TaskItemConst.DIAMOND -> itemStack = new ItemStack(Material.DIAMOND);
            case TaskItemConst.JADE -> itemStack = new ItemStack(Material.EMERALD);
            case TaskItemConst.MYTH -> itemStack = new ItemStack(Material.PRISMARINE_CRYSTALS);
        }
        return itemStack;
    }
}
