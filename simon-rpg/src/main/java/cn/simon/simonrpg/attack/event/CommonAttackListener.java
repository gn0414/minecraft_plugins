package cn.simon.simonrpg.attack.event;


import cn.simon.simonrpg.SimonRpg;
import cn.simon.simonrpg.attack.entity.Damage;
import cn.simon.simonrpg.constants.*;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.mob.entity.Mob;
import cn.simon.simonrpg.mob.entity.Skill;
import cn.simon.simonrpg.mob.holder.MobHolder;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.nbt.data.WeaponType;
import cn.simon.simonrpg.stat.entity.Stat;
import cn.simon.simonrpg.stat.holder.StatHolder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static cn.simon.simonrpg.equip.event.SoulLevelListener.*;


public class CommonAttackListener implements Listener {

    private ConcurrentHashMap<UUID, Long> playerCooldowns = new ConcurrentHashMap<>();

    public CommonAttackListener(){
        Logger logger = Logger.getLogger("common-attack");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }


    /**
     * 攻击间隔
     * @param event 实体伤害事件
     */

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player player) {
            //横扫不算间隔
            if (event.getDamage() == ConfigConst.SWEEP_PREFIX) {
                event.setDamage(1.0);
                return;
            }
            if (hasCooldown(player)) {
                event.setCancelled(true);
            } else {
                setCooldown(player, 500); // 10秒的伤害间隔
            }
        }
    }

    /**
     * 优先级极低，伤害来源类型
     * @param event 实体击打实体事件
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void damageHandler(EntityDamageByEntityEvent event){


        //如果前方有取消伤害的事件(交给其他插件限制)
        if (event.isCancelled()) return;

        //获取攻击实体属性
        Entity damager = event.getDamager();
        Entity damagedEntity = event.getEntity();
        Damage damage = null;

        if (damagedEntity instanceof LivingEntity damaged){
            if (damaged instanceof  Player player){
                //非法用户
                boolean creative = isCreative(player);
                if (creative){
                    event.setCancelled(true);
                    return;
                }
                //创造模式
                if (!StatHolder.PLAYER_STAT.containsKey(player)) {
                    event.setCancelled(true);
                    return;
                }

            }

            if (damager instanceof Player player) {
                //非法用户
                if (!StatHolder.PLAYER_STAT.containsKey(player)) {
                    event.setCancelled(true);
                    return;
                }
                boolean canUse = useBefore(player);
                //检验武器是否可使用
                if (!canUse){
                    event.setCancelled(true);
                    return;
                }
                //检验主手左键物品伤害是否有效
                if (!weaponLeftEff(player)) {
                    damage = damage(damager,damaged,false);
                } else {
                    damage = damage(damager, damaged,true);
                }

            } else if (damager instanceof Arrow) {
                ProjectileSource shooter = ((Arrow) damager).getShooter();
                //若为玩家射箭逻辑
                if (shooter instanceof Player shooterPlayer) {
                    damager = shooterPlayer;
                    damage = damage(shooterPlayer, damaged,true);
                }else if (shooter instanceof LivingEntity shooterLive){
                    damager = shooterLive;
                    damage = damage(shooterLive,damaged,false);
                }
            }  else  {
                        if (event.getDamage() >= ConfigConst.SKILL_PREFIX){
                            if (damaged instanceof Player player) {
                                //大于肯定就是技能伤害了
                                List<Skill> skills = MobHolder.mobSkills.get(damager.getName());
                                if (skills != null) {
                                    int no = getFirstNonZeroDigitOfIntegerPart(event.getDamage())-1;
                                    if (no >= 0 && no < skills.size()) {
                                        Skill skill = skills.get(no);
                                        damage = skillDamage(skill, player);
                                    }

                                }
                            }

                        }else damage = damage(damager, damaged,true);
            }
            if (damage != null) {

                //伤害取消则设置事件取消
                if (damage.getIsCancel()){
                    event.setCancelled(true);
                    return;
                }
                //检验是否已经死亡若已经死亡则不显示信息了
                if (damaged.getHealth() == 0)return;
                if (damager instanceof LivingEntity liveDamager){
                    Collection<PotionEffect> activePotionEffects = liveDamager.getActivePotionEffects();
                    for (PotionEffect activePotionEffect : activePotionEffects) {
                        if (activePotionEffect.getType().equals(PotionEffectType.INCREASE_DAMAGE)){
                            int amplifier = activePotionEffect.getAmplifier()+1;
                            damage.setDamage(getNum(damage.getDamage() * (1+  (double) amplifier /10)));
                        }
                    }

                    /**
                     * 范围和鱼竿自定义扣血逻辑
                     */

                    if (event.getDamager() instanceof Player player && event.getEntity() instanceof LivingEntity target) {
                        if (event.getCause().equals(EntityDamageEvent.DamageCause.CUSTOM)){
                            double damageMore = event.getDamage();
                            damage.setDamage(getNum(damage.getDamage()*damageMore));
                            target.damage(damage.getDamage());
                        }
                    }
                    damageMessageAndSteal(liveDamager,damaged, damage);
                }

                event.setDamage(damage.getDamage());
            }
        }
    }


    /**
     * 具体damage的逻辑
     */
    protected static Damage damage(Entity damager, Entity damaged, boolean useWeapon) {
        Damage damage = new Damage();
        if (damager instanceof Player){
            if (StatHolder.PLAYER_STAT.containsKey((Player) damager)){
                //玩家的话就准备计算伤害
                //判断伤害源
                if (damaged instanceof Player){
                    if (StatHolder.PLAYER_STAT.containsKey((Player) damaged)){
                        //玩家对玩家
                        damage = player2PlayerDamage((Player) damager,(Player) damaged,useWeapon);
                    }
                }else if (damaged instanceof LivingEntity liveDamaged){
                    //玩家对有属性生物
                   if (StatHolder.mobStat.containsKey(liveDamaged.getName())){
                        damage = player2MobDamage((Player) damager,StatHolder.mobStat.get(liveDamaged.getName()),liveDamaged,useWeapon);

                   }//玩家对无属性生物
                    else damage = player2EntityDamage((Player) damager,liveDamaged,useWeapon);
                }
            }
        }else if (damager instanceof LivingEntity damageEntity){
            if (damaged instanceof Player){
                if (StatHolder.PLAYER_STAT.containsKey((Player) damaged)){
                    //属性怪物对玩家
                    if (StatHolder.mobStat.containsKey(damageEntity.getName()) ){
                        damage = Mob2Player(damageEntity,(Player) damaged);
                    }else{
                        //无属性怪物对玩家伤害
                        double attack = Objects.requireNonNull(damageEntity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getBaseValue();
                        Stat stat = StatHolder.PLAYER_STAT.get((Player) damaged);
                        String physicalDefense = stat.getPhysicalDefense();

                        double defense = defense(attack, Double.parseDouble(physicalDefense));
                        damage.setDamage(defense);
                        damage.setCritical(false);

                    }
                }
            }
        }
        return damage;
    }



    /**
     * 玩家 有属性实体
     */
    protected static Damage player2MobDamage(Player damager, Mob boss,LivingEntity damaged,boolean useWeapon){
        
        boolean isPure = true;
        Damage damage = new Damage();
        Stat damagerStat = StatHolder.PLAYER_STAT.get(damager);
        Map<String, String> attributes = boss.getAttributes();
        double distance = Double.parseDouble(attributes.getOrDefault(MobConst.ATTACK_DISTANCE, "-1"));

        double realDistance = damager.getLocation().distance(damaged.getLocation());


        //超出距离不生效
        if (distance >= 0){
            if (distance == 0.0 || realDistance > distance){
                damage.setIsCancel(true);
                return damage;
            }
        }

        ItemStack item = damager.getInventory().getItemInMainHand();
        if (item.hasItemMeta()){
            ItemMeta itemMeta = item.getItemMeta();
            Weapon weapon = null;
            try {
                weapon = itemMeta.getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
            }catch (NullPointerException ignored){

            }
            Random random = new Random();
            if (weapon == null || weapon.getDamageType() == null)return damage;
            LinkedHashMap<String, String> mainAttribute = weapon.getMainAttribute();
            LinkedHashMap<String, String> bonusAttribute = weapon.getBonusAttribute();
            String physicalAttack = mainAttribute.get(WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL);
            double randomPhysicalAttack = getRandomPhysicalAttack(physicalAttack);

            if (!useWeapon) randomPhysicalAttack = 1.0;

            //开始计算增量伤害
            randomPhysicalAttack *= Double.parseDouble(weapon.getDamageType().get(DamageConst.PHYSICAL))/100;
            //获取物理伤害加成
            double physicalPlus = Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.PHYSICS_DAMAGE_LABEL,"0.0")) + Double.parseDouble(damagerStat.getPhysicalDamage());
            //获取pve伤害
            double pvePlus = Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.PVE_DAMAGE_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.PVE_DAMAGE_LABEL,"0.0"))+Double.parseDouble(damagerStat.getPveDamage());


            randomPhysicalAttack =  randomPhysicalAttack * (1+physicalPlus/100)  * (1+pvePlus/100);

            //获得目标防御
            double defense = Double.parseDouble(attributes.getOrDefault(MobConst.PHYSICAL_DEFENSE_LABEL,"0.0"));

            double pveDefense = Double.parseDouble(attributes.getOrDefault(MobConst.PVE_DEFENSE_LABEL,"0.0"));

            defense *= pveDefense/100;

            //如果不存在纯粹伤害
            if (!bonusAttribute.containsKey(WeaponBonusAttributeConst.PURE_DAMAGE_LABEL)){
                isPure = false;
                //再获得暴击伤害加成
                double criticalChance = Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.CRITICAL_CHANCE_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.CRITICAL_CHANCE_LABEL,"0.0"))+Double.parseDouble(damagerStat.getCriticalChance());
                double randomChance = random.nextDouble(100.0);
                if (randomChance <= criticalChance){
                    //暴击再算上暴击伤害
                    damage.setCritical(true);

                    double criticalDamage = Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.CRITICAL_DAMAGE_LABEL, "1.0")) + Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_LABEL,"0.0"));

                    criticalDamage -= Double.parseDouble(attributes.getOrDefault(MobConst.CRITICAL_REDUCE_LABEL,"0.0"));

                    if (criticalDamage < 1)criticalDamage = 1;

                    randomPhysicalAttack *= criticalDamage;


                }
                //护甲穿透伤害
                double armor = Double.parseDouble(damagerStat.getArmorPenetration())+Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.ARMOR_PENETRATION_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.ARMOR_PENETRATION_LABEL,"0.0"));
                if (armor < 100.0){
                    defense -= defense*(armor/100);
                    defense = defense(randomPhysicalAttack,defense);
                }else{
                    armor -= 100.0;
                    defense = randomPhysicalAttack * (1+armor/100);

                }
            }else{
                //纯粹伤害
                double pureDamage = Double.parseDouble(bonusAttribute.get(WeaponBonusAttributeConst.PURE_DAMAGE_LABEL));
                if (pureDamage < 100.0){
                    defense = defense((100-pureDamage)/100 * randomPhysicalAttack,defense);
                    defense += (pureDamage/100) * randomPhysicalAttack;
                }else{
                    //无视防御
                    defense = randomPhysicalAttack;
                }
            }

            //闪避
            double miss = Double.parseDouble(attributes.getOrDefault(MobConst.MISS_CHANCE_LABEL,"0.0"));
            miss = miss-(Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.HIT_CHANCE_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.HIT_CHANCE_LABEL,"0.0")));

            if (random.nextDouble(100.0) <= miss){
                damage.setMiss(true);
                damage.setDamage(0.0);
                return damage;
            }
            //伤害缩减
            double reduce = Double.parseDouble(attributes.getOrDefault(MobConst.DAMAGE_REDUCE_LABEL,"0.0"));
            defense -= (reduce/100)*defense;

            damage.setDamage(getNum(defense));
            double steal = Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.LIFE_STEAL_LABEL,"0.0"))+Double.parseDouble(damagerStat.getLifeSteal())+Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.LIFE_STEAL_LABEL,"0.0"));
            damage.setSteal(getNum(defense * steal/100));
        }
        return damage;
    }



    /**
     * 玩家 无属性实体
     * @param damager
     * @param damaged
     * @return
     */

    protected static Damage player2EntityDamage(Player damager,LivingEntity damaged,boolean useWeapon){
        boolean isPure = true;
        Damage damage = new Damage();
        //玩家对无属性怪物
        Stat damagerStat = StatHolder.PLAYER_STAT.get(damager);

        //首先获取伤害者的主手武器物理攻击
        ItemStack item = damager.getInventory().getItemInMainHand();
        if (item.hasItemMeta()){
            ItemMeta itemMeta = item.getItemMeta();
            Weapon weapon = null;
            try {
                weapon = itemMeta.getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
            }catch (NullPointerException ignored){

            }
            Random random = new Random();
            if (weapon == null || weapon.getDamageType() == null)return damage;
            LinkedHashMap<String, String> mainAttribute = weapon.getMainAttribute();
            LinkedHashMap<String, String> bonusAttribute = weapon.getBonusAttribute();
            String physicalAttack = mainAttribute.get(WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL);
            double randomPhysicalAttack = getRandomPhysicalAttack(physicalAttack);
            if (!useWeapon) randomPhysicalAttack = 1.0;
            //开始计算增量伤害
            randomPhysicalAttack *= Double.parseDouble(weapon.getDamageType().get(DamageConst.PHYSICAL))/100;
            //获取物理伤害加成
            double physicalPlus = Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.PHYSICS_DAMAGE_LABEL,"0.0")) + Double.parseDouble(damagerStat.getPhysicalDamage());
            //算上pve伤害
            double pvePlus = Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.PVE_DAMAGE_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.PVE_DAMAGE_LABEL,"0.0"))+Double.parseDouble(damagerStat.getPveDamage());
            //加伤后
            randomPhysicalAttack =  randomPhysicalAttack * (1+physicalPlus/100)  * (1+pvePlus/100);


            //如果不存在纯粹伤害
            if (!bonusAttribute.containsKey(WeaponBonusAttributeConst.PURE_DAMAGE_LABEL)){
                isPure = false;
                //再获得暴击伤害加成
                double criticalChance = Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.CRITICAL_CHANCE_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.CRITICAL_CHANCE_LABEL,"0.0"))+Double.parseDouble(damagerStat.getCriticalChance());
                double randomChance = random.nextDouble(100.0);
                if (randomChance <= criticalChance){
                    //暴击再算上暴击伤害
                    damage.setCritical(true);
                    randomPhysicalAttack *= Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.CRITICAL_DAMAGE_LABEL, "1.0")) + Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_LABEL,"0.0"));
                }

                //护甲穿透伤害
                double armor = Double.parseDouble(damagerStat.getArmorPenetration())+Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.ARMOR_PENETRATION_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.ARMOR_PENETRATION_LABEL,"0.0"));
                randomPhysicalAttack = randomPhysicalAttack * (1+armor/100);
            }


            double steal = Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.LIFE_STEAL_LABEL,"0.0"))+Double.parseDouble(damagerStat.getLifeSteal())+Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.LIFE_STEAL_LABEL,"0.0"));
            damage.setSteal(getNum(randomPhysicalAttack * steal/100));
            damage.setDamage(getNum(randomPhysicalAttack));

        }

        return damage;
    }

    /**
     * 属性小怪/玩家
     * @param mob
     * @param player
     * @return
     */

    protected static Damage Mob2Player(Entity mob,Player player){
        Damage res = new Damage();
        //获得玩家属性
        Stat stat = StatHolder.PLAYER_STAT.get(player);
        //获取mob的属性
        Map<String, String> attributes = StatHolder.mobStat.get(mob.getName()).getAttributes();
        //物理攻击
        double damage = Double.parseDouble(attributes.getOrDefault(MobConst.PHYSICAL_ATTACK_LABEL,"0.0"));

        //来算pvp伤害
        double pvpPlus = Double.parseDouble(attributes.getOrDefault(MobConst.PVP_DAMAGE_LABEL,"0.0"));


        //来算加伤
        damage = damage*(1+pvpPlus/100);


        //获得暴击几率
        double criticalChance = Double.parseDouble(attributes.getOrDefault(MobConst.CRITICAL_CHANCE_LABEL,"0.0"));

        Random random = new Random();
        if (random.nextDouble(100) <= criticalChance){
            damage *= Double.parseDouble(attributes.getOrDefault(MobConst.CRITICAL_DAMAGE_LABEL,"1.0"));
            res.setCritical(true);

        }



        //来算防御
        double defense = Double.parseDouble(stat.getPhysicalDefense());

        double pveDefense = Double.parseDouble(stat.getPveDefense());

        defense = defense*(1+pveDefense/100);



        //暴击伤害得到了来看护甲穿透
        double armorPenetration = Double.parseDouble(attributes.getOrDefault(MobConst.ARMOR_PENETRATION_LABEL,"0.0"));
        if (armorPenetration <= 100) {
            defense -= defense*armorPenetration/100;
            damage = defense(damage,defense);
        }else{
            armorPenetration = armorPenetration-100;
            damage = damage * (1+armorPenetration/100);

        }


        //伤害算完了算闪避
        double hitChance = Double.parseDouble(attributes.getOrDefault(MobConst.HIT_CHANCE_LABEL,"0.0"));
        hitChance = Double.parseDouble(stat.getMissChance()) - hitChance;
        if (random.nextDouble(100) <= hitChance){
            //闪避成功
            res.setMiss(true);
        }
        //格挡
        double blockChance = Double.parseDouble(stat.getBlockChance());
        if (random.nextDouble(100) < blockChance){
            res.setBlock(true);
            res.setBlockSize(Double.parseDouble(stat.getDamageReduce()));
            damage = damage*(1-(Double.parseDouble(stat.getDamageReduce())/100));
        }
        //吸血
        res.setDamage(getNum(damage));
        res.setSteal(getNum(damage * (Double.parseDouble(attributes.getOrDefault(MobConst.LIFE_STEAL_LABEL,"0.0"))/100)));
        return res;
    }

    /**
     *玩家 玩家/boss逻辑
     * @param damager 玩家
     * @param damaged 怪物
     * @return 伤害
     */
    protected static Damage player2PlayerDamage(Player damager,Player damaged,boolean useWeapon){
        boolean isPure = true;
        Damage damage = new Damage();
        //玩家对玩家
        Stat damagerStat = StatHolder.PLAYER_STAT.get(damager);
        Stat damagedStat = StatHolder.PLAYER_STAT.get(damaged);

        //首先获取伤害者的主手武器物理攻击
        ItemStack item = damager.getInventory().getItemInMainHand();
        if (item.hasItemMeta()){
            ItemMeta itemMeta = item.getItemMeta();
            Weapon weapon = null;
            try {
                weapon = itemMeta.getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
            }catch (NullPointerException e){
                return damage;
            }
            Random random = new Random();

            if (weapon == null || weapon.getDamageType() == null)return damage;
            LinkedHashMap<String, String> mainAttribute = weapon.getMainAttribute();
            LinkedHashMap<String, String> bonusAttribute = weapon.getBonusAttribute();


            String physicalAttack = mainAttribute.get(WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL);
            double randomPhysicalAttack = getRandomPhysicalAttack(physicalAttack);
            //开始计算增量伤害

            if (!useWeapon) randomPhysicalAttack = 1.0;

            randomPhysicalAttack *= Double.parseDouble(weapon.getDamageType().get(DamageConst.PHYSICAL))/100;

            //获取物理伤害加成
            double physicalPlus = Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.PHYSICS_DAMAGE_LABEL,"0.0")) + Double.parseDouble(damagerStat.getPhysicalDamage());

            //算上pvp伤害
            double pvpPlus = Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.PVP_DAMAGE_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.PVP_DAMAGE_LABEL,"0.0"))+Double.parseDouble(damagerStat.getPvpDamage());

            //算上乘以的伤害

            randomPhysicalAttack =  randomPhysicalAttack * (1+physicalPlus/100)  * (1+pvpPlus/100);

            //如果不存在纯粹伤害
            if (!bonusAttribute.containsKey(WeaponBonusAttributeConst.PURE_DAMAGE_LABEL)){
                isPure = false;
                //再获得暴击伤害加成
                double criticalChance = Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.CRITICAL_CHANCE_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.CRITICAL_CHANCE_LABEL,"0.0"))+Double.parseDouble(damagerStat.getCriticalChance());
                double randomChance = random.nextDouble(100.0);
                if (randomChance <= criticalChance){
                    //暴击再算上暴击伤害
                    damage.setCritical(true);
                    randomPhysicalAttack *= Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.CRITICAL_DAMAGE_LABEL, "1.0")) + Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_LABEL,"0.0"));
                }
            }


            double pvpDefnese = Double.parseDouble(damagedStat.getPvpDefense());

            double armor = Double.parseDouble(damagerStat.getArmorPenetration())+Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.ARMOR_PENETRATION_LABEL,"0.0"))+Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.ARMOR_PENETRATION_LABEL,"0.0"));

            double defense = Double.parseDouble(damagedStat.getPhysicalDefense());


            defense = defense * (1+pvpDefnese/100);

            if (!isPure){
                if (armor < 100.0){
                    defense -= defense*(armor/100);
                    defense = defense(randomPhysicalAttack,defense);
                }else{
                    armor -= 100.0;
                    defense = randomPhysicalAttack * (1+armor/100);
                }
            }else {
                //纯粹伤害
                double pureDamage = Double.parseDouble(bonusAttribute.get(WeaponBonusAttributeConst.PURE_DAMAGE_LABEL));
                if (pureDamage < 100.0){
                    defense = defense((100-pureDamage)/100 * randomPhysicalAttack,defense);
                    defense += (pureDamage/100) * randomPhysicalAttack;
                }else{
                    //无视防御
                    defense = randomPhysicalAttack;
                }
            }
            //加伤算完了,我们可以开始算减伤了
            //首先是正常物理防御
            //护甲穿透
            //然后再是闪避几率
            double randomMiss = random.nextDouble(100);

            if (randomMiss <= (Double.parseDouble(damagedStat.getDamageReduce()) - Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.HIT_CHANCE_LABEL,"0.0")))){
                //闪避成功了
                damage.setMiss(true);
            }

            double randomBlock = random.nextDouble(100);
            if (randomBlock <= (Double.parseDouble(damagedStat.getBlockChance()))){
                //格挡成功了;
                damage.setBlock(true);

                damage.setBlockSize(Double.parseDouble(damagedStat.getDamageReduce()));
                //减免伤害
                defense = defense * (1-(Double.parseDouble(damagedStat.getDamageReduce())/100));
            }
            //吸血计算
            double steal = Double.parseDouble(bonusAttribute.getOrDefault(WeaponBonusAttributeConst.LIFE_STEAL_LABEL,"0.0"))+Double.parseDouble(damagerStat.getLifeSteal())+Double.parseDouble(mainAttribute.getOrDefault(WeaponMainAttributeConst.LIFE_STEAL_LABEL,"0.0"));
            damage.setSteal(getNum(defense * steal/100));
            damage.setDamage(getNum(defense));

        }
        return damage;
    }

    protected static double getRandomPhysicalAttack(String physicalAttack){
        Random random = new Random();
        String[] split = physicalAttack.split(",");
        return Double.parseDouble(split[0])+random.nextDouble(Double.parseDouble(split[1])-Double.parseDouble(split[0]));
    }

    protected static double defense(double damage,double defense){
        damage = damage * (1-(defense/(defense+damage * 0.1)));
        return getNum(damage);
    }

    static double getNum(double damage){
        //保留2位小数
        double score = new BigDecimal(damage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //不足两位则补0
        DecimalFormat decimalFormat = new DecimalFormat("0.0#");
        return Double.parseDouble(decimalFormat.format(damage));
    }

    /**
     *
     * @param damager 伤害者
     * @param damaged 被伤害者
     * @param damage 伤害
     */
    private void damageMessageAndSteal(LivingEntity damager,LivingEntity damaged,Damage damage){

        if (damaged instanceof Player damagedPlayer) {

            Collection<PotionEffect> activePotionEffects = damagedPlayer.getActivePotionEffects();

            for (PotionEffect effect : activePotionEffects ) {
                if (effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE)) {
                    int amplifier = effect.getAmplifier(); // 获取效果等级
                    damage.setDamage(getNum(damage.getDamage() * (1- 0.2*(amplifier+1))));
                }
            }
            if (damage.getMiss()) {
                damagedPlayer.sendMessage(ChatColor.YELLOW + "*** " + ChatColor.GOLD + "你躲开了 " + ColorConst.TITLE_COLOR + damager.getName() + ChatColor.GOLD + " 的攻击! " + ChatColor.YELLOW + "***");
                damage.setDamage(0.0);
            } else if (damage.getBlock()) {
                damagedPlayer.sendMessage(ChatColor.GOLD + "*** 你格挡了 " + ColorConst.TITLE_COLOR + damage.getBlockSize() + "%" + ChatColor.GOLD + " 的来自 " + damager.getName() + ChatColor.GOLD + " 的攻击! ***");
                damagedPlayer.sendMessage(ChatColor.RED + "*** " + ColorConst.TITLE_COLOR + damager.getName() + ChatColor.RED + " 给你造成了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.RED + " 伤害 ***");
                damagedPlayer.playSound(damagedPlayer.getLocation(), Sound.BLOCK_ANVIL_PLACE, 1.0f, 1.0f);
            } else if (damage.getCritical()) {
                damagedPlayer.sendMessage(ChatColor.LIGHT_PURPLE + "*** 被暴击了! " + ColorConst.TITLE_COLOR + damager.getName() + ChatColor.LIGHT_PURPLE + " 给你造成了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.LIGHT_PURPLE + " 伤害 ***");
                damagedPlayer.playSound(damagedPlayer.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            } else {
                damagedPlayer.sendMessage(ChatColor.RED + "*** " + ColorConst.TITLE_COLOR + damager.getName() + ChatColor.RED + " 给你造成了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.RED + " 伤害 ***");
            }
        }

        if (damager instanceof Player damagerPlayer) {

            if (damage.getMiss()) {
                damagerPlayer.sendMessage(ChatColor.YELLOW + "*** " + damaged.getName() + ChatColor.YELLOW + " 躲开了你的攻击! ***");
                sendActionBar(damagerPlayer,ChatColor.YELLOW + "*** " + damaged.getName() + ChatColor.YELLOW + " 躲开了你的攻击! ***");
            } else if (damage.getBlock()) {
                if (damaged instanceof Player damagedPlayer) {
                    String damageReduce = StatHolder.PLAYER_STAT.get(damagedPlayer).getDamageReduce();
                    damagerPlayer.sendMessage(ChatColor.GOLD + "*** " + damagedPlayer.getName() + ChatColor.GOLD + " 格挡回了 " + ChatColor.GRAY + damageReduce + "% " + ChatColor.GOLD + "的攻击 ***");
                    sendActionBar(damagedPlayer,ChatColor.YELLOW + "*** 你打出了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.YELLOW + " 伤害 伤害对象: " + ColorConst.TITLE_COLOR + damaged.getName() + ChatColor.YELLOW + ". ***");
                    damagerPlayer.sendMessage(ChatColor.YELLOW + "*** 你打出了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.YELLOW + " 伤害 伤害对象: " + ColorConst.TITLE_COLOR + damaged.getName() + ChatColor.YELLOW + ". ***");
                }

            } else if (damage.getCritical()) {
                damagerPlayer.sendMessage(ChatColor.YELLOW + "*** 暴击! 你打出了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.YELLOW + " 伤害 伤害对象: " + ColorConst.TITLE_COLOR + damaged.getName() + ChatColor.YELLOW + ". ***");
                sendActionBar(damagerPlayer,ChatColor.YELLOW + "*** 暴击! 你打出了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.YELLOW + " 伤害 伤害对象: " + ColorConst.TITLE_COLOR + damaged.getName() + ChatColor.YELLOW + ". ***");
                damagerPlayer.playSound(damagerPlayer.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            } else {
                damagerPlayer.sendMessage(ChatColor.YELLOW + "*** 你打出了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.YELLOW + " 伤害 伤害对象: " + ColorConst.TITLE_COLOR + damaged.getName() + ChatColor.YELLOW + ". ***");
                sendActionBar(damagerPlayer,ChatColor.YELLOW + "*** 你打出了 " + ColorConst.TITLE_COLOR + damage.getDamage() + ChatColor.YELLOW + " 伤害 伤害对象: " + ColorConst.TITLE_COLOR + damaged.getName() + ChatColor.YELLOW + ". ***");
            }
            //处理吸血
            AttributeInstance maxHealth = damagerPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH);
            //获得到最大血量
            assert maxHealth != null;
            if ((maxHealth.getValue() - damagerPlayer.getHealth()) <= damage.getSteal()) {
                damagerPlayer.setHealth(maxHealth.getValue());
            } else {
                damagerPlayer.setHealth(damage.getSteal() + damagerPlayer.getHealth());
            }
        }
    }



    /**
     *
     * @param player 玩家
     * @return 是否可以使用武器
     */
    private static boolean useBefore(Player player){

        if (!soulBind(player, player.getInventory().getItemInMainHand())) {
            player.sendMessage(soulBindMessage());
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            return false;
        }

        if (!checkLevel(player, player.getInventory().getItemInMainHand())) {
            player.sendMessage(levelMessage());
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            return false;
        }
        return true;
    }


    private boolean hasCooldown(Player player) {
        Long cooldownTime = playerCooldowns.get(player.getUniqueId());
        return cooldownTime != null && cooldownTime > System.currentTimeMillis();
    }

    private void setCooldown(Player player, long cooldownTime) {
        UUID playerId = player.getUniqueId();
        playerCooldowns.compute(playerId, (key, oldValue) -> {
            if (oldValue == null || oldValue < System.currentTimeMillis()) {
                return System.currentTimeMillis() + cooldownTime;
            } else {
                return oldValue + cooldownTime; // 更新冷却时间
            }
        });
    }

    /**
     * 发送actionbar
     * @param player 玩家
     * @param message 消息
     */
    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }
    /**
     *
     * @param player 玩家
     * @return 判断是否处于创造模式
     */
    private static boolean isCreative(Player player){
        return player.getGameMode().equals(GameMode.CREATIVE);
    }

    private static boolean weaponLeftEff(Player player){
        boolean isEff = false;

        switch (player.getInventory().getItemInMainHand().getType()) {
            case WOODEN_AXE,STONE_AXE,IRON_AXE,GOLDEN_AXE,DIAMOND_AXE
                    ,WOODEN_SWORD,STONE_SWORD,IRON_SWORD,GOLDEN_SWORD,DIAMOND_SWORD
                    ,FISHING_ROD
                    -> isEff = true;
        }
        return isEff;
    }

    /**
     *
     * @param skill 技能
     * @param player 玩家
     * @return 技能伤害
     */
    private Damage skillDamage(Skill skill, Player player) {
        Stat stat = StatHolder.PLAYER_STAT.get(player);
        Damage damageRes = new Damage();
        Random random = new Random();

        double damage = 0;
        Map<String, Double> attributes = skill.getAttributes();

        double physical = attributes.getOrDefault(SkillConst.PHYSICAL_ATTACK_LABEL, 0.0);
        double pure = attributes.getOrDefault(SkillConst.PURE_DAMAGE, 0.0);
        double criticalDamage = attributes.getOrDefault(SkillConst.CRITICAL_DAMAGE_LABEL, 1.0);
        double criticalChance = attributes.getOrDefault(SkillConst.CRITICAL_CHANCE_LABEL, 0.0);
        double hitChance = attributes.getOrDefault(SkillConst.HIT_CHANCE, 0.0);
        double armor = attributes.getOrDefault(SkillConst.ARMOR_PENETRATION_LABEL, 0.0);

        // 玩家防御
        double defense = Double.parseDouble(stat.getPhysicalDefense());
        double pveDefense = Double.parseDouble(stat.getPveDefense());
        defense += defense * (pveDefense / 100);

        damage = physical;
        if (pure == 0) {
            if (random.nextDouble() * 100 <= criticalChance) {
                damage *= criticalDamage;
                damageRes.setCritical(true);
            }
            // 护甲穿透
            if (armor <= 100) {
                defense *= (1 - armor / 100);
            }
            damage = defense(damage, defense);
        } else {
            if (pure > 100) pure = 100.0;
            double pureDamage = damage * (pure / 100);
            damage = defense(damage * (1 - pure / 100), defense);
            damage += pureDamage;
        }


        double missChance = Double.parseDouble(stat.getMissChance()) - hitChance;
        double blockChance = Double.parseDouble(stat.getBlockChance());
        double blockSize = Double.parseDouble(stat.getDamageReduce());
        if (random.nextDouble() * 100 <= missChance) {
            damageRes.setMiss(true);
        }
        if (random.nextDouble() * 100 <= blockChance) {
            damageRes.setBlock(true);
            damageRes.setBlockSize(blockSize);
            damage -= damage * (blockSize / 100);
        }
        damageRes.setDamage(getNum(damage));
        return damageRes;
    }

    /**
     * 找到需求技能
     * @param number 伤害值
     * @return int
     */

    public static int getFirstNonZeroDigitOfIntegerPart(double number) {
        // 将 double 类型数字转换为字符串
        String numberStr = Double.toString(number);

        // 查找小数点的位置
        int decimalIndex = numberStr.indexOf('.');

        // 如果找到小数点，则截取小数点之前的部分
        String integerPartStr = decimalIndex != -1 ? numberStr.substring(0, decimalIndex) : numberStr;

        // 从整数部分的开头开始遍历，找到第一个非零数字字符
        for (int i = 0; i < integerPartStr.length(); i++) {
            char c = integerPartStr.charAt(i);
            if (Character.isDigit(c) && c != '0') {
                return Character.getNumericValue(c);
            }
        }

        return -1;  // 如果没有找到非零数字字符，则返回 -1
    }
}
