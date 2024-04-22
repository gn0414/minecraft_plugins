package cn.simon.simonrpg.mob.entity;

import java.util.Map;

public class Skill {


   private Map<String,Double> attributes;

    public Map<String,Double> getAttributes() {
        return attributes;
    }


    public void setAttributes(Map<String, Double> attributes) {
        this.attributes = attributes;
    }



    @Override
    public String toString() {
        return "Skill{" +
                ", attributes=" + attributes +
                '}';
    }
}
