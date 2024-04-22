package cn.simon.simonrpg.attack.event;

import cn.simon.simonrpg.constants.WeaponBonusAttributeConst;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.nbt.data.WeaponType;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.logging.Logger;

import static cn.simon.simonrpg.attack.event.CommonAttackListener.getNum;

public class FishAttackListener implements Listener {

    public FishAttackListener(){
        Logger logger = Logger.getLogger("fish-attack");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }

    /**
     * 钓鱼伤害计算
     * @param event 钓鱼事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void fishAttackHandler(PlayerFishEvent event){
        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
            Player damager = event.getPlayer();
            Entity damaged = event.getCaught();

            if (damaged instanceof LivingEntity livingEntity) {
                Weapon weapon = null;
                ItemMeta itemMeta = damager.getInventory().getItemInMainHand().getItemMeta();
                if (itemMeta != null) {
                    weapon = itemMeta.getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
                }
                if (weapon != null) {
                    //获取距离

                    if (weapon.getBonusAttribute().get(WeaponBonusAttributeConst.ATTACK_RANGE_LABEL) != null){
                        return;
                    }else {
                        double distance = damager.getLocation().distance(damaged.getLocation());
                        if (distance > 3) {
                            distance = distance - 3;
                            if (distance < 10) {
                                causeRangeDamage(damager,livingEntity,1 + 0.1 * distance);
                            } else {
                                causeRangeDamage(damager,livingEntity,2.0);
                            }
                        }else{
                            causeRangeDamage(damager,livingEntity,1.0);
                        }
                    }
                    damager.playSound(damager.getLocation(), Sound.ENTITY_FISHING_BOBBER_SPLASH, 1.0f, 1.0f);
                }

            }
        }
    }

    public void causeRangeDamage(Player player, LivingEntity entity, double damage) {
        // 在这里模拟造成伤害的过程
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player,entity, EntityDamageEvent.DamageCause.CUSTOM,damage);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }
}
