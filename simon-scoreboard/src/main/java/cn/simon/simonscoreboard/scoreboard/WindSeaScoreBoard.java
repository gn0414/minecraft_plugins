package cn.simon.simonscoreboard.scoreboard;

import cn.simon.simonscoreboard.constants.ServerConst;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.LinkedHashMap;
import java.util.Objects;

public class WindSeaScoreBoard{

    public static void createSidebarScoreboard(Player player) {
        //计分板管理
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        //获取玩家的计分板
        Scoreboard scoreboard = player.getScoreboard();

        //若没有自定义计分板就new一个
        if (scoreboard.equals(scoreboardManager.getMainScoreboard())){
            scoreboard = scoreboardManager.getNewScoreboard();
        }

        Objective sidebarObject = scoreboard.getObjective("top-info");
        if (Objects.isNull(sidebarObject)){
            sidebarObject = scoreboard.registerNewObjective("top-info", Criteria.DUMMY, ChatColor.AQUA+"   "+ ServerConst.SERVER_NAME +ChatColor.WHITE+" - "+ChatColor.GOLD+"无尽试炼   ");
            sidebarObject.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        sidebarObject.getScore(" ").setScore(15);
        sidebarObject.getScore(ChatColor.GOLD+"个人信息: ").setScore(14);
        sidebarObject.getScore(ChatColor.WHITE+"称号: ").setScore(13);
        sidebarObject.getScore(ChatColor.WHITE+"点卷: ").setScore(12);
        sidebarObject.getScore(ChatColor.WHITE+"会员: ").setScore(11);
        sidebarObject.getScore(ChatColor.WHITE+"延迟: ").setScore(10);
        sidebarObject.getScore("  ").setScore(9);
        sidebarObject.getScore(ChatColor.GOLD+"试炼信息: ").setScore(8);
        sidebarObject.getScore(ChatColor.WHITE+"特权: "+ChatColor.RED+"未启用").setScore(7);
        sidebarObject.getScore(ChatColor.WHITE+"银币: ").setScore(6);
        sidebarObject.getScore(ChatColor.WHITE+"血量: ").setScore(5);
        sidebarObject.getScore(ChatColor.WHITE+"技艺: ").setScore(4);
        sidebarObject.getScore(ChatColor.WHITE+"等级: ").setScore(3);
        sidebarObject.getScore("   ").setScore(2);
        sidebarObject.getScore(ChatColor.WHITE+"交流群: "+ChatColor.GOLD+ServerConst.COMMUNITY).setScore(1);

        LinkedHashMap<String,String> info = new LinkedHashMap<>();

        info.put(ChatColor.GOLD+"个人信息: ",ChatColor.GREEN+player.getName());
        info.put(ChatColor.WHITE+"称号: ",PlaceholderAPI.setPlaceholders(player,"%playerTitle_use%"));
        info.put(ChatColor.WHITE+"点卷: ",ChatColor.GOLD+PlaceholderAPI.setPlaceholders(player,"%playerpoints_points%"));
        info.put(ChatColor.WHITE+"会员: ",ChatColor.AQUA+"敬请期待");
        info.put(ChatColor.WHITE+"延迟: ",ChatColor.YELLOW+String.valueOf(player.getPing())+"毫秒");
        info.put(ChatColor.GOLD+"试炼信息: ",ChatColor.AQUA+"频道1");
        info.put(ChatColor.WHITE+"特权: "+ChatColor.RED+"未启用","");
        info.put(ChatColor.WHITE+"银币: ",ChatColor.YELLOW+ PlaceholderAPI.setPlaceholders(player,"%vault_eco_balance%"));
        info.put(ChatColor.WHITE+"血量: ",ChatColor.GOLD+String.valueOf(player.getHealth()));
        info.put(ChatColor.WHITE+"技艺: ",ChatColor.GREEN+"0.0");
        info.put(ChatColor.WHITE+"等级: ",ChatColor.YELLOW+String.valueOf(player.getLevel())+" ⋆");
        info.put(" ","");
        info.put("  ","");
        info.put("   ","");
        info.put(ChatColor.WHITE+"交流群: "+ChatColor.GOLD+ServerConst.COMMUNITY,"");


        doMakeInfo(scoreboard,info,player);

        player.setScoreboard(scoreboard);

    }

    private static void doMakeInfo(Scoreboard scoreboard,LinkedHashMap<String,String> map,Player player){
        if (!map.isEmpty()){

            map.forEach((k,v) -> {
                Team team = scoreboard.getTeam(k+"-info-team");
                if (Objects.isNull(team)){
                    team = scoreboard.registerNewTeam(k+"-info-team");
                    team.addEntry(k);
                    team.setSuffix(v);
                }else{
                    //若不存在相应的key
                    if (!team.hasEntry(k)){
                        team.addEntry(k);
                        team.setSuffix(v);
                    }else{
                        team.setSuffix(v);
                    }
                }
            });
        }
    }
}
