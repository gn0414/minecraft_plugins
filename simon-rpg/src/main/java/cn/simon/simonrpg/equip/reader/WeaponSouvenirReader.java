package cn.simon.simonrpg.equip.reader;

import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.equip.entity.WeaponSouvenir;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class WeaponSouvenirReader {

    public static List<WeaponSouvenir> ReadConfig(String path) {

        List<WeaponSouvenir> weaponSouvenirRes = new ArrayList<>();

        List<FileConfiguration> weaponConfigs = ReadingUtil.readConfig(path);

        for (FileConfiguration weaponConfig : weaponConfigs) {
            List<Map<?, ?>> souvenirs = weaponConfig.getMapList("souvenir");

            for (Map<?, ?> souvenir : souvenirs) {
                //读取类型封装Weapon
                WeaponSouvenir newWeaponSouvenir = new WeaponSouvenir();
                //先读取姓名
                String name = (String) souvenir.get(ConfigConst.NAME);

                newWeaponSouvenir.setName(name);
                //类型
                String type = (String) souvenir.get(ConfigConst.TYPE);

                newWeaponSouvenir.setType(type);

                //分类
                String category = (String) souvenir.get(ConfigConst.CATEGORY);

                newWeaponSouvenir.setCategory(category);

                //灵魂绑定
                String soulBind = (String) souvenir.get(ConfigConst.SOUL_BIND);

                newWeaponSouvenir.setSoulBind(soulBind);

                //等级需求

                String level = (String) souvenir.get(ConfigConst.LEVEL);

                newWeaponSouvenir.setLevel(level);

                //伤害类型
                LinkedHashMap<String, String> damageType = (LinkedHashMap<String, String>) souvenir.get(ConfigConst.DAMAGE_TYPE);
                newWeaponSouvenir.setDamageType(damageType);

                //主属性

                LinkedHashMap<String, String> main = (LinkedHashMap<String, String>) souvenir.get(ConfigConst.MAIN);

                newWeaponSouvenir.setMainAttribute(main);

                //副属性

                LinkedHashMap<String, String> bonus = (LinkedHashMap<String, String>) souvenir.get(ConfigConst.BONUS);
                if (bonus == null)bonus = new LinkedHashMap<>();
                newWeaponSouvenir.setBonusAttribute(bonus);

                List<String> gem = (List<String>) souvenir.get(ConfigConst.GEM);
                List<String> enchant = (List<String>) souvenir.get(ConfigConst.ENCHANT);
                List<String> rune = (List<String>) souvenir.get(ConfigConst.RUNE);


                if (gem == null){
                    gem = new ArrayList<>();
                    enchant = new ArrayList<>();
                    rune = new ArrayList<>();
                }

                newWeaponSouvenir.setGem(gem);
                newWeaponSouvenir.setEnchant(enchant);
                newWeaponSouvenir.setRune(rune);

                //描述
                List<String> description = (List<String>) souvenir.get(ConfigConst.DESCRIPTION);

                newWeaponSouvenir.setDescription(description);


                //自定义颜色
                String color = (String) souvenir.get(ConfigConst.CUSTOM_COLOR);

                newWeaponSouvenir.setCustomizeColor(color);

                weaponSouvenirRes.add(newWeaponSouvenir);
            }
        }
        return weaponSouvenirRes;
    }
}
