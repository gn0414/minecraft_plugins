package cn.simon.simonscoreboard.listener;


import cn.simon.simonscoreboard.scoreboard.WindSeaScoreBoard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
            WindSeaScoreBoard.createSidebarScoreboard(event.getPlayer());
        // 当玩家加入游戏时触发的事件处理
    }
}
