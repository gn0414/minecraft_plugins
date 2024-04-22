package cn.simon.simonrpg.jewel.entity;


public class Jewel {

    private String name;

    private String category;

    private String innerType;

    private String type;

    private String level;

    private String successPercent;

    private String description;

    private String prompt;



    public void setInnerType(String innerType) {
        this.innerType = innerType;
    }

    public String getInnerType() {
        return innerType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setSuccessPercent(String successPercent) {
        this.successPercent = successPercent;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getLevel() {
        return level;
    }

    public String getPrompt() {
        return prompt;
    }

    public String getSuccessPercent() {
        return successPercent;
    }
}
