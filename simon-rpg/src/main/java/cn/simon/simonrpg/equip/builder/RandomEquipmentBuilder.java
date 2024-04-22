package cn.simon.simonrpg.equip.builder;



import cn.simon.simonrpg.constants.*;
import cn.simon.simonrpg.equip.entity.Equipment;
import cn.simon.simonrpg.equip.entity.RandomEquipmentDetail;
import cn.simon.simonrpg.equip.entity.Weapon;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

;

/**
 * 制造随机装备
 */
public class RandomEquipmentBuilder {

    public static final String[] DAMAGE = {DamageConst.FIRE,DamageConst.WIND,DamageConst.MAGIC,DamageConst.POISON,DamageConst.WATER};

    public static final String WEAPON = "weapon";

    public static final String EQUIPMENT = "equipment";

    public static ItemStack makeRandomEquipment(RandomEquipmentDetail detail, String category){
        //根据描述制造装备
        //封装Weapon或者Equipment
        String type = randomType(detail.getType());

        return switch (type) {
            case WeaponConst.SWORD, WeaponConst.AXE,
                    WeaponConst.BOW, WeaponConst.FISHING_ROD -> doMakeRandomWeapon(detail,category,type);
            case EquipmentConst.HEAD, EquipmentConst.CHEST,EquipmentConst.WING,
                    EquipmentConst.FOOT,EquipmentConst.LEG,EquipmentConst.SHIELD->doMakeRandomEquipment(detail,category,type);
            default -> new ItemStack(Material.WOODEN_SWORD);
        };
    }

    private static ItemStack doMakeRandomEquipment(RandomEquipmentDetail detail,String category,String type){

        if (type == null || category == null)return new ItemStack(Material.WOODEN_SWORD);

        //获取category
        //获取属性封装为一个Equipment即可

        Equipment equipment = new Equipment();

        equipment.setCategory(category);

        equipment.setType(type);

        equipment.setSoulBind("");

        equipment.setName(getRandomName(detail.getName().get(type)));


        int mainNum = attributeNum(detail.getMain().get(EQUIPMENT));

        int bonusNum = attributeNum(detail.getBonus().get(EQUIPMENT));

        //设置等级
        equipment.setLevel(String.valueOf(getLevel(detail.getLevel(),mainNum+bonusNum)));




        equipment.setMainAttribute(getAttribute(detail.getMainAttribute().get(EQUIPMENT),detail.getMainAttributeValue().get(EQUIPMENT),mainNum,EQUIPMENT,"main"));

        equipment.setBonusAttribute(getAttribute(detail.getBonusAttribute().get(EQUIPMENT),detail.getBonusAttributeValue().get(EQUIPMENT),bonusNum,EQUIPMENT,"bonus"));



        switch (category){
            case CategoryConst.COMMON, CategoryConst.RARE,CategoryConst.LEGEND-> {
                return EquipmentBuilder.makeEquipment(equipment);
            }
        }

        //设置gem
        equipment.setGem(getRandomList(detail.getGem()));

        equipment.setEnchant(getRandomList(detail.getEnchant()));

        equipment.setRune(getRandomList(detail.getRune()));




        return EquipmentBuilder.makeEquipment(equipment);
    }



    private static ItemStack doMakeRandomWeapon(RandomEquipmentDetail detail,String category,String type){

        Weapon weapon = new Weapon();

        weapon.setCategory(category);

        weapon.setType(type);

        weapon.setSoulBind("");

        weapon.setName(getRandomName(detail.getName().get(type)));


        int mainNum = attributeNum(detail.getMain().get(WEAPON));

        int bonusNum = attributeNum(detail.getBonus().get(WEAPON));

        //设置等级
        weapon.setLevel(String.valueOf(getLevel(detail.getLevel(),mainNum+bonusNum)));


        weapon.setMainAttribute(getAttribute(detail.getMainAttribute().get(WEAPON),detail.getMainAttributeValue().get(WEAPON),mainNum,WEAPON,"main"));

        weapon.setBonusAttribute(getAttribute(detail.getBonusAttribute().get(WEAPON),detail.getBonusAttributeValue().get(WEAPON),bonusNum,WEAPON,"bonus"));

        weapon.setDamageType(getRandomDamageType());

        switch (category){
            case CategoryConst.COMMON,CategoryConst.RARE,CategoryConst.LEGEND-> {
                return EquipmentBuilder.makeWeapon(weapon);
            }
        }

        //设置gem
        weapon.setGem(getRandomList(detail.getGem()));

        weapon.setEnchant(getRandomList(detail.getEnchant()));

        weapon.setRune(getRandomList(detail.getRune()));

        weapon.setSkill(getRandomList(detail.getSkill()));



        return EquipmentBuilder.makeWeapon(weapon);
    }

    private static String getNum(double damage){
        //保留2位小数
        double score = new BigDecimal(damage).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //不足两位则补0
        DecimalFormat decimalFormat = new DecimalFormat("0.0#");
        return decimalFormat.format(damage);
    }

    private static LinkedHashMap<String, String> getRandomDamageType(){
        LinkedHashMap<String, String> damageType = new LinkedHashMap<>();
        Random random = new Random();

        if (random.nextDouble()*100 <5 ){
            damageType.put(DamageConst.PHYSICAL,"100.0");
            return damageType;
        }
        double total = 100.0;
        String ph = getNum(random.nextDouble()*50+50);
        damageType.put(DamageConst.PHYSICAL, String.valueOf(ph));
        total -= Double.parseDouble(ph);

        while (total > 0){
            double damage = random.nextDouble() * 50;
            if (damage < 10)damage += 10;
            if (total - damage > 0 ){
                String type = DAMAGE[random.nextInt(0, DAMAGE.length)];
                while (damageType.containsKey(type)){
                    type = DAMAGE[random.nextInt(0, DAMAGE.length)];
                }
                damageType.put(type,getNum(damage));
                total = total-damage;
            }else{
                String type = DAMAGE[random.nextInt(0, DAMAGE.length)];
                while (damageType.containsKey(type)){
                    type = DAMAGE[random.nextInt(0, DAMAGE.length)];
                }
                damageType.put(type,getNum(total));
                total = 0;
            }
        }
        return damageType;
    }
    private static List<String> getRandomList(String range){
        String[] split = range.split("-");
        List<String> list = new ArrayList<>();
        int num = new Random().nextInt(Integer.parseInt(split[0]),Integer.parseInt(split[1])+1);

        for (int i = 0; i < num; i++) {
            list.add("");
        }
        return list;
    }


    private static int getLevel(String levelRange,int num){

        Random random = new Random();

        String[] level = levelRange.split("-");

        return random.nextInt(Integer.parseInt(level[0]),Integer.parseInt(level[1])+1);
    }

    private static String getRandomName(List<String> names){
        int i = new Random().nextInt(names.size());
        return names.get(i);
    }



    private static LinkedHashMap<String, String> getAttribute(List<String> attribute,List<String> attributeValue,int num,String type,String attributeType){
        //根据属性出现概率+值范围+属性数量来生成物品
        LinkedHashMap<String, String> attributes = new LinkedHashMap<String, String>();

        LinkedHashMap<String, String> attributesValue = new LinkedHashMap<String, String>();

        LinkedHashMap<String, String> res = new LinkedHashMap<String, String>();

        for (String prop : attribute) {
            String[] split = prop.split(":");
            attributes.put(split[0],split[1]);
        }

        for (String value : attributeValue) {
            String[] split = value.split(":");
            attributesValue.put(split[0],split[1]);
        }



        switch (type){
            case WEAPON -> {
                switch (attributeType){
                    case "main" -> {
                        //生成物理攻击属性
                        double p1 = Double.parseDouble(getAttributeRandomValue(attributesValue.get(WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL)));
                        double p2 = Double.parseDouble(getAttributeRandomValue(attributesValue.get(WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL)));
                        String value;
                        if (p1 > p2){
                            value = p2 + ","+ p1;
                        }else{
                            value = p1 + ","+ p2;
                        }
                        res.put(WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL,value);
                        //随机剩余词条
                        //删除物理攻击词条
                        attributes.remove(WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL);
                        //获得剩余随机词条
                        List<String> randomAttributeList = getRandomAttributeList(attributes,num-1);
                        //遍历词条并且放入值
                        for (String attributeWord : randomAttributeList) {
                            double attributeRandomValue = Double.parseDouble(getAttributeRandomValue(attributesValue.get(attributeWord)));
                            res.put(attributeWord, String.valueOf(attributeRandomValue));
                        }

                        if (res.containsKey(WeaponMainAttributeConst.CRITICAL_DAMAGE_LABEL)) {
                            String physAtkLabel = WeaponMainAttributeConst.PHYSICS_ATTACK_LABEL;
                            for (Map.Entry<String, String> entry : res.entrySet()) {
                                String label = entry.getKey();
                                if (!label.equals(physAtkLabel)) {
                                    res.remove(label);
                                    break;  // 删除一个词条后，退出循环
                                }
                            }
                            res.put(WeaponMainAttributeConst.CRITICAL_CHANCE_LABEL,getAttributeRandomValue(attributesValue.get(WeaponMainAttributeConst.CRITICAL_CHANCE_LABEL)));
                        }

                    }
                    case "bonus" ->{
                        //获得剩余随机词条
                        List<String> randomAttributeList = getRandomAttributeList(attributes,num);
                        //遍历词条并且放入值
                        for (String attributeWord : randomAttributeList) {
                            double attributeRandomValue = Double.parseDouble(getAttributeRandomValue(attributesValue.get(attributeWord)));
                            res.put(attributeWord, String.valueOf(attributeRandomValue));
                        }
                    }
                }
            }
            case EQUIPMENT -> {

                switch (attributeType){
                    case "main" -> {
                        //生成物理防御属性
                        res.put(EquipMainAttributeConst.PHYSICAL_DEFENSE_LABEL,getAttributeRandomValue(attributesValue.get(EquipMainAttributeConst.PHYSICAL_DEFENSE_LABEL)));
                        //获得剩余随机词条
                        List<String> randomAttributeList = getRandomAttributeList(attributes,num-1);
                        //遍历词条并且放入值
                        for (String attributeWord : randomAttributeList) {
                            double attributeRandomValue = Double.parseDouble(getAttributeRandomValue(attributesValue.get(attributeWord)));
                            res.put(attributeWord, String.valueOf(attributeRandomValue));
                        }
                    }
                    case "bonus" ->{
                        //获得剩余随机词条
                        List<String> randomAttributeList = getRandomAttributeList(attributes,num);
                        //遍历词条并且放入值
                        for (String attributeWord : randomAttributeList) {
                            double attributeRandomValue = Double.parseDouble(getAttributeRandomValue(attributesValue.get(attributeWord)));
                            res.put(attributeWord, String.valueOf(attributeRandomValue));
                        }

                    }
                }
            }
        }
        return res;
    }

    private static List<String> getRandomAttributeList(Map<String, String> attributes, int num) {
        List<String> res = new ArrayList<>();
        List<Map.Entry<String, String>> entryList = new ArrayList<>(attributes.entrySet());
        Random random = new Random();

        for (int i = 0; i < num; i++) {
            Collections.shuffle(entryList);
            int total = getTotal(entryList);
            int value = random.nextInt(total);

            Iterator<Map.Entry<String, String>> iterator = entryList.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                int entryValue = Integer.parseInt(entry.getValue());
                if (value < entryValue) {
                    res.add(entry.getKey());
                    iterator.remove();
                    break;
                } else {
                    value -= entryValue;
                }
            }
        }
        return res;
    }

    private static int getTotal(List<Map.Entry<String, String>> entryList) {
        int total = 0;
        for (Map.Entry<String, String> entry : entryList) {
            total += Integer.parseInt(entry.getValue());
        }
        return total;
    }


    private static String getAttributeRandomValue(String range){
        String[] split = range.split("-");
        if (split.length == 2){
            double sub = Double.parseDouble(split[1]) - Double.parseDouble(split[0]);
            double minValue = 0.0;  // 指定范围的最小值
            double maxValue = 1.0;  // 指定范围的最大值
            double mean = 0.5;      // 平均值
            double stdDev = 0.3;    // 标准差

            Random random = new Random();
            double value;

            do {
                value = random.nextGaussian() * stdDev + mean;
            } while (value < minValue || value > maxValue);

            //
            value = value*sub + Double.parseDouble(split[0]);

            DecimalFormat decimalFormat = new DecimalFormat("0.00");

            return String.valueOf(decimalFormat.format(value));
        }else{
            System.out.println(range);
        }
        return "0";
    }






    private static String randomType(Map<String, String> type){
            double randomNum = getRandomNumber();
            String typeKey = null;
            for (String key : type.keySet()) {
                if (randomNum - Double.parseDouble(type.get(key))/100 <= 0) {
                    typeKey = key;
                    break;
                }else{
                    randomNum -= Double.parseDouble(type.get(key))/100;
                }
            }
            return typeKey;
    }

    private static int attributeNum(List<String> nums){
        int num = 1;
        double random = randomNormalDistribution();
        for (String mainWord : nums) {
            String[] split = mainWord.split(":");
            if (random < Double.parseDouble(split[1])) {
                num = Integer.parseInt(split[0]) - 1;
                break;
            }
        }

        return num;
    }

    private static double randomNormalDistribution(){
        Random random = new Random();
        double mean = 50; // 均值
        double stdDeviation = 25; // 标准差

        // 生成服从正态分布，并大部分值位于 0 到 100 范围内的随机数
        double randomValue;
        do {
            randomValue = random.nextGaussian() * stdDeviation + mean;
        } while (randomValue < 0 || randomValue > 100); // 保证随机数在 0 到 100 范围内

        return randomValue;
    }

    private static double getRandomNumber() {
        Random random = new Random();
        return random.nextDouble();
    }


}
