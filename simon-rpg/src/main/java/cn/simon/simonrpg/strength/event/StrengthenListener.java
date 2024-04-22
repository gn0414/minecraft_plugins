package cn.simon.simonrpg.strength.event;

import cn.simon.simonrpg.constants.*;
import cn.simon.simonrpg.equip.entity.Equipment;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.jewel.entity.Jewel;
import cn.simon.simonrpg.nbt.builder.NameSpaceKeyBuilder;
import cn.simon.simonrpg.nbt.data.EquipmentType;
import cn.simon.simonrpg.nbt.data.WeaponType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrengthenListener implements Listener {

    public StrengthenListener(){
        Logger logger = Logger.getLogger("strength");
        logger.info(this.getClass().getSimpleName()+"注册完成!");
    }

    /**
     *
     * @param event 物品点击事件
     */
    @EventHandler
    public void useStrengthenListener(InventoryClickEvent event){
        if (event.getAction().equals(InventoryAction.SWAP_WITH_CURSOR)){
            if (event.getCursor() != null && event.getCurrentItem() != null){
                if (event.getCursor().getItemMeta() != null && event.getCurrentItem().getItemMeta() != null){
                    ItemStack strengthenSource = event.getCursor();
                    ItemStack strengthenItem = event.getCurrentItem();
                    Jewel jewel = strengthenSource.getItemMeta().getPersistentDataContainer().get(NameSpaceKeyBuilder.getJewelKey(), new cn.simon.simonrpg.nbt.data.JewelType());
                    if ( jewel != null){
                        if (event.getWhoClicked() instanceof Player player){
                            ItemStack strengthen = strengthen(jewel, strengthenItem,player);
                            if (strengthen == null){
                                player.sendMessage(cantStrength());
                            }else if (!strengthen.getType().equals(Material.AIR)){
                                //删掉玩家的原有武器和强化物
                                int itemSlot = findItemSlot(player,strengthenItem);
                                player.getInventory().remove(strengthenItem);
                                strengthenSource.setAmount(strengthenSource.getAmount()-1);
                                //找到第一个
                                if (itemSlot != -1){
                                    player.getInventory().setItem(itemSlot,strengthen);
                                }
                                player.sendMessage(useSuccess());
                                player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1.0f,1.0f);
                            }else {
                                //删除使用物品
                                strengthenSource.setAmount(strengthenSource.getAmount()-1);
                                player.sendMessage(strengthFail());
                                player.playSound(player.getLocation(),Sound.BLOCK_ANVIL_PLACE,1.0f,1.0f);
                            }
                        }
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


    public int findItemSlot(Player player, ItemStack itemStack) {
        Inventory inventory = player.getInventory();
        int findItemSlot = -1;
        for (int i = 0; i < inventory.getSize(); i++) {
            if (itemStack.isSimilar(inventory.getItem(i))) {
                findItemSlot = i;
                break;
            }
        }
        return findItemSlot;
    }


    private static ItemStack strengthen(Jewel jewel, ItemStack itemStack,Player player){
        //判断类型
        switch (jewel.getType().substring(2)){
            case JewelType.GEM, JewelType.RUNE -> {
                return strengthenCheck(jewel,itemStack,jewel.getType());
            }
            case JewelType.LUCKY -> {
                return strengthenLucky(jewel,itemStack);
            }case JewelType.SOUL -> {
                return useDeleteSoulBind(itemStack,player);
            }
        }
        return null;
    }


    public static ItemStack useDeleteSoulBind(ItemStack item,Player player){
        if (item.hasItemMeta()) {
            Weapon weapon = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
            if (weapon == null) {
                return null;
            } else {
                ItemMeta itemMeta = item.getItemMeta();
                List<String> lore = itemMeta.getLore();
                for (int i = 0; i < Objects.requireNonNull(lore).size(); i++) {
                    String line = lore.get(i);
                    if (line.contains(ColorConst.SOUL_BIND + "灵魂绑定 ")) {
                        String[] split = line.split(" ");
                        if (split[1].equals(ColorConst.SOUL_NAME+player.getName())){
                            lore.set(i,ChatColor.RED + "需求绑定后使用!");
                            weapon.setSoulBind(player.getName());
                            itemMeta.getPersistentDataContainer().remove(NameSpaceKeyBuilder.getEquipmentNameSpaceKey());
                            itemMeta.getPersistentDataContainer().set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType(), weapon);
                            itemMeta.setLore(lore);
                            item.setItemMeta(itemMeta);
                            return item;
                        }else{
                            player.sendMessage(notBelongMessage());
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static String notBelongMessage(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.DARK_RED+"请检查物品灵魂绑定情况";
    }

    public static String cantStrength(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.DARK_RED+"该物品无法使用";
    }

    public static String strengthFail(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.DARK_RED+"强化失败,该物品已被摧毁";
    }

    public static String useSuccess(){
        return ChatColor.AQUA+ ServerConst.SERVER_NAME+ " >> "+ChatColor.GREEN+"使用成功";
    }

    private static ItemStack strengthenCheck(Jewel jewel,ItemStack itemStack,String type){
        String jsonNBT = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), PersistentDataType.STRING);

        if (jsonNBT != null){
            String prompt = jewel.getPrompt();
            if (jsonNBT.contains("damageType")){
                if (prompt.contains("武器")){
                    switch (type.substring(2)){
                        case JewelType.RUNE -> {
                            return doWeaponStrengthenRune(jewel,itemStack);
                        }
                        case JewelType.GEM -> {
                            return doWeaponStrengthenGem(jewel,itemStack);
                        }
                    }
                }else return null;
            }else{
                if (prompt.contains("防具")){
                    switch (type.substring(2)){
                        case JewelType.RUNE -> {
                            return doEquipmentStrengthenRune(jewel,itemStack);
                        }
                        case JewelType.GEM -> {
                            return doEquipmentStrengthenGem(jewel,itemStack);
                        }
                    }
                }else return null;
            }
        }
        return null;
    }

    private static ItemStack doEquipmentStrengthenGem(Jewel jewel,ItemStack itemStack){
        return null;


    }
    private static ItemStack doWeaponStrengthenGem(Jewel jewel,ItemStack itemStack){
        int success = Integer.parseInt(jewel.getSuccessPercent());
        Random random = new Random();
        if (random.nextInt(100) >= success){
            //失败
            return new ItemStack(Material.AIR);
        }
        Weapon weapon = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
        LinkedHashMap<String, String> bonusAttribute = weapon.getBonusAttribute();
        String[] split = jewel.getDescription().split(",");

        Map<String,Double> allPlus = new ConcurrentHashMap<>();

        for (int i = 1; i < 3; i++) {
            String attributeName = null; // 初始化属性名变量
            if (split[i].contains(GemAttConfig.PVP)) {
                attributeName = WeaponBonusAttributeConst.PVP_DAMAGE_NAME;
            } else if (split[i].contains(GemAttConfig.PvE)) {
                attributeName = WeaponBonusAttributeConst.PVE_DAMAGE_NAME;
            } else if (split[i].contains(GemAttConfig.HIT_CHANCE_NAME)) {
                attributeName = WeaponBonusAttributeConst.PVE_DAMAGE_NAME;
            } else if (split[i].contains(GemAttConfig.CRITICAL_CHANCE)) {
                attributeName = WeaponBonusAttributeConst.CRITICAL_CHANCE_NAME;
            } else if (split[i].contains(GemAttConfig.PURE_DAMAGE)) {
                attributeName = WeaponBonusAttributeConst.PURE_DAMAGE_NAME;
            } else if (split[i].contains(GemAttConfig.PHYSICAL)) {
                attributeName = WeaponBonusAttributeConst.PHYSICS_DAMAGE_NAME;
            } else if (split[i].contains(GemAttConfig.CRITICAL_DAMAGE)) {
                attributeName = WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_NAME;
            } else if (split[i].contains(GemAttConfig.ARMOR)) {
                attributeName = WeaponBonusAttributeConst.ARMOR_PENETRATION_NAME;
            }

            // 添加属性名和对应的属性值
            if (attributeName != null) {
                allPlus.put(attributeName,getAttNum(split[i]));
            }
        }

        List<String> lore = itemStack.getItemMeta().getLore();

        int gemLore = -1;
        int lastBonus = -1;

        for (int i = 0; i < lore.size(); i++) {
            String tagLore = lore.get(i);

            if (tagLore.contains("⏏")){
                if (lastBonus < i) lastBonus = i;
            }

            if (tagLore.contains("宝石槽")){
                gemLore = i;
                break;
            }
        }


        if (gemLore == -1)return null;

        for (int i = 0; i < lore.size(); i++) {
            String tagLore = lore.get(i);
            if (tagLore.contains("⏏")){
                for (String plus : allPlus.keySet()) {
                    if (tagLore.contains(plus)) {
                        double attNum = getAttNum(tagLore);
                        attNum = getNum(attNum + allPlus.get(plus));
                        String[] split1 = tagLore.split(":");
                        String newLore;
                        if (plus.contains(GemAttConfig.CRITICAL_DAMAGE)){
                           newLore = split1[0] + ": " + ChatColor.GREEN + "+" + attNum + "x";
                        }else newLore = split1[0] + ": " + ChatColor.GREEN + "+" + attNum + "%";
                        lore.remove(i);
                        lore.add(i, newLore);
                        switch (plus){
                            case WeaponBonusAttributeConst.PVE_DAMAGE_NAME -> {
                                bonusAttribute.put(WeaponBonusAttributeConst.PVE_DAMAGE_LABEL,String.valueOf(attNum));
                            }
                            case WeaponBonusAttributeConst.PVP_DAMAGE_NAME -> {
                                bonusAttribute.put(WeaponBonusAttributeConst.PVP_DAMAGE_LABEL,String.valueOf(attNum));
                            }
                            case WeaponBonusAttributeConst.HIT_CHANCE_NAME -> {
                                bonusAttribute.put(WeaponBonusAttributeConst.HIT_CHANCE_LABEL,String.valueOf(attNum));
                            }
                            case WeaponBonusAttributeConst.ARMOR_PENETRATION_NAME -> {
                                bonusAttribute.put(WeaponBonusAttributeConst.ARMOR_PENETRATION_LABEL,String.valueOf(attNum));
                            }
                            case WeaponBonusAttributeConst.CRITICAL_CHANCE_NAME -> {
                                bonusAttribute.put(WeaponBonusAttributeConst.CRITICAL_CHANCE_LABEL,String.valueOf(attNum));
                            }
                            case WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_NAME -> {
                                bonusAttribute.put(WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_LABEL,String.valueOf(attNum));
                            }
                            case WeaponBonusAttributeConst.PHYSICS_DAMAGE_NAME -> {
                                bonusAttribute.put(WeaponBonusAttributeConst.PHYSICS_DAMAGE_LABEL,String.valueOf(attNum));
                            }
                        }
                        allPlus.remove(plus);
                    }
                }
            }
        }
        //扫尾
        if (!allPlus.isEmpty()){
            for (String plus : allPlus.keySet()) {
                lore.set(lastBonus+1,attrWord(plus,allPlus.get(plus),bonusAttribute));
                lastBonus++;
                gemLore++;
                lore.add(lastBonus+1,"");
            }

        }


        weapon.setBonusAttribute(bonusAttribute);

        //写入宝石槽

        String[] names = jewel.getName().split(" ");
        String lastName="";
        for (int i = 1; i < names.length; i++) {
            lastName+=names[i]+" ";
        }

        lore.set(gemLore,ChatColor.GREEN+"▣ 宝石: "+ChatColor.DARK_GREEN+lastName);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.getPersistentDataContainer().set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(),new WeaponType(),weapon);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static String attrWord(String type,double num,LinkedHashMap<String, String> bonusAttribute){
        String word = "";
        switch (type){
            case WeaponBonusAttributeConst.PVE_DAMAGE_NAME -> {
                word +=  ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.PVE_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + num + "%";
                bonusAttribute.put(WeaponBonusAttributeConst.PVE_DAMAGE_LABEL,String.valueOf(num));
            }
            case WeaponBonusAttributeConst.PVP_DAMAGE_NAME -> {
                word +=  ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.PVP_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + num + "%";
                bonusAttribute.put(WeaponBonusAttributeConst.PVP_DAMAGE_LABEL,String.valueOf(num));
            }

            case WeaponBonusAttributeConst.CRITICAL_CHANCE_NAME -> {
                word +=  ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.CRITICAL_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + num + "%";
                bonusAttribute.put(WeaponBonusAttributeConst.CRITICAL_CHANCE_LABEL,String.valueOf(num));
            }

            case WeaponBonusAttributeConst.ARMOR_PENETRATION_NAME -> {
                word +=  ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.ARMOR_PENETRATION_NAME + ": " + ChatColor.GREEN + "+" + num + "%";
                bonusAttribute.put(WeaponBonusAttributeConst.ARMOR_PENETRATION_LABEL,String.valueOf(num));
            }

            case WeaponBonusAttributeConst.HIT_CHANCE_NAME -> {
                word +=  ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.HIT_CHANCE_NAME + ": " + ChatColor.GREEN + "+" + num + "%";
                bonusAttribute.put(WeaponBonusAttributeConst.HIT_CHANCE_LABEL,String.valueOf(num));
            }
            case WeaponBonusAttributeConst.PHYSICS_DAMAGE_NAME -> {
                word +=  ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.PHYSICS_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + num + "%";
                bonusAttribute.put(WeaponBonusAttributeConst.PHYSICS_DAMAGE_LABEL,String.valueOf(num));
            }
            case WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_NAME -> {
                word +=  ChatColor.GOLD + "⏏ " + ColorConst.WORD_COLOR + WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_NAME + ": " + ChatColor.GREEN + "+" + num + "x";
                bonusAttribute.put(WeaponBonusAttributeConst.GEM_PHYSICAL_DAMAGE_LABEL,String.valueOf(num));
            }

        }
        return word;
    }


    private static double getAttNum(String text){
            Pattern pattern = Pattern.compile("\\+([0-9]+\\.?[0-9]*)%");
            Pattern patternNext = Pattern.compile("\\+([0-9]+\\.?[0-9]*)x");
            Matcher matcher = pattern.matcher(text);
            Matcher matcherNext = patternNext.matcher(text);
            if (matcher.find()) {
                   String numberStr = matcher.group(1);
                   return Double.parseDouble(numberStr);
             } else if (matcherNext.find()){
                   String numberStr = matcherNext.group(1);
                   return Double.parseDouble(numberStr);
            }else return 0;
    }

    private static ItemStack doWeaponStrengthenRune(Jewel jewel,ItemStack itemStack){

        int success = Integer.parseInt(jewel.getSuccessPercent());
        Random random = new Random();
        if (random.nextInt(100) >= success){
            //失败
            return new ItemStack(Material.AIR);
        }
        String[] names = jewel.getName().split(" ");
        String attrName = names[1];
        String attrLevel = names[2];
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            Weapon weapon = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType());
            List<String> lore = itemMeta.getLore();
            int runeSlot = -1;
            assert lore != null;
            for (int i = 0; i < lore.size(); i++) {
                if(lore.get(i).contains("符文槽")) {
                    runeSlot = i;
                    break;
                }
            }
            if (runeSlot == -1) return null;

            //嵌入装备及NBT
            lore.set(runeSlot, ChatColor.AQUA + "◈ 符文: " + ChatColor.DARK_AQUA + attrName + " " + attrLevel);

            //嵌入nbt
            List<String> rune = weapon.getRune();
            rune.add(attrName + " " + attrLevel);

            //放入nbt
            weapon.setRune(rune);
            itemMeta.getPersistentDataContainer().set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new WeaponType(), weapon);

            //放入item的lore

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            return itemStack;
        }
        return null;
    }

    private static ItemStack doEquipmentStrengthenRune(Jewel jewel,ItemStack itemStack){
        int success = Integer.parseInt(jewel.getSuccessPercent());
        Random random = new Random();
        if (random.nextInt(100) >= success){
            //失败
            return new ItemStack(Material.AIR);
        }
        String[] names = jewel.getName().split(" ");
        String attrName = names[1];
        String attrLevel = names[2];
        if (itemStack.getItemMeta() != null) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            Equipment equipment = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer().get(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new EquipmentType());
            List<String> lore = itemMeta.getLore();
            int runeSlot = -1;
            assert lore != null;
            for (int i = 0; i < lore.size(); i++) {
                if(lore.get(i).contains("符文槽")) {
                    runeSlot = i;
                    break;
                }
            }
            if (runeSlot == -1) return null;

            //嵌入装备及NBT
            lore.set(runeSlot, ChatColor.AQUA + "◈ 符文: " + ChatColor.DARK_AQUA + attrName + " " + attrLevel);

            //嵌入nbt
            List<String> rune = equipment.getRune();
            rune.add(attrName + " " + attrLevel);

            //放入nbt
            equipment.setRune(rune);
            itemMeta.getPersistentDataContainer().set(NameSpaceKeyBuilder.getEquipmentNameSpaceKey(), new EquipmentType(), equipment);

            //放入item的lore

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            return itemStack;
        }
        return null;
    }

    private static ItemStack strengthenLucky(Jewel jewel,ItemStack itemStack){
        return null;
    }

    private static double getNum(double damage){
        //保留2位小数
        double score = new BigDecimal(damage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //不足两位则补0
        DecimalFormat decimalFormat = new DecimalFormat("0.0#");
        return Double.parseDouble(decimalFormat.format(damage));
    }





}
