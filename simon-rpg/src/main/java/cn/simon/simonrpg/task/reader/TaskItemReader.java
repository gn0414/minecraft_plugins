package cn.simon.simonrpg.task.reader;

import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.task.builder.TaskItemBuilder;
import cn.simon.simonrpg.task.entity.TaskItem;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TaskItemReader {

    public static ConcurrentHashMap<String, ItemStack> ReadConfig(String path){
        ConcurrentHashMap<String,ItemStack> taskRes = new ConcurrentHashMap<>();

        List<FileConfiguration> taskConfigs= ReadingUtil.readConfig(path);

        for (FileConfiguration taskConfig : taskConfigs) {
            List<Map<?, ?>> tasks = taskConfig.getMapList("task");
            for (Map<?, ?> task : tasks) {
                TaskItem taskItem = new TaskItem();
                //先读取姓名
                String name = (String) task.get(ConfigConst.NAME);

                taskItem.setName(name);

                //类别
                String category = (String) task.get(ConfigConst.CATEGORY);
                taskItem.setCategory(category);

                //品质
                String type = (String) task.get(ConfigConst.TYPE);
                taskItem.setType(type);
                //介绍
                String description = (String) task.get(ConfigConst.DESCRIPTION);

                taskItem.setDescription(description);

                taskRes.put(taskItem.getName().substring(2), TaskItemBuilder.makeTaskItem(taskItem));

            }
        }
        return taskRes;
    }
}
