package cn.simon.simonrpg.bottle.event;

import cn.simon.simonrpg.SimonRpg;
import cn.simon.simonrpg.bottle.entity.Bottle;
import cn.simon.simonrpg.bottle.holder.BottleHolder;
import cn.simon.simonrpg.constants.CategoryConst;
import cn.simon.simonrpg.constants.ColorConst;
import cn.simon.simonrpg.constants.RewardConst;
import cn.simon.simonrpg.constants.ServerConst;
import cn.simon.simonrpg.equip.builder.RandomEquipmentBuilder;
import cn.simon.simonrpg.equip.holder.EquipmentHolder;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.nbt.data.BottleType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Logger;

public class OpenBottleListener implements Listener {

    public OpenBottleListener(){
        Logger logger = Logger.getLogger("open-bottle");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }

    @EventHandler
    public void openBottle(PlayerInteractEvent event){
        Player player = event.getPlayer();

            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if (item.hasItemMeta()){
                Bottle bottle = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(NameSpaceKeyBuilder.getBottleNameSpaceKey(), new BottleType());
                if (bottle != null){
                    //先检查背包
                    int freeSlot = findFreeSlot(player);
                    if (freeSlot != -1){
                    if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        synchronized (player){
                            int num = item.getAmount();
                            if (num > 0){
                                item.setAmount(item.getAmount()-1);
                                Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                                    //获取item数量
                                    event.getPlayer().sendMessage(openBottleSelfMessage(bottle.getName()));
                                    String res = "";
                                    Random random = new Random();
                                    double randomValue = random.nextDouble(100);
                                    LinkedHashMap<String, String> reward = bottle.getReward();
                                    if (reward == null){
                                        event.setCancelled(true);
                                        return;
                                    }
                                    for (String rewardRes : reward.keySet()) {
                                        if (randomValue < Double.parseDouble(reward.get(rewardRes))){
                                            res += rewardRes;
                                            break;
                                        }
                                    }
                                    player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1.0f, 1.0f);
                                    switch (res){
                                        case RewardConst.COMMON_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("普通", CategoryConst.COMMON),player,freeSlot);
                                        }
                                        case RewardConst.RARE_BOTTLE_Y -> sendBottle("稀有遗物宝藏",player);
                                        case RewardConst.RARE_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("稀有",CategoryConst.RARE),player,freeSlot);
                                        }
                                        case RewardConst.LEGEND_BOTTLE_Y -> sendBottle("传说遗物宝藏",player);
                                        case RewardConst.LEGEND_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("传说",CategoryConst.LEGEND),player,freeSlot);
                                        }
                                        case RewardConst.ANCIENT_BOTTLE_Y -> sendBottle("远古遗物宝藏",player);
                                        case RewardConst.ANCIENT_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("远古",CategoryConst.ANCIENT),player,freeSlot);
                                        }
                                        case RewardConst.PS_LEGEND_BOTTLE_P -> sendBottle("传说破碎宝藏",player);
                                        case RewardConst.PS_LEGEND_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("传说破碎",CategoryConst.PS_LEGEND),player,freeSlot);
                                        }
                                        case RewardConst.PS_IMOL_BOTTLE_P -> sendBottle("不朽破碎宝藏",player);
                                        case RewardConst.PS_IMOL_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("不朽破碎",CategoryConst.PS_IMMORTAL),player,freeSlot);
                                        }
                                        case RewardConst.IMOL_BOTTLE_Y -> {
                                            sendBottle("不朽遗物宝藏",player);

                                            if (bottle.getName().equals(RewardConst.LEGEND_BOTTLE_Y)){
                                                Bukkit.getServer().broadcastMessage(openBottleMessage(player.getName(),bottle.getName(),RewardConst.IMOL_BOTTLE_Y));
                                            }
                                        }
                                        case RewardConst.IMOL_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("不朽",CategoryConst.IMMORTAL),player,freeSlot);
                                            if (bottle.getName().equals(RewardConst.IMOL_BOTTLE_Y)){
                                                Bukkit.getServer().broadcastMessage(openEquipmentMessage(player.getName(),bottle.getName(),"§6不朽装备"));
                                            }
                                        }
                                        case RewardConst.WIND_FIRE_BOTTLE -> sendBottle("风林火山宝藏",player);
                                        case RewardConst.MYTH_BOTTLE_Y ->sendBottle("神话遗物宝藏",player);
                                        case RewardConst.MYTH_EQUIPMENT ->  sendItem(makeCategoryRandomEquipment("神话",CategoryConst.MYTH),player,freeSlot);
                                        case RewardConst.DAZZLING_BOTTLE_S -> sendItem(makeOneBottle("璀璨圣物珍藏"),player,freeSlot);
                                        case RewardConst.DAZZLING_EQUIPMENT -> sendItem(makeCategoryRandomEquipment("璀璨",CategoryConst.DAZZLING), player,freeSlot);
                                        case RewardConst.TREASURE_BOTTLE_Y -> {
                                            sendBottle("至宝遗物宝藏",player);
                                            Bukkit.getServer().broadcastMessage(openBottleMessage(player.getName(),bottle.getName(),RewardConst.TREASURE_BOTTLE_Y));

                                        }
                                        case RewardConst.TREASURE_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("至宝",CategoryConst.TREASURE),player,freeSlot);
                                            if (!bottle.getName().contains("史诗")){
                                                Bukkit.getServer().broadcastMessage(openEquipmentMessage(player.getName(),bottle.getName(),"§a至宝装备"));
                                            }
                                        }
                                        case RewardConst.EPIC_BOTTLE_S -> {
                                            sendBottle("史诗圣物珍藏",player);
                                            Bukkit.getServer().broadcastMessage(openBottleMessage(player.getName(),bottle.getName(),RewardConst.EPIC_BOTTLE_S));

                                        }
                                        case RewardConst.EPIC_EQUIPMENT -> {
                                            sendItem(makeCategoryRandomEquipment("史诗",CategoryConst.EPIC),player,freeSlot);
                                            Bukkit.getServer().broadcastMessage(openEquipmentMessage(player.getName(),bottle.getName(),"§e史诗装备"));
                                        }
                                    }
                                });
                            }
                        }

                    }

                    }else{
                        player.playSound(player.getLocation(),Sound.ENTITY_VILLAGER_AMBIENT,1.0f,1.0f);
                    }
                    event.setCancelled(true);
            }
        }
    }

    private static ItemStack makeOneBottle(String name){
        return BottleHolder.bottles.get(name);
    }

    private static String openBottleSelfMessage(String bottle){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.GRAY+"你打开了一个 "+bottle +ChatColor.GRAY+" .";
    }



    private static String openBottleMessage(String name,String from,String to){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.GREEN+name+ChatColor.GRAY+" 打开 "+from+ChatColor.GRAY+" 居然获得了 "+to;
    }


    private static String openEquipmentMessage(String name,String from,String to){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.GREEN+name+ChatColor.GRAY+" 打开 "+from+ChatColor.GRAY+" 居然获得了一件传说中的 "+to;
    }

    /**
     * 制造对应品质随机武器
     * @param category 类别名称
     * @param categoryLabel 类别标签
     * @return 随机物品
     */
    private static ItemStack makeCategoryRandomEquipment(String category,String categoryLabel){
        return RandomEquipmentBuilder.makeRandomEquipment(EquipmentHolder.randomEquipments.get(category),categoryLabel);
    }


    public static int findFreeSlot(Player player){
        ItemStack[] contents = player.getInventory().getStorageContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack currentItem = contents[i];
            if (currentItem == null) {
                return i;
            }
        }
        player.sendMessage(ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ ColorConst.COMMON_COLOR+"你的背包已经满了!");
        return -1; // 如果没有找到可用槽位，返回 -1
    }
    /**
     * 找到指定装备槽
     * @param item 找到指定槽位
     * @param player 玩家
     * @return 装备槽序号
     */
    public static int findSlotForItem(ItemStack item, Player player) {
        ItemStack[] contents = player.getInventory().getStorageContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack currentItem = contents[i];
            if (currentItem == null || (currentItem.isSimilar(item) && currentItem.getAmount() < currentItem.getMaxStackSize())) {
                return i;
            }
        }
        player.sendMessage(ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ ColorConst.COMMON_COLOR+"你的背包已经满了!");
        return -1; // 如果没有找到可用槽位，返回 -1
    }

    /**
     * 给玩家发送物品
     * @param item 物品
     * @param player 玩家
     */
    private static void sendItem(ItemStack item,Player player,int slot){
        player.getInventory().setItem(slot,item);
    }

    private static void sendBottle(String type,Player player){
        ItemStack item = makeOneBottle(type);
        int slotForItem = findSlotForItem(item, player);
        //获取瓶子
        ItemStack bottle = player.getInventory().getItem(slotForItem);
        if (bottle == null){
            player.getInventory().setItem(slotForItem,item);
            return;
        }
        bottle.setAmount(bottle.getAmount()+1);
    }


}
