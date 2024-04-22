package cn.simon.simonrpg.jewel.executor;

import cn.simon.simonrpg.jewel.builder.JewelBuilder;
import cn.simon.simonrpg.jewel.holder.JewelHolder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CustomJewelExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (s.equalsIgnoreCase("custom-jewel")){
            if (strings[0] !=null && strings[1] != null){
                if (JewelHolder.jewels.containsKey(strings[0])){
                    Player player = Bukkit.getPlayer(strings[1]);
                    if (player != null){
                        int firstEmptySlot = findFirstEmptySlot(player);
                        if (firstEmptySlot != -1){
                            ItemStack itemStack = JewelBuilder.makeJewel(JewelHolder.jewels.get(strings[0]));
                            player.getInventory().setItem(firstEmptySlot,itemStack);
                        }else{
                            commandSender.sendMessage("背包已经满了请检查背包");
                        }
                    }
                }else {
                    commandSender.sendMessage("请输入正确名字");
                }

            }
            return true;
        }
        return false;
    }

    public int findFirstEmptySlot(Player player) {
        Inventory inventory = player.getInventory();
        int firstEmptySlot = -1;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                firstEmptySlot = i;
                break;
            }
        }
        return firstEmptySlot;
    }
}
