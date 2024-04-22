package cn.simon.simonrpg.equip.builder;
import cn.simon.simonrpg.constants.*;
import cn.simon.simonrpg.equip.entity.Equipment;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.equip.entity.WeaponSouvenir;
import cn.simon.simonrpg.nbt.data.EquipmentType;
import cn.simon.simonrpg.nbt.data.WeaponSouvenirType;
import cn.simon.simonrpg.nbt.data.WeaponType;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.util.SimonRpgShieldUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

@SuppressWarnings("all")
public class EquipmentBuilder {

    public static final PersistentDataType WEAPON_TYPE = new WeaponType();

    public static final PersistentDataType EQUIPMENT_TYPE = new EquipmentType();

    public static final PersistentDataType WEAPONSOUVENIR_TYPE = new WeaponSouvenirType();

    public static List<ItemStack> makeWeapons(List<Weapon> weaponsData){
        List<ItemStack> weapons = new ArrayList<>();
        for (Weapon weaponsDatum : weaponsData) {
            Map<String,Double> allAttribute = new HashMap<>();
            ItemStack item = getMetaItem(weaponsDatum.getType(),weaponsDatum.getCategory());
            setName(weaponsDatum.getName(),weaponsDatum.getType(),weaponsDatum.getCategory(),
                    weaponsDatum.getDamageType(),weaponsDatum.getSoulBind(),weaponsDatum.getLevel(),item);
            setMain(weaponsDatum.getMainAttribute(),item,allAttribute);
            setBonus(weaponsDatum.getBonusAttribute(),item,allAttribute);
            setGem(weaponsDatum.getGem(),item);
            setEnchant(weaponsDatum.getEnchant(),item);
            setRune(weaponsDatum.getRune(),item);
            setSkill(weaponsDatum.getSkill(), item);
            setNBT(weaponsDatum,item);

            ItemMeta itemMeta = item.getItemMeta();
            //不可破坏
            itemMeta.setUnbreakable(true);
            //隐藏属性
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);
            weapons.add(item);
            allAttribute.clear();
        }
        return weapons;
    }



    public static ItemStack makeWeapon(Weapon weaponsDatum){
            Map<String,Double> allAttribute = new HashMap<>();
            ItemStack item = getMetaItem(weaponsDatum.getType(),weaponsDatum.getCategory());
            setName(weaponsDatum.getName(),weaponsDatum.getType(),weaponsDatum.getCategory(),
                    weaponsDatum.getDamageType(),weaponsDatum.getSoulBind(),weaponsDatum.getLevel(),item);
            setMain(weaponsDatum.getMainAttribute(),item,allAttribute);
            setBonus(weaponsDatum.getBonusAttribute(),item,allAttribute);
            setGem(weaponsDatum.getGem(),item);
            setEnchant(weaponsDatum.getEnchant(),item);
            setRune(weaponsDatum.getRune(),item);
            setSkill(weaponsDatum.getSkill(), item);
            setNBT(weaponsDatum,item);
            ItemMeta itemMeta = item.getItemMeta();
            //不可破坏
            itemMeta.setUnbreakable(true);
            //隐藏属性
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);

            allAttribute.clear();
            return item;
    }

    public static ItemStack makeWeaponSouvenir(WeaponSouvenir weaponSouvenirsDatum){
        Map<String,Double> allAttribute = new HashMap<>();
        ItemStack item = getMetaItem(weaponSouvenirsDatum.getType());
        setName(weaponSouvenirsDatum.getName(),weaponSouvenirsDatum.getType(),weaponSouvenirsDatum.getDescription(),
                weaponSouvenirsDatum.getCategory(), weaponSouvenirsDatum.getDamageType(),weaponSouvenirsDatum.getSoulBind(),weaponSouvenirsDatum.getLevel(),
                item,weaponSouvenirsDatum.getCustomizeColor());
        setMain(weaponSouvenirsDatum.getMainAttribute(),item,allAttribute);
        setBonus(weaponSouvenirsDatum.getBonusAttribute(),item,allAttribute);
        setGem(weaponSouvenirsDatum.getGem(),item);
        setEnchant(weaponSouvenirsDatum.getEnchant(),item);
        setRune(weaponSouvenirsDatum.getRune(),item);
        setSkill(weaponSouvenirsDatum.getSkill(),item);
        setNBT(weaponSouvenirsDatum,item);
        ItemMeta itemMeta = item.getItemMeta();
        //不可破坏
        itemMeta.setUnbreakable(true);
        //隐藏属性
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        allAttribute.clear();
        return item;
    }

    public static ItemStack makeEquipment(Equipment equipmentsDatum){
        Map<String,Double> allAttribute = new HashMap<>();
        ItemStack item = getMetaItem(equipmentsDatum.getType(),equipmentsDatum.getCategory());
        setName(equipmentsDatum.getName(),equipmentsDatum.getType(),equipmentsDatum.getCategory(),
                equipmentsDatum.getSoulBind(),equipmentsDatum.getLevel(),item
        );
        setMain(equipmentsDatum.getMainAttribute(),item,true,equipmentsDatum.getType(),allAttribute);
        setBonus(equipmentsDatum.getBonusAttribute(),item,true,equipmentsDatum.getType(),allAttribute);


        if (equipmentsDatum.getGem() != null){
            setGem(equipmentsDatum.getGem(),item);
            setEnchant(equipmentsDatum.getEnchant(),item);
            setRune(equipmentsDatum.getRune(),item);
        }
        setNBT(equipmentsDatum,item);
        ItemMeta itemMeta = item.getItemMeta();
        //不可破坏
        itemMeta.setUnbreakable(true);
        //隐藏属性
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
        item.setItemMeta(itemMeta);
        allAttribute.clear();
        return item;
    }

    public static List<ItemStack> makeWeaponSouvenirs(List<WeaponSouvenir> weaponSouvenirsData ){

        List<ItemStack> weaponSouvenir = new ArrayList<>();
        for (WeaponSouvenir weaponSouvenirsDatum : weaponSouvenirsData) {
            Map<String,Double> allAttribute = new HashMap<>();
            ItemStack item = getMetaItem(weaponSouvenirsDatum.getType());
            setName(weaponSouvenirsDatum.getName(),weaponSouvenirsDatum.getType(),weaponSouvenirsDatum.getDescription(),
                    weaponSouvenirsDatum.getCategory(),
                    weaponSouvenirsDatum.getDamageType(),weaponSouvenirsDatum.getSoulBind(),weaponSouvenirsDatum.getLevel(),
                    item,weaponSouvenirsDatum.getCustomizeColor());
            setMain(weaponSouvenirsDatum.getMainAttribute(),item,allAttribute);
            setBonus(weaponSouvenirsDatum.getBonusAttribute(),item,allAttribute);
            if (weaponSouvenirsDatum.getGem() != null){
                setGem(weaponSouvenirsDatum.getGem(),item);
                setEnchant(weaponSouvenirsDatum.getEnchant(),item);
                setRune(weaponSouvenirsDatum.getRune(),item);
                setSkill(weaponSouvenirsDatum.getSkill(),item);
            }
            setNBT(weaponSouvenirsDatum,item);

            ItemMeta itemMeta = item.getItemMeta();
            //不可破坏
            itemMeta.setUnbreakable(true);
            //隐藏属性
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);
            weaponSouvenir.add(item);
            allAttribute.clear();
        }
        return weaponSouvenir;
    }

    public static List<ItemStack> makeEquipments(List<Equipment> equipmentsData){

        List<ItemStack> equipment = new ArrayList<>();
        for (Equipment equipmentsDatum : equipmentsData) {
            Map<String,Double> allAttribute = new HashMap<>();
            ItemStack item = getMetaItem(equipmentsDatum.getType(),equipmentsDatum.getCategory());
            setName(equipmentsDatum.getName(),equipmentsDatum.getType(),equipmentsDatum.getCategory(),
                    equipmentsDatum.getSoulBind(),equipmentsDatum.getLevel(),item
            );
            setMain(equipmentsDatum.getMainAttribute(),item,true,equipmentsDatum.getType(),allAttribute);
            setBonus(equipmentsDatum.getBonusAttribute(),item,true,equipmentsDatum.getType(),allAttribute);
            setGem(equipmentsDatum.getGem(),item);
            setEnchant(equipmentsDatum.getEnchant(),item);
            setRune(equipmentsDatum.getRune(),item);
            setNBT(equipmentsDatum,item);
            //不可破坏
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setUnbreakable(true);
             //隐藏属性
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_UNBREAKABLE);
            item.setItemMeta(itemMeta);
            equipment.add(item);
            allAttribute.clear();
        }
        return equipment;
    }



    private static void setNBT(Weapon weapon,ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(),WEAPON_TYPE,weapon);
        item.setItemMeta(itemMeta);
    }

    private static void setNBT(Equipment equipment,ItemStack item) {

        ItemMeta itemMeta = item.getItemMeta();

        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        persistentDataContainer.set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(),EQUIPMENT_TYPE,equipment);

        item.setItemMeta(itemMeta);
    }

    private static void setNBT(WeaponSouvenir weaponSouvenir,ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(),WEAPONSOUVENIR_TYPE,weaponSouvenir);
        item.setItemMeta(itemMeta);
    }


    public static void setBonus(Map<String,String> bonusAttribute,ItemStack itemStack,Map<String,Double> allAttribute) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> bonusLore = itemMeta.getLore();
        if (bonusAttribute == null)return;
        else{
            for (String attribute : bonusAttribute.keySet()) {
                switch (attribute) {
                    case WeaponBonusAttributeConst.BLOCK_CHANCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.BLOCK_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.DAMAGE_REDUCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.DAMAGE_REDUCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.HIT_CHANCE_LABEL, WeaponBonusAttributeConst.HIT_CHANCE_NAME -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.HIT_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.PVE_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.PVE_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.PVP_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.PVP_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }

                    case WeaponBonusAttributeConst.FIRE_CHANCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.FIRE_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.ATTACK_RANGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.ATTACK_RANGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.SWEEP_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.SWEEP_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.LIFE_STEAL_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.LIFE_STEAL_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.ARMOR_PENETRATION_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.ARMOR_PENETRATION_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.MAX_HEALTH_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.MAX_HEALTH_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                        if (allAttribute.containsKey(WeaponMainAttributeConst.MAX_HEALTH_LABEL)) {
                            itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                    UUID.randomUUID(),
                                    "最大血量",
                                    allAttribute.get(WeaponMainAttributeConst.MAX_HEALTH_LABEL) * 2 +
                                            Double.valueOf(bonusAttribute.get(WeaponBonusAttributeConst.MAX_HEALTH_LABEL)) / 100 * allAttribute.get(WeaponMainAttributeConst.MAX_HEALTH_LABEL),
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    EquipmentSlot.HAND));
                            allAttribute.remove(WeaponMainAttributeConst.MAX_HEALTH_LABEL);
                        }
                    }
                    case WeaponBonusAttributeConst.CRITICAL_CHANCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.CRITICAL_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_LABEL-> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "х"
                        );
                    }
                    case WeaponBonusAttributeConst.PHYSICS_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.PHYSICS_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.PURE_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.PURE_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case WeaponBonusAttributeConst.MOVE_SPEED_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.MOVE_SPEED_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                        if (allAttribute.containsKey(WeaponMainAttributeConst.MOVE_SPEED_LABEL)) {
                            itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                    UUID.randomUUID(),
                                    "移动速度",
                                    allAttribute.get(WeaponMainAttributeConst.MOVE_SPEED_LABEL) * (Double.valueOf(bonusAttribute.get(attribute)) / 100) + allAttribute.get(WeaponMainAttributeConst.MOVE_SPEED_LABEL) * 2,
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    EquipmentSlot.HAND));
                            allAttribute.remove(WeaponMainAttributeConst.MOVE_SPEED_LABEL);
                        }

                    }
                }
            }
            //            移动速度扫尾
            if (allAttribute.get(WeaponMainAttributeConst.MOVE_SPEED_LABEL) != null){
                itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                        UUID.randomUUID(),
                        "移动速度",
                        allAttribute.get(WeaponMainAttributeConst.MOVE_SPEED_LABEL),
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND));
            }
            //          最大血量扫尾
            if (allAttribute.get(WeaponMainAttributeConst.MAX_HEALTH_LABEL) != null){
                itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                        UUID.randomUUID(),
                        "最大血量",
                        allAttribute.get(WeaponMainAttributeConst.MAX_HEALTH_LABEL),
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlot.HAND));
            }
            bonusLore.add("");
        }
        itemMeta.setLore(bonusLore);
        itemStack.setItemMeta(itemMeta);
    }


    public static void setBonus(Map<String,String> bonusAttribute,ItemStack itemStack,boolean isEquipment,String type,Map<String,Double> allAttribute) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> bonusLore = itemMeta.getLore();

        if (bonusAttribute == null)return;
        else{
            for (String attribute : bonusAttribute.keySet()) {
                switch (attribute) {
                    case EquipmentBonusAttributeConst.BLOCK_CHANCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.BLOCK_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case EquipmentBonusAttributeConst.DAMAGE_REDUCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.DAMAGE_REDUCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }

                    case EquipmentBonusAttributeConst.PVE_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.PVE_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case EquipmentBonusAttributeConst.PVP_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.PVP_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }

                    case EquipmentBonusAttributeConst.PVE_DEFENSE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.PVE_DEFENSE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case EquipmentBonusAttributeConst.PVP_DEFENSE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.PVP_DEFENSE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }


                    case EquipmentBonusAttributeConst.LIFE_STEAL_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.LIFE_STEAL_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case EquipmentBonusAttributeConst.ARMOR_PENETRATION_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.ARMOR_PENETRATION_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case EquipmentBonusAttributeConst.MAX_HEALTH_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.MAX_HEALTH_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                        if (allAttribute.containsKey(EquipMainAttributeConst.MAX_HEALTH_LABEL)){
                            switch (type) {
                                case EquipmentConst.HEAD -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "最大血量",
                                            allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL) * 2 +
                                                    Double.valueOf(bonusAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL)) / 100
                                                            * allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.HEAD
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MAX_HEALTH_LABEL);
                                }
                                case EquipmentConst.CHEST -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "最大血量",
                                            allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL) * 2 +
                                                    Double.valueOf(bonusAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL)) / 100
                                                            * allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.CHEST
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MAX_HEALTH_LABEL);
                                }
                                case EquipmentConst.WING -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "最大血量",
                                            allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL) * 2 +
                                                    Double.valueOf(bonusAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL)) / 100
                                                            * allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.CHEST
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MAX_HEALTH_LABEL);
                                }
                                case EquipmentConst.LEG -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "最大血量",
                                            allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL) * 2 +
                                                    Double.valueOf(bonusAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL)) / 100
                                                            * allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.LEGS
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MAX_HEALTH_LABEL);
                                }
                                case EquipmentConst.FOOT -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "最大血量",
                                            allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL) * 2 +
                                                    Double.valueOf(bonusAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL)) / 100
                                                            * allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.FEET
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MAX_HEALTH_LABEL);
                                }
                                case EquipmentConst.SHIELD -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "最大血量",
                                            allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL) * 2 +
                                                    Double.valueOf(bonusAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL)) / 100
                                                            * allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.OFF_HAND
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MAX_HEALTH_LABEL);
                                }
                            }
                        }
                    }
                    case EquipmentBonusAttributeConst.CRITICAL_CHANCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.CRITICAL_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case EquipmentBonusAttributeConst.PHYSICS_DAMAGE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.PHYSICS_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }

                    case EquipmentBonusAttributeConst.MISS_CHANCE_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.MISS_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                    }
                    case EquipmentBonusAttributeConst.MOVE_SPEED_LABEL -> {
                        bonusLore.add(
                                ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + EquipmentBonusAttributeConst.MOVE_SPEED_NAME + ": " + ChatColor.GREEN + "+" + bonusAttribute.get(attribute) + "%"
                        );
                        if (allAttribute.containsKey(EquipMainAttributeConst.MOVE_SPEED_LABEL)){
                            switch (type) {
                                case EquipmentConst.HEAD -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "移动速度",
                                            allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * (Double.valueOf(bonusAttribute.get(attribute)) / 100)
                                                    + allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * 2,
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.HEAD
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MOVE_SPEED_LABEL);
                                }
                                case EquipmentConst.CHEST -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "移动速度",
                                            allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * (Double.valueOf(bonusAttribute.get(attribute)) / 100)
                                                    + allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * 2,
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.CHEST
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MOVE_SPEED_LABEL);
                                }
                                case EquipmentConst.WING -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "移动速度",
                                            allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * (Double.valueOf(bonusAttribute.get(attribute)) / 100)
                                                    + allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * 2,
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.CHEST
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MOVE_SPEED_LABEL);
                                }
                                case EquipmentConst.LEG -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "移动速度",
                                            allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * (Double.valueOf(bonusAttribute.get(attribute)) / 100)
                                                    + allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * 2,
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.LEGS
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MOVE_SPEED_LABEL);
                                }
                                case EquipmentConst.FOOT -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "移动速度",
                                            allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * (Double.valueOf(bonusAttribute.get(attribute)) / 100)
                                                    + allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * 2,
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.FEET
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MOVE_SPEED_LABEL);
                                }
                                case EquipmentConst.SHIELD -> {
                                    itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                            UUID.randomUUID(),
                                            "移动速度",
                                            allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * (Double.valueOf(bonusAttribute.get(attribute)) / 100)
                                                    + allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) * 2,
                                            AttributeModifier.Operation.ADD_NUMBER,
                                            EquipmentSlot.OFF_HAND
                                    ));
                                    allAttribute.remove(EquipMainAttributeConst.MOVE_SPEED_LABEL);
                                }
                            }
                        }
                    }
                }
            }
            //            移动速度扫尾
            if (allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL) != null){
                switch (type){
                    case EquipmentConst.HEAD -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                UUID.randomUUID(),
                                "移动速度",
                                allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.HEAD));
                    }
                    case EquipmentConst.CHEST -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                UUID.randomUUID(),
                                "移动速度",
                                allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.CHEST));
                    }
                    case EquipmentConst.WING -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                UUID.randomUUID(),
                                "移动速度",
                                allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.CHEST));
                    }
                    case EquipmentConst.LEG -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                UUID.randomUUID(),
                                "移动速度",
                                allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.LEGS));
                    }
                    case EquipmentConst.FOOT -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                UUID.randomUUID(),
                                "移动速度",
                                allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.FEET));
                    }
                    case EquipmentConst.SHIELD -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                                UUID.randomUUID(),
                                "移动速度",
                                allAttribute.get(EquipMainAttributeConst.MOVE_SPEED_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.OFF_HAND));
                    }
                }
            }

            //          最大血量扫尾
            if (allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL) != null){
                switch (type) {
                    case EquipmentConst.HEAD -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                UUID.randomUUID(),
                                "最大血量",
                                allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.HEAD));
                    }
                    case EquipmentConst.CHEST -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                UUID.randomUUID(),
                                "最大血量",
                                allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.CHEST));
                    }
                    case EquipmentConst.WING -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                UUID.randomUUID(),
                                "最大血量",
                                allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.CHEST));
                    }
                    case EquipmentConst.FOOT -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                UUID.randomUUID(),
                                "最大血量",
                                allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.FEET));
                    }
                    case EquipmentConst.LEG -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                UUID.randomUUID(),
                                "最大血量",
                                allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.LEGS));
                    }
                    case EquipmentConst.SHIELD -> {
                        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                                UUID.randomUUID(),
                                "最大血量",
                                allAttribute.get(EquipMainAttributeConst.MAX_HEALTH_LABEL),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlot.OFF_HAND));
                    }
                }
            }
            bonusLore.add("");
        }
        itemMeta.setLore(bonusLore);

        //检查是否存在盔甲值

        if (itemMeta.getAttributeModifiers() != null) {
            if (!itemMeta.getAttributeModifiers().containsKey(Attribute.GENERIC_ARMOR)) {
                switch (type) {
                    case EquipmentConst.HEAD -> itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(
                            UUID.randomUUID(),
                            "盔甲值",
                            3,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlot.HEAD));
                    case EquipmentConst.CHEST -> itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(
                            UUID.randomUUID(),
                            "盔甲值",
                            8,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlot.CHEST));
                    case EquipmentConst.LEG -> itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(
                            UUID.randomUUID(),
                            "盔甲值",
                            6,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlot.LEGS));
                    case EquipmentConst.FOOT -> itemMeta.addAttributeModifier(Attribute.GENERIC_ARMOR, new AttributeModifier(
                            UUID.randomUUID(),
                            "盔甲值",
                            3,
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlot.FEET));
                }
            }
        }
        itemStack.setItemMeta(itemMeta);
    }




    public static void setMain(Map<String,String> mainAttribute,ItemStack itemStack,Map<String,Double> allAttribute){
        //设置主属性
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> mainLore = itemMeta.getLore();

        for (String attribute : mainAttribute.keySet()) {
            switch (attribute){
                case WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL -> {
                    String[] split = mainAttribute.get(attribute).split(",");
                    mainLore.add(ChatColor.GREEN + "▸ " +
                            WeaponMainAttributeConst.PHYSICS_ATTACK_NAME + ": " + ChatColor.WHITE + split[0] + " - " + split[1]);
                }
                case WeaponMainAttributeConst.CRITICAL_CHANCE_LABEL ->{
                    mainLore.add(ChatColor.GREEN + "▸ " +
                            WeaponMainAttributeConst.CRITICAL_CHANCE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.CRITICAL_DAMAGE_LABEL -> {
                    mainLore.add(ChatColor.GREEN + "▸ " +
                            WeaponMainAttributeConst.CRITICAL_DAMAGE_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)+"х");
                }
                case WeaponMainAttributeConst.DAMAGE_REDUCE_LABEL -> {
                    mainLore.add(ChatColor.GOLD + "▸ " +
                            WeaponMainAttributeConst.DAMAGE_REDUCE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.BLOCK_CHANCE_LABEL -> {
                    mainLore.add(ChatColor.GOLD + "▸ " +
                            WeaponMainAttributeConst.BLOCK_CHANCE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.HIT_CHANCE_LABEL -> {
                    mainLore.add(ChatColor.GOLD + "▸ " +
                            WeaponMainAttributeConst.HIT_CHANCE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.HIT_SPEED_LABEL -> {
                    mainLore.add(ChatColor.YELLOW + "▸ " +
                            WeaponMainAttributeConst.HIT_SPEED_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)+"%");
                    double attackSpeedModifier = 4;
                    itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,new AttributeModifier(
                           UUID.randomUUID(),
                            "攻击速度",
                            4+4*(Double.valueOf(mainAttribute.get(attribute))/100),
                            AttributeModifier.Operation.ADD_NUMBER,
                            EquipmentSlot.HAND
                    ));
                }
                case WeaponMainAttributeConst.MOVE_SPEED_LABEL -> {
                    mainLore.add(ChatColor.YELLOW + "▸ " +
                            WeaponMainAttributeConst.MOVE_SPEED_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)+"%");
                    allAttribute.put(WeaponMainAttributeConst.MOVE_SPEED_LABEL,0.1*(Double.valueOf(mainAttribute.get(attribute))/100));
                }
                case WeaponMainAttributeConst.PVE_DAMAGE_LABEL ->{
                    mainLore.add(ChatColor.AQUA + "▸ " +
                            WeaponMainAttributeConst.PVE_DAMAGE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.PVP_DAMAGE_LABEL ->{
                    mainLore.add(ChatColor.AQUA + "▸ " +
                            WeaponMainAttributeConst.PVP_DAMAGE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.ARMOR_PENETRATION_LABEL ->{
                    mainLore.add(ChatColor.RED + "▸ " +
                            WeaponMainAttributeConst.ARMOR_PENETRATION_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.LIFE_STEAL_LABEL -> {
                    mainLore.add(ChatColor.RED + "▸ " +
                            WeaponMainAttributeConst.LIFE_STEAL_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case WeaponMainAttributeConst.MAX_HEALTH_LABEL -> {
                    mainLore.add(ChatColor.RED + "▸ " +
                            WeaponMainAttributeConst.MAX_HEALTH_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute));
                    allAttribute.put(WeaponMainAttributeConst.MAX_HEALTH_LABEL, Double.valueOf(mainAttribute.get(attribute)));
                }
            }
        }
        mainLore.add("");
        itemMeta.setLore(mainLore);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setMain(Map<String,String> mainAttribute,ItemStack itemStack,boolean isEquipment,String type,Map<String,Double> allAttribute){
        //设置主属性
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> mainLore = itemMeta.getLore();
        for (String attribute : mainAttribute.keySet()) {
            switch (attribute){
                case EquipMainAttributeConst.PHYSICAL_DEFENSE_LABEL -> {
                    mainLore.add(ChatColor.GREEN+"▸ "+
                            EquipMainAttributeConst.PHYSICAL_DEFENSE_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)
                    );
                }
                case EquipMainAttributeConst.MAGIC_DEFENSE_LABEL -> {
                    mainLore.add(ChatColor.LIGHT_PURPLE +"▸ "+
                            EquipMainAttributeConst.MAGIC_DEFENSE_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)
                    );
                }
                case EquipMainAttributeConst.FIRE_DEFENSE_LABEL-> {
                    mainLore.add(ChatColor.RED+"▸ "+
                            EquipMainAttributeConst.FIRE_DEFENSE_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)
                    );
                }
                case EquipMainAttributeConst.WATER_DEFENSE_LABEL -> {
                    mainLore.add(ChatColor.BLUE +"▸ "+
                            EquipMainAttributeConst.WATER_DEFENSE_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)
                    );
                }
                case EquipMainAttributeConst.POISON_DEFENSE_LABEL -> {
                    mainLore.add(ChatColor.DARK_GREEN+"▸ "+
                            EquipMainAttributeConst.POISON_DEFENSE_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)
                    );
                }
                case EquipMainAttributeConst.WIND_DEFENSE_LABEL  -> {
                    mainLore.add(ChatColor.GRAY +"▸ "+
                            EquipMainAttributeConst.WIND_DEFENSE_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)
                    );
                }

                case EquipMainAttributeConst.MOVE_SPEED_LABEL -> {
                    mainLore.add(ChatColor.YELLOW + "▸ " +
                            EquipMainAttributeConst.MOVE_SPEED_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute)+"%");
                    allAttribute.put(EquipMainAttributeConst.MOVE_SPEED_LABEL,0.1*(Double.valueOf(mainAttribute.get(attribute))/100));
                }

                case EquipMainAttributeConst.BLOCK_CHANCE_LABEL -> {
                    mainLore.add(ChatColor.GOLD+"▸ "+
                            EquipMainAttributeConst.BLOCK_CHANCE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%"

                    );
                }

                case EquipMainAttributeConst.DAMAGE_REDUCE_LABEL -> {
                    mainLore.add(ChatColor.GOLD+"▸ "+
                            EquipMainAttributeConst.DAMAGE_REDUCE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%"

                    );
                }
                case EquipMainAttributeConst.MAX_HEALTH_LABEL -> {
                    mainLore.add(ChatColor.RED + "▸ " +
                            EquipMainAttributeConst.MAX_HEALTH_NAME+": "+ColorConst.WORD_COLOR+mainAttribute.get(attribute));
                    allAttribute.put(EquipMainAttributeConst.MAX_HEALTH_LABEL, Double.valueOf(mainAttribute.get(attribute)));
                }
                case EquipMainAttributeConst.PVE_DAMAGE_LABEL ->{
                    mainLore.add(ChatColor.AQUA + "▸ " +
                            EquipMainAttributeConst.PVE_DAMAGE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }
                case EquipMainAttributeConst.PVP_DAMAGE_LABEL ->{
                    mainLore.add(ChatColor.AQUA + "▸ " +
                            EquipMainAttributeConst.PVP_DAMAGE_NAME+": "+ColorConst.WORD_COLOR+"+"+mainAttribute.get(attribute)+"%");
                }

                case EquipMainAttributeConst.PVE_DEFENSE_LABEL -> {
                    mainLore.add(ChatColor.AQUA + "▸ " +
                            EquipMainAttributeConst.PVE_DEFENSE_NAME + ": " + ColorConst.WORD_COLOR + "+" + mainAttribute.get(attribute) + "%");
                }
                case EquipMainAttributeConst.PVP_DEFENSE_LABEL -> {
                    mainLore.add(ChatColor.AQUA + "▸ " +
                            EquipMainAttributeConst.PVP_DEFENSE_NAME + ": " + ColorConst.WORD_COLOR + "+" + mainAttribute.get(attribute) + "%");
                }
            }
        }
        mainLore.add("");
        itemMeta.setLore(mainLore);
        itemStack.setItemMeta(itemMeta);
    }


    public static void setGem(List<String> gems,ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> gemLore = itemMeta.getLore();

        if(gems == null || gems.size() == 0)return;
        else{
            for (String gem : gems) {
                if (gem.equalsIgnoreCase("")){
                    gemLore.add(ChatColor.GREEN+"□ (空) 宝石槽");
                }else {
                    gemLore.add(ChatColor.GREEN+"▣ 宝石: "+ChatColor.DARK_GREEN+gem);
                }
            }
            gemLore.add("");
        }
        itemMeta.setLore(gemLore);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setEnchant(List<String> enchants,ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> enchantLore = itemMeta.getLore();
        if (enchants == null || enchants.size() == 0)return;
        else{
            for (String enchant : enchants) {
                if (enchant.equalsIgnoreCase("")){
                    enchantLore.add(ChatColor.RED +"◦ (空) 附魔槽");
                }else {
                    enchantLore.add(ChatColor.RED+"⦁ 附魔: "+ChatColor.DARK_RED+enchant);
                }
            }
            enchantLore.add("");
        }
        itemMeta.setLore(enchantLore);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setRune(List<String> runes,ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> runeLore = itemMeta.getLore();
        if (runes == null || runes.size() == 0)return;
        else{
            for (String rune : runes) {
                if (rune.equalsIgnoreCase("")){
                    runeLore.add(ChatColor.AQUA +"◇ (空) 符文槽");
                }else {
                    runeLore.add(ChatColor.AQUA +"◈ 符文: "+ ChatColor.DARK_AQUA +rune);
                }
            }
            runeLore.add("");
        }
        itemMeta.setLore(runeLore);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setSkill(List<String> skill,ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> skillLore = itemMeta.getLore();
        if (skill == null || skill.size() == 0)return;
        else{
            for (String skillData : skill) {
                if (skillData.equalsIgnoreCase("")){
                    skillLore.add(ChatColor.LIGHT_PURPLE +"⋆ (空) 技艺槽");
                }else{
                    String[] split = skillData.split(",");
                    skillLore.add(ChatColor.LIGHT_PURPLE +"⋆ 技艺: "+ChatColor.DARK_PURPLE+split[0]
                            +ChatColor.WHITE+" ["+ChatColor.DARK_PURPLE+split[1]+ChatColor.WHITE+"] "
                            +ChatColor.WHITE+" ["+ChatColor.LIGHT_PURPLE+split[2]+ChatColor.WHITE+"] "
                    );
                }
            }
            skillLore.add("");
        }
        itemMeta.setLore(skillLore);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setName(String name , String type,List<String> description,String category
            , Map<String,String> damageType,String soulBind,String level, ItemStack itemStack, String customColor){


        ItemMeta itemMeta = itemStack.getItemMeta();
        String typeName = null;

        switch (type){
            case WeaponSouvenirConst.SHIELD -> typeName= "盾牌";
            case WeaponSouvenirConst.TELESCOPE -> typeName = "望远镜";
            case WeaponSouvenirConst.DIAMOND_DRAFT -> typeName = "稿子";
            case WeaponSouvenirConst.WOOD_SWORD -> typeName = "大剑";
            case WeaponSouvenirConst.DIAMOND_AXE -> typeName = "斧子";
            default -> typeName = "未知";
        }
        List<String> damageTypeWord = new ArrayList<>();
        Set<String> damageTypes = damageType.keySet();
        for (String key : damageTypes) {
            TextComponent text = new TextComponent(damageType.get(key)+"%");
            text.setBold(false);
            text.setFont("Arial");
            text.setColor(ColorConst.WORD_COLOR.asBungee());

            if (key.equalsIgnoreCase(DamageConst.PHYSICAL)){
                damageTypeWord.add(ColorConst.PHYSICAL_DAMAGE+"⁎ 物理伤害: "+text.getText());
            }else if(key.equalsIgnoreCase(DamageConst.MAGIC)){
                damageTypeWord.add(ColorConst.MAGIC_DAMAGE+"⁎ 魔法伤害: "+text.getText());
            }else if (key.equalsIgnoreCase(DamageConst.FIRE)){
                damageTypeWord.add(ColorConst.FIRE_DAMAGE+"⁎ 火焰伤害: "+text.getText());
            }else if (key.equalsIgnoreCase(DamageConst.POISON)){
                damageTypeWord.add(ColorConst.POISON_DAMAGE+"⁎ 毒素伤害: "+text.getText());
            }else if (key.equalsIgnoreCase(DamageConst.WATER)) {
                damageTypeWord.add(ColorConst.WATER_DAMAGE+"⁎ 洪流伤害: "+text.getText());
            }else{
                damageTypeWord.add(ChatColor.BLACK+"⁎ 未知伤害: "+text.getText());
            }
        }

        TextComponent soulText = new TextComponent(level);
        soulText.setFont("Arial");
        soulText.setBold(false);
        soulText.setColor(ColorConst.WORD_COLOR.asBungee());

        List<String> topLore = new ArrayList<>();
        topLore.add(ColorConst.TITLE_COLOR+"描述: "+customColor+description.get(0));
        for(int i = 1;i<description.size();i++){
            topLore.add(customColor+description.get(i));
        }
        topLore.add(ColorConst.TITLE_COLOR+"种类: "+ColorConst.WORD_COLOR+typeName);
        topLore.add(ColorConst.TITLE_COLOR+"品质: "+customColor+"武器纪念品");
        topLore.addAll(damageTypeWord);

        if (category != CategoryConst.COMMON && category != CategoryConst.RARE && soulBind !=null){
            topLore.add(ChatColor.RED+"需求绑定后使用!");
        }
        if (level != null){
            topLore.add( ColorConst.LEVEL+"需求等级 "+soulText.getText());
        }

        //创建主标题
        topLore.add("");
        itemMeta.setDisplayName(customColor+name+"\n");
        itemMeta.setLore(topLore);

        itemStack.setItemMeta(itemMeta);
    }

    public static void setName(String name , String type, String category
            , Map<String,String> damageType,String soulBind,String level, ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        ChatColor color = null;
        String typeName = null;
        String categoryName = null;
        switch (category){
            case CategoryConst.COMMON -> {
                color = ColorConst.COMMON_COLOR;
                categoryName = "普通";
            }
            case CategoryConst.RARE -> {
                color = ColorConst.RARE_COLOR;
                categoryName = "稀有";
            }
            case CategoryConst.LEGEND,CategoryConst.PS_LEGEND-> {
                color = ColorConst.LEGEND_COLOR;
                categoryName = "传说";
            }
            case CategoryConst.ANCIENT -> {
                color = ColorConst.ANCIENT_COLOR;
                categoryName = "远古";
            }
            case CategoryConst.IMMORTAL,CategoryConst.PS_IMMORTAL -> {
                color = ColorConst.IMMORTAL_COLOR;
                categoryName = "不朽";
            }
            case CategoryConst.MYTH -> {
                color = ColorConst.MYTH_COLOR;
                categoryName = "神话";
            }
            case CategoryConst.TREASURE -> {
                color = ColorConst.TREASURE_COLOR;
                categoryName = "至宝";
            }
            case CategoryConst.DAZZLING -> {
                color = ColorConst.DAZZLING_COLOR;
                categoryName = "璀璨";
            }
            case CategoryConst.EPIC -> {
                color = ColorConst.EPIC_COLOR;
                categoryName = "史诗";
            }
            case CategoryConst.HEAVEN -> {
                color = ColorConst.HEAVEN_COLOR;
                categoryName = "天道";
            }
            default -> {
                color = ChatColor.BLACK;
                categoryName = "未知";
            }
        }
        switch (type){
            case WeaponConst.FISHING_ROD -> typeName= "鱼竿";
            case WeaponConst.SWORD -> typeName = "大剑";
            case WeaponConst.AXE -> typeName = "斧子";
            case WeaponConst.BOW -> typeName = "弓";
            default -> typeName = "未知";
        }
        List<String> damageTypeWord = new ArrayList<>();
        Set<String> damageTypes = damageType.keySet();
        for (String key : damageTypes) {
            TextComponent text = new TextComponent(damageType.get(key)+"%");
            text.setBold(false);
            text.setFont("Arial");
            text.setColor(ColorConst.WORD_COLOR.asBungee());

            if (key.equalsIgnoreCase(DamageConst.PHYSICAL)){
                damageTypeWord.add(ColorConst.PHYSICAL_DAMAGE+"⁎ 物理伤害: "+text.getText());
            }else if(key.equalsIgnoreCase(DamageConst.MAGIC)){
                damageTypeWord.add(ColorConst.MAGIC_DAMAGE+"⁎ 魔法伤害: "+text.getText());
            }else if (key.equalsIgnoreCase(DamageConst.FIRE)){
                damageTypeWord.add(ColorConst.FIRE_DAMAGE+"⁎ 火焰伤害: "+text.getText());
            }else if (key.equalsIgnoreCase(DamageConst.POISON)){
                damageTypeWord.add(ColorConst.POISON_DAMAGE+"⁎ 毒素伤害: "+text.getText());
            }else if (key.equalsIgnoreCase(DamageConst.WATER)) {
                damageTypeWord.add(ColorConst.WATER_DAMAGE+"⁎ 洪流伤害: "+text.getText());
            }else if (key.equalsIgnoreCase(DamageConst.WIND)){
                damageTypeWord.add(ColorConst.WIND_DAMAGE+"⁎ 狂风伤害: "+text.getText());
            }else{
                damageTypeWord.add(ChatColor.BLACK+"⁎ 未知伤害: "+text.getText());
            }
        }

        TextComponent soulText = new TextComponent(level);
        soulText.setFont("Arial");
        soulText.setBold(false);
        soulText.setColor(ColorConst.WORD_COLOR.asBungee());

        List<String> topLore = new ArrayList<>();
        topLore.add(ColorConst.TITLE_COLOR+"种类: "+ColorConst.WORD_COLOR+typeName);
        topLore.add(ColorConst.TITLE_COLOR+"品质: "+color+categoryName);
        topLore.addAll(damageTypeWord);
        if (category != CategoryConst.COMMON && category != CategoryConst.RARE && soulBind !=null){
            topLore.add(ChatColor.RED+"需求绑定后使用!");
        }
        if (level != null){
            topLore.add( ColorConst.LEVEL+"需求等级 "+soulText.getText());
        }
        topLore.add("");
        //创建主标题
        itemMeta.setDisplayName(color+name+"\n");
        itemMeta.setLore(topLore);

        itemStack.setItemMeta(itemMeta);
    }


    public static void setName(String name , String type, String category
            ,String soulBind,String level, ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();
        ChatColor color = null;
        String typeName = null;
        String categoryName = null;
        switch (category){
            case CategoryConst.COMMON -> {
                color = ColorConst.COMMON_COLOR;
                categoryName = "普通";
            }
            case CategoryConst.RARE -> {
                color = ColorConst.RARE_COLOR;
                categoryName = "稀有";
            }
            case CategoryConst.LEGEND,CategoryConst.PS_LEGEND-> {
                color = ColorConst.LEGEND_COLOR;
                categoryName = "传说";
            }
            case CategoryConst.ANCIENT -> {
                color = ColorConst.ANCIENT_COLOR;
                categoryName = "远古";
            }
            case CategoryConst.IMMORTAL,CategoryConst.PS_IMMORTAL -> {
                color = ColorConst.IMMORTAL_COLOR;
                categoryName = "不朽";
            }
            case CategoryConst.MYTH -> {
                color = ColorConst.MYTH_COLOR;
                categoryName = "神话";
            }
            case CategoryConst.TREASURE -> {
                color = ColorConst.TREASURE_COLOR;
                categoryName = "至宝";
            }
            case CategoryConst.DAZZLING -> {
                color = ColorConst.DAZZLING_COLOR;
                categoryName = "璀璨";
            }
            case CategoryConst.EPIC -> {
                color = ColorConst.EPIC_COLOR;
                categoryName = "史诗";
            }
            case CategoryConst.HEAVEN -> {
                color = ColorConst.HEAVEN_COLOR;
                categoryName = "天道";
            }
            default -> {
                color = ChatColor.BLACK;
                categoryName = "未知";
            }
        }
        switch (type){
            case EquipmentConst.HEAD -> typeName= "头盔";
            case EquipmentConst.CHEST -> typeName = "胸甲";
            case EquipmentConst.LEG -> typeName = "腿甲";
            case EquipmentConst.FOOT-> typeName = "靴子";
            case EquipmentConst.SHIELD -> typeName = "盾牌";
            case EquipmentConst.WING -> typeName = "鞘翅";
            default -> typeName = "未知";
        }


        TextComponent soulText = new TextComponent(level);
        soulText.setFont("Arial");
        soulText.setBold(false);
        soulText.setColor(ColorConst.WORD_COLOR.asBungee());

        List<String> topLore = new ArrayList<>();
        topLore.add(ColorConst.TITLE_COLOR+"种类: "+ColorConst.WORD_COLOR+typeName);
        topLore.add(ColorConst.TITLE_COLOR+"品质: "+color+categoryName);

        if (category != CategoryConst.COMMON && category != CategoryConst.RARE && soulBind !=null){
            topLore.add(ChatColor.RED+"需求绑定后使用!");
        }
        if (level != null){
            topLore.add( ColorConst.LEVEL+"需求等级 "+soulText.getText());
        }

        topLore.add("");

        //创建主标题
        itemMeta.setDisplayName(color+name+"\n");
        itemMeta.setLore(topLore);

        itemStack.setItemMeta(itemMeta);
    }

    public static ItemStack getMetaItem(String type, String category) {
        final String commonCategory = CategoryConst.COMMON;
        final String rareCategory = CategoryConst.RARE;
        final String legendCategory = CategoryConst.LEGEND;
        final String immortalCategory = CategoryConst.IMMORTAL;
        final String ancientCategory = CategoryConst.ANCIENT;
        return switch (type) {
            case WeaponConst.FISHING_ROD -> new ItemStack(Material.FISHING_ROD);
            case WeaponConst.SWORD -> switch (category.toLowerCase()) {
                case commonCategory -> new ItemStack(Material.WOODEN_SWORD);
                case rareCategory -> new ItemStack(Material.STONE_SWORD);
                case legendCategory,ancientCategory,CategoryConst.PS_LEGEND -> new ItemStack(Material.GOLDEN_SWORD);
                case immortalCategory,CategoryConst.PS_IMMORTAL -> new ItemStack(Material.IRON_SWORD);
                default -> new ItemStack(Material.DIAMOND_SWORD);
            };
            case WeaponConst.AXE -> switch (category) {
                case commonCategory -> new ItemStack(Material.WOODEN_AXE);
                case rareCategory -> new ItemStack(Material.STONE_AXE);
                case legendCategory,ancientCategory,CategoryConst.PS_LEGEND -> new ItemStack(Material.GOLDEN_AXE);
                case immortalCategory,CategoryConst.PS_IMMORTAL -> new ItemStack(Material.IRON_AXE);
                default -> new ItemStack(Material.DIAMOND_AXE);
            };
            case WeaponConst.BOW -> new ItemStack(Material.BOW);
            case EquipmentConst.HEAD -> switch (category){
                case commonCategory -> new ItemStack(Material.LEATHER_HELMET);
                case rareCategory -> new ItemStack(Material.CHAINMAIL_HELMET);
                case legendCategory,ancientCategory,CategoryConst.PS_LEGEND -> new ItemStack(Material.GOLDEN_HELMET);
                case immortalCategory,CategoryConst.PS_IMMORTAL -> new ItemStack(Material.IRON_HELMET);
                default -> new ItemStack(Material.DIAMOND_HELMET);
            };
            case EquipmentConst.CHEST -> switch (category){
                case commonCategory -> new ItemStack(Material.LEATHER_CHESTPLATE);
                case rareCategory -> new ItemStack(Material.CHAINMAIL_CHESTPLATE);
                case legendCategory ,ancientCategory,CategoryConst.PS_LEGEND-> new ItemStack(Material.GOLDEN_CHESTPLATE);
                case immortalCategory,CategoryConst.PS_IMMORTAL -> new ItemStack(Material.IRON_CHESTPLATE);
                default -> new ItemStack(Material.DIAMOND_CHESTPLATE);
            };
            case EquipmentConst.LEG -> switch (category){
                case commonCategory -> new ItemStack(Material.LEATHER_LEGGINGS);
                case rareCategory -> new ItemStack(Material.CHAINMAIL_LEGGINGS);
                case legendCategory,ancientCategory,CategoryConst.PS_LEGEND -> new ItemStack(Material.GOLDEN_LEGGINGS);
                case immortalCategory,CategoryConst.PS_IMMORTAL -> new ItemStack(Material.IRON_LEGGINGS);
                default -> new ItemStack(Material.DIAMOND_LEGGINGS);
            };
            case EquipmentConst.FOOT -> switch (category){
                case commonCategory -> new ItemStack(Material.LEATHER_BOOTS);
                case rareCategory -> new ItemStack(Material.CHAINMAIL_BOOTS);
                case legendCategory,ancientCategory,CategoryConst.PS_LEGEND -> new ItemStack(Material.GOLDEN_BOOTS);
                case immortalCategory,CategoryConst.PS_IMMORTAL -> new ItemStack(Material.IRON_BOOTS);
                default -> new ItemStack(Material.DIAMOND_BOOTS);
            };
            case EquipmentConst.SHIELD -> SimonRpgShieldUtil.createRandomPatternBanner();
            case EquipmentConst.WING -> new ItemStack(Material.ELYTRA);
            default -> null;
        };
    }

    //纪念品武器
    public static ItemStack getMetaItem(String type) {
        return switch (type) {
            case WeaponSouvenirConst.TELESCOPE -> new ItemStack(Material.SPYGLASS);
            case WeaponSouvenirConst.DIAMOND_DRAFT -> new ItemStack(Material.NETHERITE_SWORD);
            case WeaponSouvenirConst.SHIELD -> SimonRpgShieldUtil.createRandomPatternBanner();
            case WeaponSouvenirConst.BLAZE_ROD -> new ItemStack(Material.BLAZE_ROD);
            case WeaponSouvenirConst.WOOD_SWORD -> new ItemStack(Material.WOODEN_SWORD);
            case WeaponSouvenirConst.DIAMOND_AXE -> new ItemStack(Material.DIAMOND_AXE);
            case WeaponSouvenirConst.GOLD_SWORD ->new ItemStack(Material.GOLDEN_SWORD);
            default -> null;
        };
    }
}
