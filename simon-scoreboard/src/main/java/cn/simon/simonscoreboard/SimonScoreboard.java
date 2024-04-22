package cn.simon.simonscoreboard;

import cn.simon.simonscoreboard.listener.PlayerListener;
import cn.simon.simonscoreboard.task.ScoreboardUpdaterTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class SimonScoreboard extends JavaPlugin {

    Logger logger = Logger.getLogger("SimonScoreboard");

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getScheduler().runTaskTimer(this, () -> {
            // 获取所有在线玩家并为他们创建或更新计分板
            for (Player player : Bukkit.getOnlinePlayers()) {
                new ScoreboardUpdaterTask(player).run();
            }
        }, 0L, 600L);
        outLogo();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private void outLogo(){
        logger.info("");
    }



}
