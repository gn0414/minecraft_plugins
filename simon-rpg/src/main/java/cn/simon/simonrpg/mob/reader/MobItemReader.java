package cn.simon.simonrpg.mob.reader;

import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.mob.entity.MobItem;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MobItemReader {
    public static ConcurrentHashMap<String, MobItem> ReadConfig(String path){
        ConcurrentHashMap<String, MobItem> mobItemRes = new ConcurrentHashMap<>();

        List<FileConfiguration> mobItemConfigs = ReadingUtil.readConfig(path);

        for (FileConfiguration mobItemConfig : mobItemConfigs) {
            List<Map<?, ?>> tasks = mobItemConfig .getMapList("mob-item");
            for (Map<?, ?> task : tasks) {
                MobItem mobItem = new MobItem();
                //先读取姓名
                String name = (String) task.get(ConfigConst.NAME);

                mobItem.setName(name);

                String isBoss = (String) task.get(ConfigConst.IS_BOSS);

                if (isBoss != null)mobItem.setIsBoss(true);

                HashMap<String,String> item = (HashMap<String, String>) task.get(ConfigConst.ITEM);

                mobItem.setItem(item);
                //读取
                HashMap<String,String> itemNum = (HashMap<String, String>) task.get(ConfigConst.ITEM_NUM);
                if (itemNum != null) mobItem.setItemNum(itemNum);

                mobItemRes.put(name,mobItem);

            }
        }
        return mobItemRes;
    }
}
