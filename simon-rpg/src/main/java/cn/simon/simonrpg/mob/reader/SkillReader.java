package cn.simon.simonrpg.mob.reader;

import cn.simon.simonrpg.constants.ConfigConst;
import cn.simon.simonrpg.constants.MobConst;
import cn.simon.simonrpg.constants.SkillConst;
import cn.simon.simonrpg.mob.entity.Mob;
import cn.simon.simonrpg.mob.entity.Skill;
import cn.simon.simonrpg.util.ReadingUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SkillReader {

    public static ConcurrentHashMap<String, List<Skill>> ReadConfig(String path){
        ConcurrentHashMap<String, List<Skill>> skillRes = new ConcurrentHashMap<>();
        List<FileConfiguration> skillConfigs = ReadingUtil.readConfig(path);
        for (FileConfiguration skillConfig : skillConfigs) {
            String skillNo = skillConfig.getString("skill-no");
            if (skillNo != null) {
                ConfigConst.SKILL_PREFIX = Double.parseDouble(skillNo);
            }
            List<Map<?, ?>> skills = skillConfig.getMapList("skill");
            for (Map<?, ?> skill : skills) {
                String name = (String) skill.get("name");
                List<Skill> mobSkills = new ArrayList<>();
                List<Map<?, ?>> skillDetails = (List<Map<?, ?>>) skill.get("skills");
                for (Map<?, ?> detail : skillDetails) {
                    Skill skillTmp = new Skill();
                    Map<String,Double> skillMap = new HashMap<>();
                    skillMap.put(SkillConst.PHYSICAL_ATTACK_LABEL, (double) detail.get(SkillConst.PHYSICAL_ATTACK_LABEL));
                    skillMap.put(SkillConst.ARMOR_PENETRATION_LABEL, (double) detail.get(SkillConst.ARMOR_PENETRATION_LABEL));
                    skillMap.put(SkillConst.CRITICAL_CHANCE_LABEL, (double) detail.get(SkillConst.CRITICAL_CHANCE_LABEL));
                    skillMap.put(SkillConst.CRITICAL_DAMAGE_LABEL, (double) detail.get(SkillConst.CRITICAL_DAMAGE_LABEL));
                    skillMap.put(SkillConst.PURE_DAMAGE, (double) detail.get(SkillConst.PURE_DAMAGE));
                    skillMap.put(SkillConst.HIT_CHANCE, (double) detail.get(SkillConst.HIT_CHANCE));
                    skillTmp.setAttributes(skillMap);
                    mobSkills.add(skillTmp);
                }
                skillRes.put(name,mobSkills);
                }
            }
        return skillRes;
    }

}
