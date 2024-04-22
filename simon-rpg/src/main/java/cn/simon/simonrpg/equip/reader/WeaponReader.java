package cn.simon.simonrpg.equip.reader;


import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.equip.entity.Weapon;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class WeaponReader {
    public static List<Weapon> ReadConfig(String path){
        List<Weapon> weaponRes = new ArrayList<>();
        List<FileConfiguration> weaponConfigs = ReadingUtil.readConfig(path);
        for (FileConfiguration weaponConfig : weaponConfigs) {
                List<Map<?, ?>> weapons = weaponConfig.getMapList("weapon");

                for (Map<?, ?> weapon : weapons) {
                    //读取类型封装Weapon
                    Weapon newWeapon = new Weapon();
                    //先读取姓名
                    String name = (String) weapon.get(ConfigConst.NAME);

                    newWeapon.setName(name);
                    //类型
                    String type = (String) weapon.get(ConfigConst.TYPE);

                    newWeapon.setType(type);

                    //分类
                    String category = (String) weapon.get(ConfigConst.CATEGORY);

                    newWeapon.setCategory(category);

                    //灵魂绑定
                    String soulBind = (String) weapon.get(ConfigConst.SOUL_BIND);

                    newWeapon.setSoulBind(soulBind);

                    //等级需求

                    String level = (String) weapon.get(ConfigConst.LEVEL);

                    newWeapon.setLevel(level);

                    //伤害类型
                    LinkedHashMap<String, String> damageType = (LinkedHashMap<String, String>) weapon.get(ConfigConst.DAMAGE_TYPE);
                    newWeapon.setDamageType(damageType);

                    //主属性

                    LinkedHashMap<String, String> main = (LinkedHashMap<String, String>) weapon.get(ConfigConst.MAIN);
                    newWeapon.setMainAttribute(main);


                    //副属性

                    LinkedHashMap<String, String> bonus = (LinkedHashMap<String, String>) weapon.get(ConfigConst.BONUS);
                    if (bonus == null)bonus = new LinkedHashMap<>();
                    newWeapon.setBonusAttribute(bonus);


                    List<String> gem = (List<String>) weapon.get(ConfigConst.GEM);
                    List<String> enchant = (List<String>) weapon.get(ConfigConst.ENCHANT);
                    List<String> rune = (List<String>) weapon.get(ConfigConst.RUNE);
                    List<String> skill = (List<String>) weapon.get(ConfigConst.SKILL);


                    if (gem == null){
                        gem = new ArrayList<>();
                        enchant = new ArrayList<>();
                        rune = new ArrayList<>();
                        skill = new ArrayList<>();
                    }

                    newWeapon.setGem(gem);
                    newWeapon.setEnchant(enchant);
                    newWeapon.setRune(rune);
                    newWeapon.setSkill(skill);
                    weaponRes.add(newWeapon);
                }
            }
        return weaponRes;
    }
}
