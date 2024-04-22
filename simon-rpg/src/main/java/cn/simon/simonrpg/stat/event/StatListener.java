package cn.simon.simonrpg.stat.event;

import cn.simon.simonrpg.SimonRpg;
import cn.simon.simonrpg.constants.EquipMainAttributeConst;
import cn.simon.simonrpg.constants.EquipmentBonusAttributeConst;
import cn.simon.simonrpg.constants.RuneConst;
import cn.simon.simonrpg.equip.entity.Equipment;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.nbt.data.EquipmentType;
import cn.simon.simonrpg.nbt.data.WeaponType;
import cn.simon.simonrpg.stat.entity.Stat;
import cn.simon.simonrpg.stat.holder.StatHolder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.logging.Logger;

public class StatListener implements Listener {

    public StatListener(){
        Logger logger = Logger.getLogger("stat");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }
    private static final int MAX_LEVEL = 70;

    /**
     * 保证玩家不超过指定级别
     * @param event 玩家获取经验事件
     */
    @EventHandler
    public void MaxLevelHandler(PlayerExpChangeEvent event){
        if (event.getPlayer().getLevel() >= MAX_LEVEL) {
            event.setAmount(0);
        }
    }

    @EventHandler
    public void playerReborn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        setStat(player,player.getInventory());
    }

    /**
     * 删去玩家状态
     * @param event 玩家离开事件
     */
    @EventHandler
    public void removeStatHandler(PlayerQuitEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
            Player player = event.getPlayer();
            removeStat(player);
        });
    }


    /**
     * 玩家进入游戏初始化属性
     * @param event 玩家加入游戏事件
     */
    @EventHandler
    public void setStatHandler(PlayerJoinEvent event){
        Bukkit.getScheduler().runTaskAsynchronously(JavaPlugin.getPlugin(SimonRpg.class), () -> {
            Player player = event.getPlayer();
            PlayerInventory inventory = player.getInventory();
            setStat(player,inventory);
        });
    }

    private static void removeStat(Player player){
        StatHolder.PLAYER_STAT.remove(player);
    }


    public static void setStat(Player player, PlayerInventory inventory){
        // 获取头盔
        ItemStack helmet = inventory.getHelmet();

        // 获得胸甲
        ItemStack chestplate = inventory.getChestplate();

        // 获得腿甲
        ItemStack leggings = inventory.getLeggings();

        // 获得靴子
        ItemStack boots = inventory.getBoots();

        // 获得副手
        ItemStack itemInOffHand = inventory.getItemInOffHand();

        // 获得主手
        ItemStack itemInMainHand = inventory.getItemInMainHand();

        Stat stat = new Stat();

        addStat(helmet, stat);
        addStat(chestplate, stat);
        addStat(leggings, stat);
        addStat(boots, stat);
        addStat(itemInOffHand, stat);
        addWeaponRune(itemInMainHand, stat);
        addRune(player, stat);

        // 获取之前保存的Stat对象
        Stat previousStat = StatHolder.PLAYER_STAT.getOrDefault(player, new Stat());

        // 比较之前的Stat和当前的Stat，获取增加和移除的属性增益
        Map<String, Integer> preRunes = previousStat.getRunes();

        Map<String, Integer> nowRunes = stat.getRunes();

        diffClearType(preRunes.keySet(),nowRunes.keySet(),player);
        // 将当前属性增益保存
        StatHolder.PLAYER_STAT.put(player, stat);
    }

    private static void diffClearType(Set<String> preKeys, Set<String> nowKeys,Player player) {
        Set<String> removedKeys = new HashSet<>(preKeys);
        removedKeys.removeAll(nowKeys);

        for (String removedKey : removedKeys) {
                switch (removedKey) {
                    case RuneConst.KX_1, RuneConst.CCWZ_1 -> removePotionEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 0);
                    case RuneConst.KX_2, RuneConst.CCWZ_2 -> removePotionEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 1);
                    case RuneConst.KX_3, RuneConst.CCWZ_3 -> removePotionEffect(player, PotionEffectType.DAMAGE_RESISTANCE, 2);
                    case RuneConst.AYZY_1 -> removePotionEffect(player, PotionEffectType.NIGHT_VISION, 0);
                    case RuneConst.CKXY -> removePotionEffect(player, PotionEffectType.INVISIBILITY, 0);
                    case RuneConst.HYBH_1 -> removePotionEffect(player, PotionEffectType.FIRE_RESISTANCE, 0);
                    case RuneConst.HYBH_2 -> removePotionEffect(player, PotionEffectType.FIRE_RESISTANCE, 1);
                    case RuneConst.HYBH_3 -> removePotionEffect(player, PotionEffectType.FIRE_RESISTANCE, 2);
                    case RuneConst.LL_1 -> removePotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 0);
                    case RuneConst.LL_2 -> removePotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 1);
                    case RuneConst.LL_3 -> removePotionEffect(player, PotionEffectType.INCREASE_DAMAGE, 2);
                    case RuneConst.SMHF_1 -> removePotionEffect(player, PotionEffectType.REGENERATION, 0);
                    case RuneConst.SMHF_2 -> removePotionEffect(player, PotionEffectType.REGENERATION, 1);
                    case RuneConst.TYTS_1 -> removePotionEffect(player, PotionEffectType.JUMP, 0);
                    case RuneConst.TYTS_2 -> removePotionEffect(player, PotionEffectType.JUMP, 1);
                    case RuneConst.TYTS_3 -> removePotionEffect(player, PotionEffectType.JUMP, 2);
                    case RuneConst.SDTS_1 -> removePotionEffect(player, PotionEffectType.SPEED, 0);
                    case RuneConst.SDTS_2 -> removePotionEffect(player, PotionEffectType.SPEED, 1);
                    case RuneConst.HSZX_1 -> removePotionEffect(player, PotionEffectType.WATER_BREATHING, 0);
                    case RuneConst.HSZX_2 -> removePotionEffect(player, PotionEffectType.WATER_BREATHING, 1);
                    case RuneConst.HSZX_3 -> removePotionEffect(player, PotionEffectType.WATER_BREATHING, 2);
                }
        }
    }

    /**
     *
     * @param player 玩家
     * @param effectType 增益效果类型
     * @param amplifier 等级
     */

    private static void removePotionEffect(Player player, PotionEffectType effectType, int amplifier) {

        // 遍历玩家当前所有的增益效果
        for (PotionEffect effect : player.getActivePotionEffects()) {
            // 检查效果类型和等级是否匹配
            if (effect.getType().equals(effectType) && effect.getAmplifier() == amplifier) {
                // 如果匹配，则移除该增益效果
                Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(SimonRpg.class),()->{
                    player.removePotionEffect(effect.getType());
                });
                break; // 可以结束循环，因为我们只需要移除一个特定类型和等级的效果
            }
        }
    }



    private static void addRune(Player player, Stat stat) {
        Map<String, Integer> runes = stat.getRunes();
        runes.keySet().forEach(
                    r -> {
                        Bukkit.getScheduler().runTask(JavaPlugin.getPlugin(SimonRpg.class), () -> {
                            switch (r) {
                                case RuneConst.KX_1, RuneConst.CCWZ_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, -1, 0));
                                case RuneConst.KX_2, RuneConst.CCWZ_2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, -1, 1));
                                case RuneConst.KX_3, RuneConst.CCWZ_3 -> player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, -1, 2));
                                case RuneConst.AYZY_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, -1, 0));
                                case RuneConst.CKXY -> player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,-1, 0));
                                case RuneConst.HYBH_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,-1, 0));
                                case RuneConst.HYBH_2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, -1, 1));
                                case RuneConst.HYBH_3 -> player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,-1, 2));
                                case RuneConst.LL_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,-1, 0));
                                case RuneConst.LL_2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, -1, 1));
                                case RuneConst.LL_3 -> player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,-1, 2));
                                case RuneConst.SMHF_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, -1, 0));
                                case RuneConst.SMHF_2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, -1, 1));
                                case RuneConst.TYTS_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, -1, 0));
                                case RuneConst.TYTS_2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, -1, 1));
                                case RuneConst.TYTS_3 -> player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, -1, 2));
                                case RuneConst.SDTS_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 0));
                                case RuneConst.SDTS_2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, -1, 1));
                                case RuneConst.HSZX_1 -> player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, -1, 0));
                                case RuneConst.HSZX_2 -> player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,-1, 1));
                                case RuneConst.HSZX_3 -> player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING,-1, 2));

                            }
                        });
                    }
        );
    }


    private static void addWeaponRune(ItemStack itemStack,Stat stat){
        if (itemStack == null)return;
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            Weapon weapon = itemMeta.getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(),new WeaponType());

            if (weapon != null){
                List<String> rune = weapon.getRune();
                Map<String, Integer> runes = stat.getRunes();
                //记录符文
                rune.forEach(
                        r -> {
                            if (!r.isEmpty()){
                                if (runes.containsKey(r)) {
                                    Integer num = runes.get(r);
                                    num++;
                                    runes.put(r,num);
                                }else runes.put(r,1);
                            }
                        }
                );
                stat.setRunes(runes);
            }
        }

    }

    private static void addStat(ItemStack itemStack,Stat stat){
        if (itemStack == null)return;
        if (itemStack.getItemMeta() != null){
            ItemMeta itemMeta = itemStack.getItemMeta();
            Equipment equipment = itemMeta.getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new EquipmentType());
            if (equipment != null){
                LinkedHashMap<String, String> mainAttribute = equipment.getMainAttribute();
                LinkedHashMap<String, String> bonusAttribute = equipment.getBonusAttribute();
                List<String> rune = equipment.getRune();
                //先过主属性
                stat.setPhysicalDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.PHYSICAL_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPhysicalDefense())));

                stat.setFireDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.FIRE_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getFireDefense())));

                stat.setWaterDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.WATER_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getWaterDefense())));

                stat.setPoisonDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.POISON_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPoisonDefense())));

                stat.setMagicDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.MAGIC_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getMagicDefense())));

                stat.setWindDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.WIND_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getWindDefense())));

                stat.setPvpDamage(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.PVP_DAMAGE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPveDamage())));

                stat.setPvpDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.PVP_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPvpDefense())));

                stat.setPveDamage(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.PVE_DAMAGE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPveDamage())));

                stat.setPveDefense(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.PVE_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPveDefense())));

                stat.setBlockChance(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.BLOCK_CHANCE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getBlockChance())));

                stat.setDamageReduce(String.valueOf(Double.parseDouble(mainAttribute.getOrDefault(EquipMainAttributeConst.DAMAGE_REDUCE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getDamageReduce())));
                //过副属性
                stat.setBlockChance(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.BLOCK_CHANCE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getBlockChance())));

                stat.setDamageReduce(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.DAMAGE_REDUCE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getDamageReduce())));

                stat.setLifeSteal(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.LIFE_STEAL_LABEL,"0.0"))
                        +Double.parseDouble(stat.getLifeSteal())));

                stat.setPvpDamage(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.PVP_DAMAGE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPvpDamage())));

                stat.setPvpDefense(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.PVP_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPvpDefense())));

                stat.setPveDamage(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.PVE_DAMAGE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPveDamage())));

                stat.setPveDefense(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.PVE_DEFENSE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPveDefense())));

                stat.setArmorPenetration(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.ARMOR_PENETRATION_LABEL,"0.0"))
                        +Double.parseDouble(stat.getArmorPenetration())));

                stat.setPhysicalDamage(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.PHYSICS_DAMAGE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getPhysicalDamage())));

                stat.setCriticalChance(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.CRITICAL_CHANCE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getCriticalChance())));

                stat.setMissChance(String.valueOf(Double.parseDouble(bonusAttribute.getOrDefault(EquipmentBonusAttributeConst.MISS_CHANCE_LABEL,"0.0"))
                        +Double.parseDouble(stat.getMissChance())));

                if (Double.parseDouble(stat.getMissChance()) > 75)stat.setMissChance("75.00");
                if (Double.parseDouble(stat.getDamageReduce()) > 75)stat.setDamageReduce("75.00");
                if (Double.parseDouble(stat.getBlockChance()) > 75)stat.setBlockChance("75.00");

                Map<String, Integer> runes = stat.getRunes();
                //记录符文
                rune.forEach(
                      r -> {
                          if (!r.isEmpty()){
                              if (runes.containsKey(r)) {
                                  Integer num = runes.get(r);
                                  num++;
                                  runes.put(r,num);
                              }else runes.put(r,1);
                          }
                      }
                );
                stat.setRunes(runes);
            }
        }
    }
}
