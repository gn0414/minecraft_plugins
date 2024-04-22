package cn.simon.simonrpg.stat.holder;

import cn.simon.simonrpg.mob.entity.Mob;
import cn.simon.simonrpg.stat.entity.Stat;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatHolder {

    public static Map<Player, Stat> PLAYER_STAT = new ConcurrentHashMap<>();

    public static Map<String, Mob> mobStat = new ConcurrentHashMap<>();
}
