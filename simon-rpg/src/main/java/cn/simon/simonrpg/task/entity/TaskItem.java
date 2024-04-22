package cn.simon.simonrpg.task.entity;

public class TaskItem {


    private String name;

    private String type;

    private String category;

    private String description;

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }


    public String getType() {
        return type;
    }
}
