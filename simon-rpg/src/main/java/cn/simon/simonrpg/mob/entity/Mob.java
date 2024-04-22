package cn.simon.simonrpg.mob.entity;

import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Map;

public class Mob {

    private String name;
    private Map<String,String> attributes;



    public void setName(String name) {
        this.name = name;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }


    @Override
    public String toString() {
        return "Mob{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
