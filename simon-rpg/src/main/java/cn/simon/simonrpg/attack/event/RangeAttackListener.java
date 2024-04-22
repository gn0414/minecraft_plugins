package cn.simon.simonrpg.attack.event;

import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.constants.WeaponBonusAttributeConst;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.nbt.data.WeaponType;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Logger;

public class RangeAttackListener implements Listener {

    public RangeAttackListener(){
        Logger logger = Logger.getLogger("range-attack");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void attackRange(PlayerInteractEvent event){
        if (event.getAction().equals(Action.LEFT_CLICK_AIR)){
            synchronized (event.getPlayer()){
                //我们来获取主手武器是否具有属性攻击范围
                Player player = event.getPlayer();

                ItemStack item = null;
                Weapon weapon = null;
                try {
                    item = player.getInventory().getItemInMainHand();
                    weapon = item.getItemMeta().getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
                }catch (NullPointerException e){
                    return;
                }
                if (weapon == null){
                    return;
                }

                LinkedHashMap<String, String> bonusAttribute = weapon.getBonusAttribute();
                if (weapon.getBonusAttribute().containsKey(WeaponBonusAttributeConst.ATTACK_RANGE_LABEL)){

                    int range = (int)Double.parseDouble(bonusAttribute.get(WeaponBonusAttributeConst.ATTACK_RANGE_LABEL))/60 + 3;
                    LivingEntity entity = attackRange(range, event.getPlayer());
                    if (entity == null)return;

                    if (bonusAttribute.containsKey(WeaponBonusAttributeConst.SWEEP_DAMAGE_LABEL)){
                        causeRangeDamage(player,entity,1);
                        double sweep = Double.parseDouble(bonusAttribute.get(WeaponBonusAttributeConst.SWEEP_DAMAGE_LABEL))/40;
                        //横扫
                        List<Entity> nearbyEntities = entity.getNearbyEntities(sweep, sweep, sweep);
                        nearbyEntities.remove(entity);
                        nearbyEntities.remove(player);
                        for (Entity nearbyEntity : nearbyEntities) {
                            if (nearbyEntity instanceof LivingEntity near){
                                causeRangeDamage(player,near, ConfigConst.SWEEP_PREFIX);
                            }
                        }
                    }else{
                        causeRangeDamage(player,entity,1);
                    }
                }
            }
        }
    }

    private static LivingEntity attackRange(int range,Player player){
        Location eyeLocation = player.getEyeLocation(); //获取玩家视线位置
        Location startLocation;
        Vector direction = eyeLocation.getDirection(); //获取玩家面朝方向向量
        double nearestDistance = Double.MAX_VALUE;
        LivingEntity nearestEntity = null;
        try {
            for (int i = 1; i <= range; i++) {
                startLocation = eyeLocation;
                Location targetLocation = startLocation.add(i*direction.getX()/Math.sqrt(2),0,i*direction.getZ()/Math.sqrt(2));
                for (Entity entity : player.getWorld().getNearbyEntities(targetLocation, 2, 2, 2)) {
                    if (entity.equals(player)) {
                        // 排除自己的实体
                        continue;
                    }

                    double distance = entity.getLocation().distance(eyeLocation);
                    if (distance < nearestDistance) {
                        nearestDistance = distance;
                        if (entity instanceof LivingEntity entity1){
                            nearestEntity = entity1;
                        }
                        // 找到实体后退出循环
                        break;
                    }
                }

                // 如果找到了实体则退出外层循环
                if (nearestEntity != null) {
                    break;
                }
            }
        }catch (IllegalArgumentException ignored){
        }
        return nearestEntity;
    }


    public void causeRangeDamage(Player player, LivingEntity entity, double damage) {
        // 在这里模拟造成伤害的过程
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player,entity, EntityDamageEvent.DamageCause.CUSTOM,damage);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

}
