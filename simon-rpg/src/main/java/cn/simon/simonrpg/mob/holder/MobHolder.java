package cn.simon.simonrpg.mob.holder;

import cn.simon.simonrpg.mob.entity.MobItem;
import cn.simon.simonrpg.mob.entity.Skill;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MobHolder {

    public static ConcurrentHashMap<String, MobItem> mobItemHolder;

    public static ConcurrentHashMap<String, List<Skill>> mobSkills;
}
