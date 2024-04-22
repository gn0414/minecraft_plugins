package cn.simon.simonrpg.mob.executor;
import cn.simon.simonrpg.bottle.holder.BottleHolder;
import cn.simon.simonrpg.task.holder.TaskHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Objects;
import java.util.Random;

public class BossFallExecutor implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender commandSender,  Command command,  String s, String[] strings) {
        if (s.equalsIgnoreCase("bossfall")){
            //首先检查三个参数
            if (strings[0] != null && strings[1] != null && strings[2] != null){
                Player player = Bukkit.getPlayer(strings[2]);
                if (BottleHolder.bottles.containsKey(strings[0])){
                    if (player != null){
                        try {
                            int num = Integer.parseInt(strings[1]);
                            putItem(player,BottleHolder.bottles.get(strings[0]),num,true);
                        }catch (NumberFormatException e) {
                            commandSender.sendMessage("请输入合法数字");
                        }
                    }
                }else if (TaskHolder.tasks.containsKey(strings[0])){
                    if (player != null){
                        try {
                            int num = Integer.parseInt(strings[1]);
                            putItem(player,TaskHolder.tasks.get(strings[0]),num,false);
                        }catch (NumberFormatException e) {
                            commandSender.sendMessage("请输入合法数字");
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static void putItem(Player player, ItemStack item, int num,boolean isBottle) {
        Random rand = new Random();
        if (num > 64 )num = 64;
        int resultNum = rand.nextInt(num)+1;
        //获取玩家背包
        PlayerInventory inventory = player.getInventory();
        int sameSlot = findSameSlot(inventory,item);
        if (sameSlot != -1){
            //找到相同的了获取相同的
            ItemStack sameItem = inventory.getItem(sameSlot);
            assert sameItem != null;
            int amount = sameItem.getAmount();
            if (amount + resultNum > 64){
                //先设置原位置为64
                sameItem.setAmount(64);
                inventory.setItem(sameSlot,sameItem);

                //然后找一个空位置

                int firstEmptySlot = findFirstEmptySlot(inventory);
                if (firstEmptySlot == -1){
                    player.sendMessage(ChatColor.AQUA+"无尽试炼 >> "+ChatColor.GRAY+"你的背包已经满了!");
                }else{
                    ItemStack cloneStack = sameItem.clone();
                    cloneStack.setAmount(amount + resultNum-64);
                    inventory.setItem(firstEmptySlot,cloneStack);
                }
            }else{
                sameItem.setAmount(sameItem.getAmount()+resultNum);
                inventory.setItem(sameSlot,sameItem);
                if (isBottle){
                    player.sendMessage(ChatColor.AQUA+"无尽试炼 >> "+ChatColor.GRAY+"收到了珍藏");
                }
            }

        }else{
            int firstEmptySlot = findFirstEmptySlot(inventory);
            if (firstEmptySlot == -1){
                player.sendMessage(ChatColor.AQUA+"无尽试炼 >> "+ChatColor.GRAY+"你的背包已经满了!");
            }else{
                item.setAmount(resultNum);
                inventory.setItem(firstEmptySlot,item);
                if (isBottle){
                    player.sendMessage(ChatColor.AQUA+"无尽试炼 >> "+ChatColor.GRAY+"收到了珍藏");
                }
            }
        }
    }

    private static int findSameSlot(Inventory inventory,ItemStack itemStack){
        for (int i = 0;i<inventory.getSize();i++) {
            if (itemStack.isSimilar(inventory.getItem(i)) && Objects.requireNonNull(inventory.getItem(i)).getAmount() != 64) {
                return i;
            };
        }
        return -1;
    }

    private static int findFirstEmptySlot(Inventory inventory) {
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
