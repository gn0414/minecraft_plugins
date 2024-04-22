package cn.simon.simonrpg.equip.executor;

import cn.simon.simonrpg.equip.holder.EquipmentHolder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Array;

public class CustomEquipExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("custom-equip")){
            if (strings[0] != null && strings[1] != null){
                ItemStack item = new ItemStack(Material.AIR);
                try {
                    switch (strings[0]){
                        case "weapon" -> item = EquipmentHolder.weapons.get(Integer.parseInt(strings[1])-1);
                        case "equipment" -> item = EquipmentHolder.equipments.get(Integer.parseInt(strings[1])-1);
                        case "souvenir" -> item = EquipmentHolder.souvenirs.get(Integer.parseInt(strings[1])-1);
                    }
                }catch (ClassCastException | IndexOutOfBoundsException e){
                    commandSender.sendMessage("装备序号错误");
                    return true;
                }
                if (commandSender instanceof Player player){
                    int emptySlot = findEmptySlot(player);

                    if (emptySlot == -1){
                        commandSender.sendMessage("背包已满");
                        return true;
                    }
                    player.getInventory().setItem(emptySlot,item);
                }

            }else commandSender.sendMessage("参数列表不正确");
            return true;
        }
        return false;
    }

    public int findEmptySlot(Player player) {
        ItemStack[] contents = player.getInventory().getStorageContents();

        for (int i = 0; i < contents.length; i++) {
            ItemStack item = contents[i];
            if (item == null) {
                return i;
            }
        }

        return -1; // 如果没有空闲槽位，返回 -1
    }
}
