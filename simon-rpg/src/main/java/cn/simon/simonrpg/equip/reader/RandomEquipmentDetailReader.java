package cn.simon.simonrpg.equip.reader;


import cn.simon.simonrpg.constants.RandomEquipmentDetailConst;
import cn.simon.simonrpg.equip.entity.RandomEquipmentDetail;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.file.FileConfiguration;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RandomEquipmentDetailReader {

    public static ConcurrentHashMap<String, RandomEquipmentDetail> ReadConfig(String path){
        ConcurrentHashMap<String,RandomEquipmentDetail> randomEquipmentRes = new ConcurrentHashMap<>();
        List<FileConfiguration> randomEquipmentConfigs = ReadingUtil.readConfig(path);
        for (FileConfiguration randomEquipmentConfig : randomEquipmentConfigs) {
            List<Map<?, ?>> equipments = randomEquipmentConfig.getMapList("random");
            for (Map<?, ?> equipment : equipments) {

                //读取类型封装Equipment
                RandomEquipmentDetail newEquipmentDetail = new RandomEquipmentDetail();

                //读取装备类型type概率

                Map<String,String> type = (Map<String, String>) equipment.get(RandomEquipmentDetailConst.TYPE);

                newEquipmentDetail.setType(type);

                //读取武器类型的name

                Map<String,List<String>> name = (Map<String, List<String>>) equipment.get(RandomEquipmentDetailConst.NAME);

                newEquipmentDetail.setName(name);

                //读取等级范围

                String level = (String) equipment.get(RandomEquipmentDetailConst.LEVEL);

                newEquipmentDetail.setLevel(level);


                //list[0]是武器 list[1]是装备
                //主词条数量
                Map<String,List<String>> main = (Map<String,List<String>>) equipment.get(RandomEquipmentDetailConst.MAIN);

                newEquipmentDetail.setMain(main);

                //副词条数量
                Map<String,List<String>> bonus = (Map<String,List<String>>)  equipment.get(RandomEquipmentDetailConst.BONUS);

                newEquipmentDetail.setBonus(bonus);

                //主词条出现概率

                Map<String,List<String>>  mainAttribute =  (Map<String,List<String>>) equipment.get(RandomEquipmentDetailConst.MAIN_ATTRIBUTE);


                newEquipmentDetail.setMainAttribute(mainAttribute);

                //副词条出现概率

                Map<String,List<String>>  bonusAttribute =  (Map<String,List<String>>) equipment.get(RandomEquipmentDetailConst.BONUS_ATTRIBUTE);

                newEquipmentDetail.setBonusAttribute(bonusAttribute);

                //主词条范围

                Map<String,List<String>> mainAttributeValue = (Map<String,List<String>>) equipment.get(RandomEquipmentDetailConst.MAIN_ATTRIBUTE_VALUE);
                newEquipmentDetail.setMainAttributeValue(mainAttributeValue);

                //副词条范围
                Map<String,List<String>> bonusAttributeValue = (Map<String,List<String>>) equipment.get(RandomEquipmentDetailConst.BONUS_ATTRIBUTE_VALUE);
                newEquipmentDetail.setBonusAttributeValue(bonusAttributeValue);

                String gem = (String) equipment.get(RandomEquipmentDetailConst.GEM);
                newEquipmentDetail.setGem(gem);

                String enchant = (String) equipment.get(RandomEquipmentDetailConst.ENCHANT);
                newEquipmentDetail.setEnchant(enchant);

                String rune = (String) equipment.get(RandomEquipmentDetailConst.RUNE);
                newEquipmentDetail.setRune(rune);

                String skill = (String) equipment.get(RandomEquipmentDetailConst.SKILL);
                newEquipmentDetail.setSkill(skill);

                String category = (String) equipment.get(RandomEquipmentDetailConst.CATEGORY);

                randomEquipmentRes.put(category,newEquipmentDetail);

            }

        }

        return randomEquipmentRes;
    }
}
