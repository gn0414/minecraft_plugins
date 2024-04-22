package cn.simon.simonrpg.mob.reader;

import cn.simon.simonrpg.constants.MobConst;
import cn.simon.simonrpg.mob.entity.Mob;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MobReader {

    public static ConcurrentHashMap<String, Mob> ReadConfig(String path){
        ConcurrentHashMap<String, Mob> mobRes = new ConcurrentHashMap<>();
        List<FileConfiguration> mobConfigs = ReadingUtil.readConfig(path);
        for (FileConfiguration mobConfig : mobConfigs) {
            List<Map<?, ?>> mobs = mobConfig.getMapList("mob");
            for (Map<?, ?> mob : mobs) {
                //读取并写属性
                Mob boss = new Mob();
                String name = (String) mob.get(MobConst.NAME);
                boss.setName(name);

                Map<String,String> attributes = (Map<String, String>) mob.get(MobConst.ATTRIBUTES);
                boss.setAttributes(attributes);
                mobRes.put(name,boss);
            }
        }
        return mobRes;
    }
}
