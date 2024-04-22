package cn.simon.simonrpg.mob.event;

import cn.simon.simonrpg.SimonRpg;
import cn.simon.simonrpg.bottle.holder.BottleHolder;
import cn.simon.simonrpg.mob.entity.MobItem;
import cn.simon.simonrpg.mob.holder.MobHolder;
import cn.simon.simonrpg.task.holder.TaskHolder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class MobListener implements Listener {

    private static final ConcurrentHashMap<String, List<UUID>> damageRewards = new ConcurrentHashMap<>();

    public MobListener(){
        Logger logger = Logger.getLogger("mob");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }

    /**
     * 获取boss伤害列表
     * @param event 实体伤害事件
     */
    @EventHandler
    public void MobDamageList(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            String mobName = event.getEntity().getName();
            UUID playerUuid = player.getUniqueId();

            if (damageRewards.containsKey(mobName)) {
                List<UUID> uuids = damageRewards.get(mobName);
                if (uuids.contains(playerUuid)) {
                    return;
                }
                uuids.add(playerUuid);
                damageRewards.put(mobName, uuids);
                return;
            }

            if (MobHolder.mobItemHolder.containsKey(mobName)) {
                MobItem mobItem = MobHolder.mobItemHolder.get(mobName);
                // 如果是boss
                if (mobItem.getIsBoss()) {
                    List<UUID> list = new ArrayList<>();
                    list.add(playerUuid);
                    damageRewards.put(mobName, list);
                }
            }
        }
    }

    /**
     * 若死亡则去掉奖励列表
     * @param event 玩家死亡事件
     */
    @EventHandler
    public void playerDeath(PlayerDeathEvent event){
        UUID playerUUID = event.getEntity().getUniqueId();
        damageRewards.entrySet().parallelStream()
                .forEach(entry -> {
                    List<UUID> uuids = entry.getValue();
                    // 移除包含玩家 UUID 的元素
                    uuids.removeIf(uuid -> uuid.equals(playerUUID));
                    // 更新 ConcurrentHashMap 中的值
                    damageRewards.put(entry.getKey(), uuids);
                });
    }

    /**
     * 怪物掉落物品逻辑
     * @param event 实体死亡事件
     */
    @EventHandler
    public void MobDropItemHandler(EntityDeathEvent event){
        Entity entity = event.getEntity();
        String name = entity.getName();
        if (MobHolder.mobItemHolder.containsKey(name)){
            MobItem mobItem = MobHolder.mobItemHolder.get(name);

            HashMap<String, String> item = mobItem.getItem();
            HashMap<String, String> itemNum = mobItem.getItemNum();

            Random random = new Random();
            Set<String> keys = item.keySet();
            for (String key : keys) {
                double value = Double.parseDouble(item.get(key));
                double randomValue = random.nextDouble() * 100;
                if (randomValue <= value) {
                    if (damageRewards.containsKey(name)){
                        List<UUID> uuids = damageRewards.get(name);
                        if (itemNum != null){
                            for (UUID uuid : uuids) {
                                Player player = Bukkit.getServer().getPlayer(uuid);
                                if (player != null) {
                                    if (BottleHolder.bottles.containsKey(key)){
                                        int num = Integer.parseInt(mobItem.getItemNum().get(key));
                                        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                                            putItem(player, BottleHolder.bottles.get(key), num, true);
                                        });
                                    }else if (TaskHolder.tasks.containsKey(key)){
                                        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                                            int num = Integer.parseInt(mobItem.getItemNum().get(key));
                                            putItem(player, TaskHolder.tasks.get(key), num, false);
                                        });
                                    }
                                }
                            }
                        }
                    } else {
                        dropItemByKey(key, entity);
                    }
                }
            }
            damageRewards.remove(name);
        }


    }



    private void dropItemByKey(String key, Entity entity){
        if (TaskHolder.tasks.containsKey(key)) {
            dropItemAtEntity(entity, TaskHolder.tasks.get(key));
        } else if (BottleHolder.bottles.containsKey(key)) {
            dropItemAtEntity(entity, BottleHolder.bottles.get(key));
        }
    }

    private void dropItemAtEntity(Entity entity, ItemStack itemStack){
        entity.getLocation().setY(0.5);
        Item dropItem = entity.getWorld().dropItem(entity.getLocation(), itemStack);
        Vector velocity = new Vector(0.5, 0.0, 0);
        dropItem.setVelocity(velocity);
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

    private static int findSameSlot(Inventory inventory, ItemStack itemStack){
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
