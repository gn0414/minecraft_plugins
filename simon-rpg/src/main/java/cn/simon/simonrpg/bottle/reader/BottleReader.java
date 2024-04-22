package cn.simon.simonrpg.bottle.reader;

import cn.simon.simonrpg.bottle.entity.Bottle;
import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BottleReader {
    public static ConcurrentHashMap<String, Bottle> ReadConfig(String path){
        ConcurrentHashMap<String,Bottle> bottleRes = new ConcurrentHashMap<>();
        List<FileConfiguration> bottleConfigs = ReadingUtil.readConfig(path);
        for (FileConfiguration bottleConfig : bottleConfigs) {
            List<Map<?, ?>> bottles = bottleConfig.getMapList("bottle");
            for (Map<?, ?> bottle : bottles) {
                Bottle newBottle = new Bottle();

                //先读取姓名
                String name = (String) bottle.get(ConfigConst.NAME);

                newBottle.setName(name);

                //类别
                String category = (String) bottle.get(ConfigConst.CATEGORY);
                newBottle.setCategory(category);

                //结果集
                LinkedHashMap<String,String> reward = (LinkedHashMap<String, String>) bottle.get(ConfigConst.REWARD);

                newBottle.setReward(reward);

                //介绍
                String description = (String) bottle.get(ConfigConst.DESCRIPTION);

                newBottle.setDescription(description);

                bottleRes.put(newBottle.getName().substring(2),newBottle);

            }
        }

        return bottleRes;
    }
}
