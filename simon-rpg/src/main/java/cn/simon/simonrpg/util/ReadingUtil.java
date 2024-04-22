package cn.simon.simonrpg.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ReadingUtil {

    private static final Logger logger = Logger.getLogger("ReadUtils");
    public static List<FileConfiguration> readConfig(String path){
        List<FileConfiguration> configurations = new ArrayList<>();
        logger.info("reading path:"+path);
        File directory = new File(path);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".yml"));

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    FileConfiguration configuration = new YamlConfiguration();

                    try {
                        configuration.load(file);
                        configurations.add(configuration);
                    } catch (IOException | InvalidConfigurationException e) {
                        logger.info("path:" + path + "读取出错");
                        // 处理加载错误的逻辑
                    }
                }
            }
        }
        return configurations;
    }
}
