package cn.simon.simonscoreboard.task;

import cn.simon.simonscoreboard.constants.ServerConst;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ScoreboardUpdaterTask extends BukkitRunnable {

    private final Player player;

    public ScoreboardUpdaterTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        //计分板管理
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        //获取玩家的计分板
        Scoreboard scoreboard = player.getScoreboard();

        //若没有自定义计分板就new一个
        if (scoreboard.equals(scoreboardManager.getMainScoreboard())) {
            scoreboard = scoreboardManager.getNewScoreboard();
        }

        Objective sidebarObject = scoreboard.getObjective("top-info");
        if (sidebarObject == null) {
            sidebarObject = scoreboard.registerNewObjective("top-info", Criteria.DUMMY, ChatColor.AQUA + "   "+ ServerConst.SERVER_NAME + ChatColor.WHITE + " - " + ChatColor.GOLD + "无尽试炼   ");
            sidebarObject.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        //血量刷新
        Team health = scoreboard.getTeam(ChatColor.WHITE+"血量: "+"-info-team");

        if (health != null){
            health.setSuffix(ChatColor.GOLD+String.valueOf(getNum(player.getHealth())));
        }

        Team money = scoreboard.getTeam(ChatColor.WHITE+"银币: "+"-info-team");

        if (money != null){
            money.setSuffix(ChatColor.YELLOW+ PlaceholderAPI.setPlaceholders(player,"%vault_eco_balance%"));
        }

        Team title = scoreboard.getTeam(ChatColor.WHITE+"称号: "+"-info-team");
        if (title != null){
            title.setSuffix(PlaceholderAPI.setPlaceholders(player,"%playerTitle_use%"));
        }

        Team points = scoreboard.getTeam(ChatColor.WHITE+"点卷: "+"-info-team");
        if (points != null){
           points.setSuffix(ChatColor.GOLD+PlaceholderAPI.setPlaceholders(player,"%playerpoints_points%"));
        }

        Team ping = scoreboard.getTeam(ChatColor.WHITE+"延迟: "+"-info-team");
        if (ping != null){
            ping.setSuffix(ChatColor.GOLD+PlaceholderAPI.setPlaceholders(player,ChatColor.YELLOW+String.valueOf(player.getPing())+"毫秒"));
        }

        player.setScoreboard(scoreboard);
    }

    private static double getNum(double num){
        //保留2位小数
        double score = new BigDecimal(num).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        //不足两位则补0
        DecimalFormat decimalFormat = new DecimalFormat("0.0#");
        return Double.parseDouble(decimalFormat.format(num));
    }
}