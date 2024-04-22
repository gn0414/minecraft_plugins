package cn.simon.simonrpg.equip.reader;


import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.equip.entity.Equipment;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EquipmentReader {
    public static List<Equipment> ReadConfig(String path){
        List<Equipment> equipmentRes = new ArrayList<>();

        List<FileConfiguration> equipmentConfigs = ReadingUtil.readConfig(path);
        for(FileConfiguration equipmentConfig : equipmentConfigs) {
            List<Map<?, ?>> equipments = equipmentConfig.getMapList("equipment");

            for (Map<?, ?> equipment : equipments) {
                //读取类型封装Equipment
                Equipment newEquipment = new Equipment();
                //先读取姓名
                String name = (String) equipment.get(ConfigConst.NAME);

                newEquipment.setName(name);
                //类型
                String type = (String) equipment.get(ConfigConst.TYPE);

                newEquipment.setType(type);

                //分类
                String category = (String) equipment.get(ConfigConst.CATEGORY);

                newEquipment.setCategory(category);

                //灵魂绑定
                String soulBind = (String) equipment.get(ConfigConst.SOUL_BIND);

                newEquipment.setSoulBind(soulBind);

                //等级需求
                String level = (String) equipment.get(ConfigConst.LEVEL);

                newEquipment.setLevel(level);

                //主属性
                LinkedHashMap<String, String> main = (LinkedHashMap<String, String>) equipment.get(ConfigConst.MAIN);
                newEquipment.setMainAttribute(main);

                //副属性
                LinkedHashMap<String, String> bonus = (LinkedHashMap<String, String>) equipment.get(ConfigConst.BONUS);
                if (bonus == null)bonus = new LinkedHashMap<>();
                newEquipment.setBonusAttribute(bonus);


                List<String> gem = (List<String>) equipment.get(ConfigConst.GEM);
                List<String> enchant = (List<String>) equipment.get(ConfigConst.ENCHANT);
                List<String> rune = (List<String>) equipment.get(ConfigConst.RUNE);


                if (gem == null){
                    gem = new ArrayList<>();
                    enchant = new ArrayList<>();
                    rune = new ArrayList<>();
                }

                newEquipment.setGem(gem);
                newEquipment.setEnchant(enchant);
                newEquipment.setRune(rune);

                equipmentRes.add(newEquipment);

            }
        }
        return equipmentRes;
    }
}
