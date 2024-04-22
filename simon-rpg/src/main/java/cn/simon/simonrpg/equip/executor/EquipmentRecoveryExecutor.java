package cn.simon.simonrpg.equip.executor;

import cn.simon.simonrpg.constants.RecoveryConst;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class EquipmentRecoveryExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s,  String[] strings) {
        if (s.equalsIgnoreCase("recovery")){
            if (strings[0] != null && strings[1] != null){
                Player player = Bukkit.getServer().getPlayer(strings[0]);
                if (player == null){
                    commandSender.sendMessage("玩家不存在");
                    return true;
                }
                int money = sumMoney(player, strings[1]);
                if (money != 0){
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"money give "+player.getName()+" "+money);
                }
                return true;
            }
        }
        return false;
    }

    private static int sumMoney(Player player,String category){
        Inventory inventory = player.getInventory();
        int sum = 0;
        int num = 0;
        switch (category){
            case RecoveryConst.COMMON ->{
                for (int i = 0;i<inventory.getSize();i++) {
                    ItemStack item = inventory.getItem(i);
                    if (i < 36 || i >40){
                        if (item != null){
                            if (item.hasItemMeta()){
                                List<String> lore = item.getItemMeta().getLore();
                                if (lore != null){

                                    boolean first = false;
                                    boolean second = false;

                                    for (String loreTag : lore) {
                                        if (loreTag.contains("品质:")){
                                            String[] split = loreTag.split(":");
                                            if (split[1].contains(RecoveryConst.COMMON)){
                                               first = true;
                                            }
                                        }

                                        if (loreTag.contains("需求等级")){
                                            second = true;
                                        }

                                        if (first && second){
                                            sum += 50;
                                            inventory.remove(item);
                                            num++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN+"一共回收了 "+ChatColor.DARK_GREEN+num+ChatColor.GREEN+" 个物品, + "+ChatColor.DARK_GREEN+"$"+sum+"!");
            }
            case RecoveryConst.RARE -> {
                for (int i = 0;i<inventory.getSize();i++) {
                    ItemStack item = inventory.getItem(i);
                    if (i < 36 || i >40){
                        if (item != null){
                            if (item.hasItemMeta()){
                                List<String> lore = item.getItemMeta().getLore();
                                if (lore != null){
                                    boolean first = false;
                                    boolean second = false;
                                    for (String loreTag : lore) {
                                        if (loreTag.contains("品质:")){
                                            String[] split = loreTag.split(":");
                                            if (split[1].contains(RecoveryConst.RARE)){
                                                first = true;
                                            }
                                        }

                                        if (loreTag.contains("需求等级")){
                                            second = true;
                                        }

                                        if (first && second){
                                            sum += 300;
                                            inventory.remove(item);
                                            num++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN+"一共回收了 "+ChatColor.DARK_GREEN+num+ChatColor.GREEN+" 个物品, + "+ChatColor.DARK_GREEN+"$"+sum+"!");
            }
            case RecoveryConst.LEGEND -> {
                for (int i = 0;i<inventory.getSize();i++) {
                    ItemStack item = inventory.getItem(i);
                    if (i < 36 || i >40){
                        if (item != null){
                            if (item.hasItemMeta()){
                                List<String> lore = item.getItemMeta().getLore();
                                if (lore != null){
                                    boolean first = false;
                                    boolean second = false;
                                    for (String loreTag : lore) {
                                        if (loreTag.contains("品质:")){
                                            String[] split = loreTag.split(":");
                                            if (split[1].contains(RecoveryConst.LEGEND)){
                                                first = true;
                                            }
                                        }

                                        if (loreTag.contains("需求绑定")){
                                            second = true;
                                        }

                                        if (first && second){
                                            sum += 2000;
                                            inventory.remove(item);
                                            num++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN+"一共回收了 "+ChatColor.DARK_GREEN+num+ChatColor.GREEN+" 个物品, + "+ChatColor.DARK_GREEN+"$"+sum+"!");
            }
            case RecoveryConst.IMO -> {
                for (int i = 0;i<inventory.getSize();i++) {
                    ItemStack item = inventory.getItem(i);
                    if (i < 36 || i >40){
                        if (item != null){
                            if (item.hasItemMeta()){
                                List<String> lore = item.getItemMeta().getLore();
                                if (lore != null){
                                    boolean first = false;
                                    boolean second = false;
                                    for (String loreTag : lore) {
                                        if (loreTag.contains("品质:")){
                                            String[] split = loreTag.split(":");
                                            if (split[1].contains(RecoveryConst.IMO)){
                                                first = true;
                                            }
                                        }

                                        if (loreTag.contains("需求绑定")){
                                            second = true;
                                        }
                                        if (first && second){
                                            sum += 2000;
                                            inventory.remove(item);
                                            num++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN+"一共回收了 "+ChatColor.DARK_GREEN+num+ChatColor.GREEN+" 个物品, + "+ChatColor.DARK_GREEN+"$"+sum+"!");
            }
            case RecoveryConst.W_LEGEND -> {
                for (int i = 0;i<inventory.getSize();i++) {
                    ItemStack item = inventory.getItem(i);
                    if (i < 36 || i >40){
                        if (item != null){
                            if (item.hasItemMeta()){
                                List<String> lore = item.getItemMeta().getLore();
                                if (lore != null){
                                    boolean first = false;
                                    boolean second = false;
                                    for (String loreTag : lore) {
                                        if (loreTag.contains("品质:")){
                                            String[] split = loreTag.split(":");
                                            if (split[1].contains(RecoveryConst.LEGEND) && item.getItemMeta().getDisplayName().contains("伪")){
                                                first = true;
                                            }
                                        }

                                        if (loreTag.contains("需求绑定")){
                                            second = true;
                                        }

                                        if (first && second){
                                            sum += 2000;
                                            inventory.remove(item);
                                            num++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN+"一共回收了 "+ChatColor.DARK_GREEN+num+ChatColor.GREEN+" 个物品, + "+ChatColor.DARK_GREEN+"$"+sum+"!");
            }
            case RecoveryConst.W_IMO -> {
                for (int i = 0;i<inventory.getSize();i++) {
                    ItemStack item = inventory.getItem(i);
                    if (i < 36 || i >40){
                        if (item != null){

                            if (item.hasItemMeta()){
                                List<String> lore = item.getItemMeta().getLore();
                                if (lore != null){
                                    boolean first = false;
                                    boolean second = false;
                                    for (String loreTag : lore) {
                                        if (loreTag.contains("品质:")){
                                            String[] split = loreTag.split(":");
                                            if (split[1].contains(RecoveryConst.IMO) && item.getItemMeta().getDisplayName().contains("伪")){
                                                first = true;
                                            }
                                        }

                                        if (loreTag.contains("需求绑定")){
                                            second = true;
                                        }

                                        if (first && second){
                                            sum += 2000;
                                            inventory.remove(item);
                                            num++;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                player.sendMessage(ChatColor.GREEN+"一共回收了 "+ChatColor.DARK_GREEN+num+ChatColor.GREEN+" 个物品, + "+ChatColor.DARK_GREEN+"$"+sum+"!");
            }
            default -> {
                return 0;
            }
        }
        return sum;
    }
}
