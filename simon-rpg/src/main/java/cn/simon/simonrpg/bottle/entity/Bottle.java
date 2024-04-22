package cn.simon.simonrpg.bottle.entity;
import java.util.LinkedHashMap;

/**
 *  瓶子系统
 */
public class Bottle {

    @Override
    public String toString() {
        return "Bottle{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", Reward=" + Reward +
                '}';
    }

    private String name;

    private String description;

    private String category;

    //对应奖励及其概率
    private LinkedHashMap<String,String> Reward;

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setReward(LinkedHashMap<String, String> reward) {
        Reward = reward;
    }

    public LinkedHashMap<String, String> getReward() {
        return Reward;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
