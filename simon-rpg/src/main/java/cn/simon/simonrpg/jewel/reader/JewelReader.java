package cn.simon.simonrpg.jewel.reader;

import cn.simon.simonrpg.jewel.entity.Jewel;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JewelReader {
    public static ConcurrentHashMap<String, Jewel> ReadConfig(String path){
        ConcurrentHashMap<String,Jewel> jewelRes = new ConcurrentHashMap<>();
        List<FileConfiguration> jewelConfigs = ReadingUtil.readConfig(path);

        for (FileConfiguration jewelConfig : jewelConfigs) {
            List<Map<?, ?>> jewels = jewelConfig.getMapList("jewel");
            for (Map<?, ?> jewel : jewels) {
                Jewel newJewel = new Jewel();
                String name = (String) jewel.get("name");

                newJewel.setName(name);

                String category = (String) jewel.get("category");

                newJewel.setCategory(category);

                String level = (String) jewel.get("level");
                newJewel.setLevel(level);

                String innerType = (String)jewel.get("inner-type");
                newJewel.setInnerType(innerType);

                String type = (String) jewel.get("type");

                newJewel.setType(type);

                String description = (String) jewel.get("description");

                newJewel.setDescription(description);

                String success = (String)jewel.get("success-percent");
                newJewel.setSuccessPercent(success);

                String prompt = (String)jewel.get("prompt");
                newJewel.setPrompt(prompt);

                String getName = (String) jewel.get("get-name");

                jewelRes.put(getName,newJewel);
            }
        }
        return jewelRes;
    }
}
