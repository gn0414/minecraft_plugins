package cn.simon.simonrpg.bottle.executor;

import cn.simon.simonrpg.bottle.builder.BottleBuilder;
import cn.simon.simonrpg.bottle.holder.BottleHolder;
import cn.simon.simonrpg.equip.holder.EquipmentHolder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CustomBottleExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("custom-bottle")){
            if (strings[0] != null && strings[1] != null){
                ItemStack item = BottleHolder.bottles.get(strings[0]);
                if (item == null){
                    commandSender.sendMessage("瓶子不存在");
                    return true;
                }
                try {
                    int num = Integer.parseInt(strings[1]);
                    if (num > 64)num = 64;
                    item.setAmount(num);
                }catch (ClassCastException e){
                    commandSender.sendMessage("数量非法");
                    return true;
                }

                if (commandSender instanceof Player player){
                    int emptySlot = findEmptySlot(player);

                    if (emptySlot == -1){
                        commandSender.sendMessage("背包已满");
                        return true;
                    }
                    player.getInventory().setItem(emptySlot,item);
                    player.sendMessage(ChatColor.AQUA+"无尽试炼 >> "+ChatColor.GRAY+"收到了珍藏");
                    return true;
                }

            }else commandSender.sendMessage("参数列表不正确");

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
